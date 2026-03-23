using computer_project.ApiService.Models;
using System.Text.Json;
using System.Text.Json.Serialization;
using Microsoft.Extensions.AI;

namespace computer_project.ApiService.Services;

public class AIService
{
    private readonly IChatClient _chatClient;
    private readonly ILogger<AIService> _logger;

    private static readonly JsonSerializerOptions s_jsonOptions = new()
    {
        PropertyNameCaseInsensitive = true
    };

    public AIService(IChatClient chatClient, ILogger<AIService> logger)
    {
        _chatClient = chatClient;
        _logger = logger;
    }

    public async Task<string> GetChatResponseAsync(string prompt)
    {
        try
        {
            var systemInstructions = @"You are the SmartBite AI Engine powered by GitHub Models. Provide personalized, highly-structured advice across three core pathways:
1. Diet-Focused: Generate meal plans, macro targets, portion sizes, and grocery list integrations.
2. Gym-Focused: Suggest customized workout routines (strength, HIIT), calorie burn matching, and progressive overload.
3. General Wellness: Provide daily lifestyle routines (hydration, sleep hygiene, step goals).
Format all responses beautifully using Markdown. Be concise but actionable.";

            var messages = new List<ChatMessage>
            {
                new ChatMessage(ChatRole.System, systemInstructions),
                new ChatMessage(ChatRole.User, prompt)
            };

            var response = await _chatClient.GetResponseAsync(messages, new ChatOptions { MaxOutputTokens = 1000, Temperature = 0.7f });
            return response.Text?.Trim() ?? "No response from AI.";
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error calling GitHub Models API");
            return $"AI is currently unavailable. Error: {ex.Message}";
        }
    }

    public async Task<MealNutritionStub?> EstimateNutritionAsync(string mealDescription)
    {
        try
        {
            var prompt = $"Analyze this meal: '{mealDescription}'. " +
                         "Estimate its nutritional values. Return ONLY a JSON object with properties: " +
                         "Calories (double), Protein (double), Carbs (double), Fat (double). " +
                         "Ensure its valid JSON.";

            var content = await GetChatResponseAsync(prompt);
            content = CleanJsonResponse(content);

            return JsonSerializer.Deserialize<MealNutritionStub>(content, s_jsonOptions);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error estimating nutrition for {Meal}", mealDescription);
            return null;
        }
    }

    public record MealNutritionStub(double Calories, double Protein, double Carbs, double Fat);

    public async Task<List<AIRecommendation>> GetRecommendationsAsync(List<Meal> recentMeals, UserGoal? goal)
    {
        try
        {
            var mealSummary = string.Join(", ", recentMeals.Select(m => $"{m.Name} ({m.Calories} kcal)"));
            var goalSummary = goal != null
                ? $"Goal: {goal.TargetCalories} kcal, P: {goal.TargetProtein}g, C: {goal.TargetCarbs}g, F: {goal.TargetFat}g"
                : "No specific goal set.";

            var prompt = $"Based on these recent meals: {mealSummary}. {goalSummary}. " +
                         "Suggest 2 healthy meals. Return ONLY a JSON array of objects with properties: " +
                         "SuggestedMeal (string), Reason (string), EstimatedCalories (int).";

            var content = await GetChatResponseAsync(prompt);
            content = CleanJsonResponse(content);

            return JsonSerializer.Deserialize<List<AIRecommendation>>(content, s_jsonOptions) ?? [];
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error getting AI recommendations");
            return [];
        }
    }

    public async Task<string> GenerateHealthReportAsync(HealthReport report, UserGoal? goal)
    {
        try
        {
            var prompt = $"Analyze this health report: Total Calories: {report.TotalCalories}, " +
                         $"Protein: {report.Protein}g, Carbs: {report.Carbs}g, Fat: {report.Fat}g. " +
                         $"Target Calories: {goal?.TargetCalories ?? 2000}. " +
                         "Provide a concise, encouraging 2-3 sentence health assessment.";

            return await GetChatResponseAsync(prompt);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error generating health report assessment");
            return "Could not generate assessment at this time.";
        }
    }

    public async Task<string> GetWaterAdviceAsync(double totalWater, double targetWater)
    {
        try
        {
            var prompt = $"The user has drunk {totalWater:F1}L of water today. " +
                         $"Their goal is {targetWater:F1}L. " +
                         "Provide a short, motivating tip (max 20 words) for their water intake.";

            return await GetChatResponseAsync(prompt);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error getting water advice");
            return "Stay hydrated! Remember to drink water throughout the day.";
        }
    }

    private string CleanJsonResponse(string content)
    {
        content = content.Trim();
        if (content.StartsWith("```json")) content = content[7..].Trim();
        else if (content.StartsWith("```")) content = content[3..].Trim();
        if (content.EndsWith("```")) content = content[..^3].Trim();
        return content;
    }

    public async Task<object> CheckStatusAsync()
    {
        try
        {
            // Simple ping to check status
            await _chatClient.GetResponseAsync([new ChatMessage(ChatRole.User, "ping")], new ChatOptions { MaxOutputTokens = 1 });
            return new { Status = "online", Endpoint = "GitHub Models API (MEAI)", Model = "gpt-4o-mini" };
        }
        catch
        {
            return new { Status = "offline", Endpoint = "GitHub Models API (MEAI)" };
        }
    }
}
