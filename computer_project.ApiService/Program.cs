using computer_project.ApiService.Data;
using computer_project.ApiService.Models;
using computer_project.ApiService.Services;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add service defaults & Aspire client integrations.
builder.AddServiceDefaults();

// Add services to the container.
builder.Services.AddProblemDetails();
builder.Services.AddOpenApi();
builder.Services.AddResponseCompression();

// Register AIService as singleton with its own HttpClient (bypasses Aspire's
// standard resilience 10s timeout — local AI inference needs much longer)
builder.AddAzureChatCompletionsClient("chatModel").AddChatClient();
builder.Services.AddSingleton<AIService>();

// Swap SQLite for PostgreSQL managed by Aspire
builder.AddNpgsqlDbContext<AppDbContext>("smartbitedb");

// Add Redis Distributed Caching automatically via Aspire
builder.AddRedisDistributedCache("cache");

var app = builder.Build();

// Configure the HTTP request pipeline.
app.UseExceptionHandler();
app.UseResponseCompression();

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

// Seed data
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.EnsureCreated();
    if (!db.Users.Any())
    {
        db.Users.Add(new User { Username = "user", PasswordHash = "password", Role = "EndUser" });
        db.Users.Add(new User { Username = "Admin", PasswordHash = "Admin1234", Role = "Admin" });
        db.UserGoals.Add(new UserGoal
        {
            UserId = 1,
            TargetCalories = 2200,
            TargetProtein = 160,
            TargetCarbs = 250,
            TargetFat = 70,
            TargetWater = 3.0
        });

        // Add some random meals for the last 7 days
        var random = new Random();
        var mealNames = new[] { "Chicken Breast", "Oatmeal", "Greek Yogurt", "Salmon & Avocado", "Quinoa Salad", "Protein Shake", "Steak & Potatoes", "Brown Rice & Beans" };
        for (int i = 0; i < 20; i++)
        {
            db.Meals.Add(new Meal
            {
                UserId = 1,
                Name = mealNames[random.Next(mealNames.Length)],
                Calories = random.Next(300, 800),
                Protein = random.Next(20, 50),
                Carbs = random.Next(10, 80),
                Fat = random.Next(5, 30),
                LoggedAt = DateTime.UtcNow.AddDays(-random.Next(0, 7))
            });
        }

        // Add some random water intake
        for (int i = 0; i < 15; i++)
        {
            db.WaterIntakes.Add(new WaterIntake
            {
                UserId = 1,
                Amount = random.NextDouble() * 1.5,
                LoggedAt = DateTime.UtcNow.AddDays(-random.Next(0, 7))
            });
        }

        db.ShoppingListItems.Add(new ShoppingListItem { UserId = 1, ItemName = "Avocados", Quantity = "3", IsPurchased = false });
        db.ShoppingListItems.Add(new ShoppingListItem { UserId = 1, ItemName = "Chicken Breast", Quantity = "1kg", IsPurchased = true });
        db.ShoppingListItems.Add(new ShoppingListItem { UserId = 1, ItemName = "Greek Yogurt", Quantity = "500g", IsPurchased = false });

        db.SaveChanges();
    }
}

// --- Endpoints ---

// User
app.MapGet("/users", async (AppDbContext db) =>
    await db.Users.AsNoTracking().ToListAsync());

app.MapGet("/users/{id}", async (int id, AppDbContext db) =>
    await db.Users.FindAsync(id) is User user ? Results.Ok(user) : Results.NotFound());

app.MapPost("/users", async (UserRegistrationRequest request, AppDbContext db) =>
{
    if (await db.Users.AnyAsync(u => u.Username == request.Username))
        return Results.BadRequest("Username already exists");

    var user = new User
    {
        Username = request.Username,
        PasswordHash = request.Password // Simple demo, no hashing
    };
    db.Users.Add(user);
    await db.SaveChangesAsync();
    return Results.Created($"/users/{user.Id}", user);
});

app.MapPost("/login", async (LoginRequest request, AppDbContext db) =>
{
    var user = await db.Users.FirstOrDefaultAsync(u => u.Username == request.Username && u.PasswordHash == request.Password);
    return user is not null ? Results.Ok(user) : Results.Unauthorized();
});

app.MapDelete("/users/{id}", async (int id, AppDbContext db) =>
{
    var user = await db.Users.FindAsync(id);
    if (user is null) return Results.NotFound();
    db.Users.Remove(user);
    await db.SaveChangesAsync();
    return Results.NoContent();
});

app.MapPut("/users/{id}", async (int id, User updatedUser, AppDbContext db) =>
{
    var user = await db.Users.FindAsync(id);
    if (user is null) return Results.NotFound();

    user.Username = updatedUser.Username;
    user.Role = updatedUser.Role;
    // Password update omitted for simplicity or you can add it if needed

    await db.SaveChangesAsync();
    return Results.NoContent();
});

// Meals
app.MapGet("/meals", async (AppDbContext db) =>
    await db.Meals.AsNoTracking().OrderByDescending(m => m.LoggedAt).ToListAsync());

app.MapPost("/meals", async (Meal meal, AppDbContext db) =>
{
    db.Meals.Add(meal);
    await db.SaveChangesAsync();
    return Results.Created($"/meals/{meal.Id}", meal);
});

// Shopping List
app.MapGet("/shoppinglist", async (AppDbContext db) =>
    await db.ShoppingListItems.AsNoTracking().ToListAsync());

app.MapPost("/shoppinglist", async (ShoppingListItem item, AppDbContext db) =>
{
    db.ShoppingListItems.Add(item);
    await db.SaveChangesAsync();
    return Results.Created($"/shoppinglist/{item.Id}", item);
});

app.MapPut("/shoppinglist/{id}", async (int id, ShoppingListItem inputItem, AppDbContext db) =>
{
    var item = await db.ShoppingListItems.FindAsync(id);
    if (item is null) return Results.NotFound();

    item.IsPurchased = inputItem.IsPurchased;
    item.ItemName = inputItem.ItemName;
    item.Quantity = inputItem.Quantity;
    item.PaymentMethod = inputItem.PaymentMethod;

    await db.SaveChangesAsync();
    return Results.NoContent();
});

app.MapDelete("/shoppinglist/{id}", async (int id, AppDbContext db) =>
{
    var item = await db.ShoppingListItems.FindAsync(id);
    if (item is null) return Results.NotFound();

    db.ShoppingListItems.Remove(item);
    await db.SaveChangesAsync();

    return Results.NoContent();
});

// Stats / Reports
app.MapGet("/stats", async (AppDbContext db, AIService ai) =>
{
    var stats = await db.Meals.GroupBy(_ => 1).Select(g => new
    {
        TotalCalories = g.Sum(m => m.Calories),
        MealCount = g.Count(),
        Protein = g.Sum(m => m.Protein),
        Carbs = g.Sum(m => m.Carbs),
        Fat = g.Sum(m => m.Fat)
    }).FirstOrDefaultAsync();

    var goal = await db.UserGoals.AsNoTracking().FirstOrDefaultAsync(g => g.UserId == 1);

    var report = new HealthReport
    {
        UserId = 1,
        TotalCalories = stats?.TotalCalories ?? 0,
        MealCount = stats?.MealCount ?? 0,
        Protein = stats?.Protein ?? 0,
        Carbs = stats?.Carbs ?? 0,
        Fat = stats?.Fat ?? 0,
        GeneratedAt = DateTime.UtcNow
    };

    try
    {
        report.Summary = await ai.GenerateHealthReportAsync(report, goal);
    }
    catch
    {
        report.Summary = "AI summary unavailable — start AI Foundry Local for insights.";
    }
    return report;
});

// AI Recommendations
app.MapGet("/recommendations", async (AppDbContext db, AIService ai) =>
{
    try
    {
        var recentMeals = await db.Meals.AsNoTracking().OrderByDescending(m => m.LoggedAt).Take(10).ToListAsync();
        var goal = await db.UserGoals.AsNoTracking().FirstOrDefaultAsync(g => g.UserId == 1);
        return await ai.GetRecommendationsAsync(recentMeals, goal);
    }
    catch
    {
        return new List<AIRecommendation>();
    }
});

app.MapPost("/ai/chat", async (ChatRequest request, AIService ai) =>
{
    var response = await ai.GetChatResponseAsync(request.Prompt);
    return Results.Ok(response);
});

app.MapGet("/ai/estimate", async (string mealDescription, AIService ai) =>
{
    var estimation = await ai.EstimateNutritionAsync(mealDescription);
    return estimation is not null ? Results.Ok(estimation) : Results.NotFound();
});

app.MapGet("/ai/status", async (AIService ai) =>
{
    try
    {
        var response = await ai.CheckStatusAsync();
        return Results.Ok(response);
    }
    catch
    {
        return Results.Ok(new { Status = "offline", Endpoint = "" });
    }
});

// Water Tracking
app.MapGet("/water", async (AppDbContext db) =>
    await db.WaterIntakes.AsNoTracking().OrderByDescending(w => w.LoggedAt).ToListAsync());

app.MapGet("/water/advice", async (double current, double target, AIService ai) =>
{
    try
    {
        return await ai.GetWaterAdviceAsync(current, target);
    }
    catch
    {
        return "Stay hydrated! Start AI Foundry Local for personalized advice.";
    }
});

app.MapPost("/water", async (WaterIntake intake, AppDbContext db) =>
{
    db.WaterIntakes.Add(intake);
    await db.SaveChangesAsync();
    return Results.Created($"/water/{intake.Id}", intake);
});

// User Goals
app.MapGet("/goals/{userId}", async (int userId, AppDbContext db) =>
    await db.UserGoals.AsNoTracking().FirstOrDefaultAsync(g => g.UserId == userId) is UserGoal goal
        ? Results.Ok(goal)
        : Results.NotFound());

app.MapPut("/goals/{userId}", async (int userId, UserGoal updatedGoal, AppDbContext db) =>
{
    var goal = await db.UserGoals.FirstOrDefaultAsync(g => g.UserId == userId);
    if (goal is null) return Results.NotFound();

    goal.TargetCalories = updatedGoal.TargetCalories;
    goal.TargetProtein = updatedGoal.TargetProtein;
    goal.TargetCarbs = updatedGoal.TargetCarbs;
    goal.TargetFat = updatedGoal.TargetFat;
    goal.TargetWater = updatedGoal.TargetWater;

    await db.SaveChangesAsync();
    return Results.NoContent();
});

app.MapDefaultEndpoints();

app.Run();

record ChatRequest(string Prompt);
public record UserRegistrationRequest(string Username, string Password);
public record LoginRequest(string Username, string Password);




