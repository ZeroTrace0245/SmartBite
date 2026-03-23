# Contributing to SmartBite

> Contribution guide for the PUSL2021 Computing Group Project.

---

## Getting Started

1. Clone the repo and switch to a feature branch:
   ```bash
   git clone https://github.com/ZeroTrace0245/computer_project.git
   cd computer_project
   git checkout -b feature/<your-area>
   ```
2. Restore and run:
   ```bash
   dotnet restore
   dotnet run --project computer_project.ApiService
   dotnet watch --project computer_project.Web
   ```
3. Make changes, test locally, commit with a clear message, and open a pull request.

---

## Contribution Areas

### Member 1 — Layout & Responsiveness (DGJKM Madugalla)
**Goal**: Maintain the mica/acrylic visual identity and responsive layout.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Layout/MainLayout.razor` | Shell layout (sidebar, content area, header) |
| `computer_project.Web/wwwroot/app.css` | All CSS: mica/acrylic backdrop, dark/light, cards |

**Tasks**:
- Adjust sidebar breakpoints for mobile viewports.
- Fine-tune acrylic blur/opacity values.
- Ensure `data-bs-theme` toggle propagates to all nested components.

---

### Member 2 — Theming Pipeline (Sathira lakshan)
**Goal**: Keep sidebar navigation in sync with page routes.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Layout/NavMenu.razor` | Sidebar links, icons, active-state highlighting |
| Each `@page` directive in `Pages/*.razor` | Route registration |

**Tasks**:
- Add/remove nav items when pages are created or renamed.
- Maintain consistent icon usage (`bi bi-*`).
- Test deep-link navigation (paste URL directly).

---

### Member 3 — Navigation & Routing (Rhls.dayananda)
**Goal**: Manage login state, roles, and conditional UI rendering.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Services/UserSession.cs` | `CurrentUser`, `IsAdmin`, `IsEndUser`, `Login()`, `Logout()` |
| `computer_project.Web/Components/ConsumerOnly.razor` | Blocks admin from consumer-only pages |

**Tasks**:
- Ensure `OnChange` event fires on every state mutation.
- Add new role gates if needed (`AdminOnly`, etc.).
- Harden logout to clear all session fields.

---

### Member 4 — Session State (KGSN Bandara)
**Goal**: Meal CRUD and history display.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/MealLogging.razor` | UI: form inputs, table, CSV export |
| `computer_project.Web/SmartBiteApiClient.cs` | `GetMealsAsync()`, `AddMealAsync()` |
| `computer_project.ApiService/Program.cs` | `GET /meals`, `POST /meals` endpoints |

**Tasks**:
- Validate input before POST (non-empty name, positive calories).
- Add inline editing or delete for existing meals.
- Extend CSV export with additional columns if needed.

---

### Member 5 — Auth Flows (Athukoralage Pabasara)
**Goal**: Water intake logging and history.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/WaterTracking.razor` | UI: log form, intake list |
| `computer_project.Web/SmartBiteApiClient.cs` | `GetWaterIntakesAsync()`, `AddWaterIntakeAsync()` |
| `computer_project.ApiService/Program.cs` | `GET /water`, `POST /water` endpoints |

**Tasks**:
- Add daily total calculation.
- Show progress toward `UserGoal.TargetWater`.

---

### Member 6 — Header Actions (Abekon Abekon)
**Goal**: Grocery management with payment tagging and checkout tracker.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/ShoppingList.razor` | UI: add/delete items, checkout progress |
| `computer_project.Web/SmartBiteApiClient.cs` | `GetShoppingListAsync()`, `AddShoppingListItemAsync()`, `DeleteShoppingListItemAsync()` |
| `computer_project.ApiService/Program.cs` | `GET /shoppinglist`, `POST /shoppinglist`, `DELETE /shoppinglist/{id}` |

**Tasks**:
- Polish checkout tracker animation.
- Allow editing item quantity after creation.
- Add category/tag filtering.

---

### Member 7 — Profile Chip (D.M.Nisansala Niroshani)
**Goal**: Summary cards, daily stats, and export.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/Dashboard.razor` | Summary cards (calories, protein, meals, water) |
| `computer_project.Web/Components/Pages/Reports.razor` | Detailed report view, CSV export |
| `computer_project.ApiService/Program.cs` | `GET /stats` endpoint |

**Tasks**:
- Add trend charts (line/bar) using a JS chart library.
- Extend CSV to include water and grocery data.
- Add PDF export option.

---

### Member 8 — Feedback / Contact (BSB ABEYSOORIYA)
**Goal**: Admin-only configuration and user management.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/Settings.razor` | Goal config, theme, user list (admin only) |
| `computer_project.ApiService/Program.cs` | `GET /users`, `GET /goals`, `POST /goals` |

**Tasks**:
- Add user deletion from admin panel.
- Allow admin to reset a user's password.
- Validate goal values (no negatives).

---

### Member 9 — API Client & Database (Sachitha Rathnayaka)
**Goal**: Contact/help entry point for users.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/Feedback.razor` | Feedback form UI |

**Tasks**:
- Persist feedback to the database (new `Feedback` entity + endpoint).
- Add success/error toast notifications.
- Display submitted feedback list for admins.

---

### Member 10 — Database Design & SQL (AMGG ADHIKARI)

**Goal**: Design, maintain, and evolve the SQLite database schema and EF Core data layer.

| File | Purpose |
| --- | --- |
| `computer_project.ApiService/Data/AppDbContext.cs` | EF Core `DbContext` — 7 DbSets (Users, Meals, ShoppingListItems, HealthReports, AIRecommendations, WaterIntakes, UserGoals) |
| `computer_project.ApiService/Models.cs` | All entity classes: `User`, `Meal`, `WaterIntake`, `ShoppingListItem`, `HealthReport`, `UserGoal`, `AIRecommendation` |
| `computer_project.ApiService/Program.cs` (lines 17-18) | SQLite connection string: `Data Source=SmartBite.db` |
| `computer_project.ApiService/Program.cs` (lines 31-82) | Seed data block — pre-populates DB on first run |
| `computer_project.ApiService/computer_project.ApiService.csproj` | NuGet: `Microsoft.EntityFrameworkCore.Sqlite` v10.0.3 |

**Key code — SQLite registration** (`Program.cs`):
```csharp
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlite("Data Source=SmartBite.db"));
```

**Key code — DbContext** (`AppDbContext.cs`):
```csharp
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
}
```

**Tasks**:
- Add `OnModelCreating` overrides to define explicit FK relationships and indexes.
- Create EF Core migrations (`dotnet ef migrations add <Name>`).
- Add cascade-delete rules between `User` and child tables.
- Add data annotations or Fluent API constraints (e.g., `[Required]`, max lengths).
- Validate normalization compliance (1NF/2NF/3NF) — see README Database Design section.

**Migration commands**:
```bash
# Install EF tools (once)
dotnet tool install --global dotnet-ef

# Add a migration
dotnet ef migrations add InitialCreate --project computer_project.ApiService

# Apply to SmartBite.db
dotnet ef database update --project computer_project.ApiService
```

---

## Quick Reference

| No. | Area | Primary Files | Owner |
| --- | --- | --- | --- |
| 1 | Layout & responsiveness | `MainLayout.razor`, `MainLayout.razor.css`, `app.css` | [DGJKM Madugalla](https://github.com/kaveeshajanith10-afk) |
| 2 | Theming pipeline | `MainLayout.razor`, `app.css`, `UserSession.cs` | [Sathira lakshan](https://github.com/Sathi-26) |
| 3 | Navigation & routing | `NavMenu.razor`, `NavMenu.razor.css`, `app.css` | [Rhls.dayananda]((https://github.com/Lalindu01)) |
| 4 | Session state | `UserSession.cs`, `MainLayout.razor` | [KGSN Bandara](https://github.com/sahannirmal1511) |
| 5 | Auth flows | `Login.razor`, `Register.razor`, `Settings.razor`, `ConsumerOnly.razor`| [Athukoralage Pabasara](https://github.com/MashiAshi) |
| 6 | Header actions | `MainLayout.razor` (quick actions), `SmartBiteApiClient.cs` | [Abekon Abekon](https://github.com/induwarasandeepa2006) |
| 7 | Profile chip | `MainLayout.razor` (profile section), `UserSession.cs` | [D.M.Nisansala Niroshani](https://github.com/NisansalaDMN) |
| 8 | Feedback / contact | `Feedback.razor`, `SmartBiteApiClient.cs`, `Program.cs`, `Models.cs` | [BSB ABEYSOORIYA](https://github.com/sithiraabey) |
| 9 | API client & database | `SmartBiteApiClient.cs`, `Models.cs`, `Program.cs`, `AppDbContext.cs` | [Sachitha Rathnayaka](https://github.com/ZeroTrace0245) |
| 10 | Database design & SQL | `AppDbContext.cs`, `Models.cs`, `Program.cs` (SQLite config + seed data) | [AMGG ADHIKARI](https://github.com/gihangimnath2003-glitch) |
