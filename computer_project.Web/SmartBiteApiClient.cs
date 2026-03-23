using computer_project.Web.Models;

namespace computer_project.Web;

public class SmartBiteApiClient(HttpClient httpClient)
{
    public async Task<List<Meal>> GetMealsAsync(CancellationToken cancellationToken = default)
    {
        return await httpClient.GetFromJsonAsync<List<Meal>>("/meals", cancellationToken) ?? [];
    }

    public async Task AddMealAsync(Meal meal, CancellationToken cancellationToken = default)
    {
        await httpClient.PostAsJsonAsync("/meals", meal, cancellationToken);
    }

    public async Task<List<ShoppingListItem>> GetShoppingListAsync(CancellationToken cancellationToken = default)
    {
        return await httpClient.GetFromJsonAsync<List<ShoppingListItem>>("/shoppinglist", cancellationToken) ?? [];
    }

    public async Task AddShoppingListItemAsync(ShoppingListItem item, CancellationToken cancellationToken = default)
    {
        await httpClient.PostAsJsonAsync("/shoppinglist", item, cancellationToken);
    }

    public async Task UpdateShoppingListItemAsync(ShoppingListItem item, CancellationToken cancellationToken = default)
    {
        await httpClient.PutAsJsonAsync($"/shoppinglist/{item.Id}", item, cancellationToken);
    }

    public async Task DeleteShoppingListItemAsync(int id, CancellationToken cancellationToken = default)
    {
        await httpClient.DeleteAsync($"/shoppinglist/{id}", cancellationToken);
    }

    public async Task<HealthReport?> GetReportAsync(CancellationToken cancellationToken = default)
    {
        return await httpClient.GetFromJsonAsync<HealthReport>("/stats", cancellationToken);
    }

    public async Task<List<AIRecommendation>> GetRecommendationsAsync(CancellationToken cancellationToken = default)
    {
        return await httpClient.GetFromJsonAsync<List<AIRecommendation>>("/recommendations", cancellationToken) ?? [];
    }

    public async Task<List<WaterIntake>> GetWaterIntakesAsync(CancellationToken ct = default)
    {
        return await httpClient.GetFromJsonAsync<List<WaterIntake>>("/water", ct) ?? [];
    }

    public async Task<string> GetWaterAdviceAsync(double current, double target, CancellationToken ct = default)
    {
        var response = await httpClient.GetAsync($"/water/advice?current={current}&target={target}", ct);
        var text = await response.Content.ReadAsStringAsync(ct);
        try
        {
            return System.Text.Json.JsonSerializer.Deserialize<string>(text) ?? text;
        }
        catch
        {
            return text;
        }
    }

    public async Task AddWaterIntakeAsync(WaterIntake intake, CancellationToken ct = default)
    {
        await httpClient.PostAsJsonAsync("/water", intake, ct);
    }

    public async Task<UserGoal?> GetGoalAsync(int userId, CancellationToken ct = default)
    {
        try
        {
            return await httpClient.GetFromJsonAsync<UserGoal>($"/goals/{userId}", ct);
        }
        catch (HttpRequestException ex) when (ex.StatusCode == System.Net.HttpStatusCode.NotFound)
        {
            return null;
        }
    }

    public async Task UpdateGoalAsync(UserGoal goal, CancellationToken ct = default)
    {
        await httpClient.PutAsJsonAsync($"/goals/{goal.UserId}", goal, ct);
    }

    public async Task<string> GetAICoachAdviceAsync(string prompt, CancellationToken ct = default)
    {
        var response = await httpClient.PostAsJsonAsync("/ai/chat", new { prompt }, ct);
        var text = await response.Content.ReadAsStringAsync(ct);
        try
        {
            return System.Text.Json.JsonSerializer.Deserialize<string>(text) ?? text;
        }
        catch
        {
            return text;
        }
    }

    public async Task<bool> RegisterUserAsync(string username, string password, CancellationToken ct = default)
    {
        var response = await httpClient.PostAsJsonAsync("/users", new { Username = username, Password = password }, ct);
        return response.IsSuccessStatusCode;
    }

    public async Task<User?> LoginAsync(string username, string password, CancellationToken ct = default)
    {
        var response = await httpClient.PostAsJsonAsync("/login", new { Username = username, Password = password }, ct);
        if (response.IsSuccessStatusCode)
        {
            return await response.Content.ReadFromJsonAsync<User>(ct);
        }
        return null;
    }

    public async Task<List<User>> GetUsersAsync(CancellationToken ct = default)
    {
        return await httpClient.GetFromJsonAsync<List<User>>("/users", ct) ?? [];
    }

    public async Task DeleteUserAsync(int id, CancellationToken ct = default)
    {
        await httpClient.DeleteAsync($"/users/{id}", ct);
    }

    public async Task UpdateUserAsync(User user, CancellationToken ct = default)
    {
        await httpClient.PutAsJsonAsync($"/users/{user.Id}", user, ct);
    }

    public async Task<NutritionEstimation?> GetNutritionEstimationAsync(string mealDescription, CancellationToken ct = default)
    {
        return await httpClient.GetFromJsonAsync<NutritionEstimation>($"/ai/estimate?mealDescription={Uri.EscapeDataString(mealDescription)}", ct);
    }

    public async Task<bool> IsAIOnlineAsync(CancellationToken ct = default)
    {
        try
        {
            var result = await httpClient.GetFromJsonAsync<AIStatusResponse>("/ai/status", ct);
            return result?.Status == "online";
        }
        catch
        {
            return false;
        }
    }
}

public record NutritionEstimation(double Calories, double Protein, double Carbs, double Fat);
public record AIStatusResponse(string Status, string Endpoint);


