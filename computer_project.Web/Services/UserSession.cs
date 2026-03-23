namespace computer_project.Web.Services;

using computer_project.Web.Models;

public class UserSession
{
    public User? CurrentUser { get; private set; }
    public bool IsLoggedIn => CurrentUser != null;
    public bool IsAdmin => CurrentUser?.Role == "Admin";
    public bool IsEndUser => CurrentUser?.Role == "EndUser";
    public bool IsDarkMode { get; set; }
    public string TimeZoneId { get; set; } = "UTC";
    public string SearchQuery { get; private set; } = string.Empty;
    public bool PaymentCompleted { get; set; }
    public string PreferredPaymentMethod { get; set; } = "Cash";

    public event Action? OnChange;

    public void Login(User user)
    {
        CurrentUser = user;
        NotifyStateChanged();
    }

    public void Logout()
    {
        CurrentUser = null;
        NotifyStateChanged();
    }

    public void SetTheme(bool dark)
    {
        IsDarkMode = dark;
        NotifyStateChanged();
    }

    public void SetSearch(string query)
    {
        SearchQuery = query;
        NotifyStateChanged();
    }

    public DateTime ToLocalTime(DateTime utcTime)
    {
        try
        {
            var tzi = TimeZoneInfo.FindSystemTimeZoneById(TimeZoneId);
            return TimeZoneInfo.ConvertTimeFromUtc(utcTime, tzi);
        }
        catch
        {
            return utcTime;
        }
    }

    public void NotifyStateChanged() => OnChange?.Invoke();
}
