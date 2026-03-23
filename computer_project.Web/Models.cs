namespace computer_project.Web.Models;

public class User
{
    public int Id { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Role { get; set; } = "EndUser";
    public string Pathway { get; set; } = "General"; // Gym, Diet, General
}

public class Meal
{
    public int Id { get; set; }
    public int UserId { get; set; }
    public string Name { get; set; } = string.Empty;
    public double Calories { get; set; }
    public double Protein { get; set; }
    public double Carbs { get; set; }
    public double Fat { get; set; }
    public DateTime LoggedAt { get; set; } = DateTime.UtcNow;
}

public class ShoppingListItem
{
    public int Id { get; set; }
    public int UserId { get; set; }
    public string ItemName { get; set; } = string.Empty;
    public string? Quantity { get; set; }
    public bool IsPurchased { get; set; } = false;
    public string PaymentMethod { get; set; } = "Cash";
}

public class HealthReport
{
    public int UserId { get; set; }
    public double TotalCalories { get; set; }
    public int MealCount { get; set; }
    public double Protein { get; set; }
    public double Carbs { get; set; }
    public double Fat { get; set; }
    public string Summary { get; set; } = string.Empty;
    public DateTime GeneratedAt { get; set; }
}


public class AIRecommendation
{
    public int Id { get; set; }
    public string SuggestedMeal { get; set; } = string.Empty;
    public string Reason { get; set; } = string.Empty;
    public int EstimatedCalories { get; set; }
}

public class WaterIntake
{
    public int Id { get; set; }
    public int UserId { get; set; }
    public double Amount { get; set; }
    public DateTime LoggedAt { get; set; } = DateTime.UtcNow;
}

public class UserGoal
{
    public int Id { get; set; }
    public int UserId { get; set; }
    public double TargetCalories { get; set; }
    public double TargetProtein { get; set; }
    public double TargetCarbs { get; set; }
    public double TargetFat { get; set; }
    public double TargetWater { get; set; }
    public string TimeZoneId { get; set; } = "UTC";
    public string Region { get; set; } = "Global";
    public bool IsDarkMode { get; set; } = false;
}
