using computer_project.ApiService.Models;
using Microsoft.EntityFrameworkCore;

namespace computer_project.ApiService.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<User> Users => Set<User>();
    public DbSet<Meal> Meals => Set<Meal>();
    public DbSet<ShoppingListItem> ShoppingListItems => Set<ShoppingListItem>();
    public DbSet<HealthReport> HealthReports => Set<HealthReport>();
    public DbSet<AIRecommendation> AIRecommendations => Set<AIRecommendation>();
    public DbSet<WaterIntake> WaterIntakes => Set<WaterIntake>();
    public DbSet<UserGoal> UserGoals => Set<UserGoal>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Meal>(e =>
        {
            e.HasIndex(m => m.UserId);
            e.HasIndex(m => m.LoggedAt);
        });

        modelBuilder.Entity<WaterIntake>(e =>
        {
            e.HasIndex(w => w.UserId);
            e.HasIndex(w => w.LoggedAt);
        });

        modelBuilder.Entity<ShoppingListItem>()
            .HasIndex(s => s.UserId);

        modelBuilder.Entity<HealthReport>()
            .HasIndex(h => h.UserId);

        modelBuilder.Entity<UserGoal>()
            .HasIndex(g => g.UserId)
            .IsUnique();

        modelBuilder.Entity<User>()
            .HasIndex(u => u.Username)
            .IsUnique();
    }
}

