# Contributing to SmartBit

> Contribution guide for the PUSL2021 Computing Group Project and open-source contributors.

> ⚠️ **Requires .NET 10 or .NET 11 Preview SDK.** Earlier SDK versions are not supported.

---

## Getting Started

### Prerequisites

#### Required Software
- .NET 10 SDK or .NET 11 Preview SDK
- Visual Studio 2026 Insiders (recommended) or VS Code
- Git

#### Optional (for installer development)
- WiX Toolset v3 (for MSI creation)
- Inno Setup (alternative installer)
- PowerShell 5.1 or later

### Installing .NET SDK

To install the .NET 11 SDK using Windows Package Manager (winget):

```sh
winget install Microsoft.DotNet.SDK.Preview
winget install Microsoft.DotNet.SDK.10
```

Or download manually from [dotnet.microsoft.com](https://dotnet.microsoft.com/download/dotnet/11.0)

### Development Setup

1. Clone the repo and switch to a feature branch:

```sh
git clone https://github.com/ZeroTrace0245/SmartBit.git
cd SmartBit
git checkout -b feature/<your-area>
```

2. Restore and run:

```sh
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

### Member 9 — API Client & Database · Working with AI Locally (Sachitha Rathnayaka)
**Goal**: Implement and maintain AI features using **AI Foundry Local** — all AI processing runs entirely on-device with no cloud APIs or API keys required.

| File | Purpose |
| --- | --- |
| `computer_project.ApiService/Services/AIService.cs` | Backend AI service — chat completions, nutrition estimation, recommendations, report summaries, hydration tips via AI Foundry Local |
| `computer_project.Web/Components/Pages/AICoach.razor` | AI Health Coach UI — chat interface, connection status indicator, setup instructions |
| `computer_project.Web/SmartBiteApiClient.cs` | HTTP client methods: `IsAIOnlineAsync()`, `GetAICoachAdviceAsync()`, etc. |
| `computer_project.ApiService/Program.cs` | AI-related API endpoints (`/ai/chat`, `/ai/status`, `/ai/nutrition`, `/ai/recommendations`) |
| `computer_project.ApiService/appsettings.json` | `LocalAI` configuration section (`Endpoint`, `Model`) |
| `computer_project.ApiService/Data/AppDbContext.cs` | EF Core DbContext — `AIRecommendations` DbSet |
| `computer_project.ApiService/Models.cs` | `AIRecommendation` entity model |

#### What is AI Foundry Local?

[AI Foundry Local](https://github.com/microsoft/ai-foundry-local) is a Microsoft tool that lets you download and run AI models directly on your machine. SmartBite uses it to power all AI features without sending any data to cloud services.

- **Model used**: `phi-3.5-mini` — a compact, fast language model suitable for nutrition advice and meal estimation.
- **Protocol**: AI Foundry Local exposes an OpenAI-compatible `/v1/chat/completions` endpoint on `localhost`. `AIService.cs` sends standard chat-completion requests to this local server.
- **No API keys needed**: everything stays on-device.

#### How it works in SmartBite

1. **`AIService.cs`** connects to AI Foundry Local at the configured endpoint (default `http://localhost:5272`) and sends chat-completion requests with a nutrition-focused system prompt.
2. **Five AI capabilities** are exposed:
   - `GetChatResponseAsync()` — general nutrition/health chat
   - `EstimateNutritionAsync()` — parses a meal description into calories, protein, carbs, fat (JSON)
   - `GetRecommendationsAsync()` — suggests meals based on recent logs and user goals
   - `GenerateHealthReportAsync()` — produces a 2–3 sentence daily health assessment
   - `GetWaterAdviceAsync()` — motivational hydration tips
3. **`AICoach.razor`** provides the user-facing chat UI with a live connection status badge (green = connected, red = offline) and inline setup instructions.

#### Setup

```sh
# Install AI Foundry Local (one-time)
winget install Microsoft.AIFoundryLocal

# Download the model
foundry model download phi-3.5-mini

# Start the local server (must be running before using AI features)
foundry service start
```

> ⚠️ The port changes every time Foundry starts. Check the output line `Service is Started on http://127.0.0.1:XXXXX/` and update `computer_project.ApiService/appsettings.json` → `"Endpoint"` if the port differs.

**Tasks**:
- Continue tuning AI prompts for better nutrition estimates.
- Add conversation history support for multi-turn coaching.
- Integrate AI recommendations into the Dashboard.
- Add fallback messages when the model is unavailable.

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

```sh
# Install EF tools (once)
dotnet tool install --global dotnet-ef

# Add a migration
dotnet ef migrations add InitialCreate --project computer_project.ApiService

# Apply to SmartBite.db
dotnet ef database update --project computer_project.ApiService
```

---

## Commit History

| Hash | Message | Author | Date |
| --- | --- | --- | --- |
| a478bd8 | remove the API key | Zero_Trace | 2026-02-07 |
| 38d6684 | Add project files. | Zero_Trace | 2026-02-07 |
| 8eff6e6 | Add .gitattributes and .gitignore. | Zero_Trace | 2026-02-07 |

---

## Quick Reference

| No. | Area | Primary Files | Owner |
| --- | --- | --- | --- |
| 1 | Layout & responsiveness | `MainLayout.razor`, `MainLayout.razor.css`, `app.css` | [DGJKM Madugalla](https://github.com/kaveeshajanith10-afk) |
| 2 | Theming pipeline | `MainLayout.razor`, `app.css`, `UserSession.cs` | [Sathira lakshan](https://github.com/Sathi-26) |
| 3 | Navigation & routing | `NavMenu.razor`, `NavMenu.razor.css`, `app.css` | [Rhls.dayananda](https://github.com/Lalindu01) |
| 4 | Session state | `UserSession.cs`, `MainLayout.razor` | [KGSN Bandara](https://github.com/sahannirmal1511) |
| 5 | Auth flows | `Login.razor`, `Register.razor`, `Settings.razor`, `ConsumerOnly.razor` | [Athukoralage Pabasara](https://github.com/MashiAshi) |
| 6 | Header actions | `MainLayout.razor` (quick actions), `SmartBiteApiClient.cs` | [Abekon Abekon](https://github.com/induwarasandeepa2006) |
| 7 | Profile chip | `MainLayout.razor` (profile section), `UserSession.cs` | [D.M.Nisansala Niroshani](https://github.com/NisansalaDMN) |
| 8 | Feedback / contact | `Feedback.razor`, `SmartBiteApiClient.cs`, `Program.cs`, `Models.cs` | [BSB ABEYSOORIYA](https://github.com/sithiraabey) |
| 9 | API client & database · AI locally | `SmartBiteApiClient.cs`, `Models.cs`, `Program.cs`, `AppDbContext.cs`, `AIService.cs`, `AICoach.razor` | [Sachitha Rathnayaka](https://github.com/ZeroTrace0245) |
| 10 | Database design & SQL | `AppDbContext.cs`, `Models.cs`, `Program.cs` (SQLite config + seed data) | [AMGG ADHIKARI](https://github.com/gihangimnath2003-glitch) |
| 11 | Installer & Packaging | `Build-Installer.ps1`, `MSI-Installer/`, WiX source files | [Sachitha Rathnayaka](https://github.com/ZeroTrace0245) |
| 12 | Payment Integration | `PaymentModal.razor`, `ThirdPartyPayment.razor`, `UserSession.cs` (payment preferences) | [Sachitha Rathnayaka](https://github.com/ZeroTrace0245) |

---

### Installer & Packaging Development
**Goal**: Maintain and improve the professional installation packages for SmartBit.

| File | Purpose |
| --- | --- |
| `Build-Installer.ps1` | Main build script for portable ZIP package |
| `MSI-Installer/SmartBit.wxs` | WiX source file for MSI creation |
| `Create-MSI-Installer.ps1` | Automated MSI build script |
| `Create-InnoSetup-Installer.ps1` | Alternative Inno Setup installer script |
| `Create-SFX-Installer.ps1` | Self-extracting archive creator |
| `MSI-INSTALLER-COMPLETE.md` | Installation and deployment guide |
| `MSI-INSTALLER-GUIDE.md` | Installer creation guide |

#### Prerequisites for Installer Development
```powershell
# Install WiX Toolset v3
winget install WiXToolset.WiXToolset

# Optional: Install Inno Setup
winget install JRSoftware.InnoSetup
```

#### Building Installers
```powershell
# Step 1: Build application package
.\Build-Installer.ps1

# Step 2: Compile MSI
& "C:\Program Files (x86)\WiX Toolset v3.14\bin\candle.exe" `
    ".\MSI-Installer\SmartBit.wxs" `
    -out ".\MSI-Installer\SmartBit.wixobj" `
    -ext WixUIExtension -arch x64

# Step 3: Link to MSI
& "C:\Program Files (x86)\WiX Toolset v3.14\bin\light.exe" `
    ".\MSI-Installer\SmartBit.wixobj" `
    -out ".\MSI-Installer\Output\SmartBit-v1.0-Setup.msi" `
    -ext WixUIExtension -cultures:en-us -sval
```

#### Installer Development Tasks
- [ ] Update version numbers in WiX source when releasing
- [ ] Test silent installation on clean VMs
- [ ] Verify uninstaller removes all files
- [ ] Test upgrade scenarios (old version → new version)
- [ ] Optimize MSI package size
- [ ] Add digital signature support
- [ ] Create installer localization for multiple languages

### Payment Integration Development
**Goal**: Maintain and expand payment method integration features.

| File | Purpose |
| --- | --- |
| `computer_project.Web/Components/Pages/PaymentModal.razor` | Payment method selection UI component |
| `computer_project.Web/Components/Pages/ThirdPartyPayment.razor` | PayPal and Stripe integration simulation |
| `computer_project.Web/Components/Pages/Settings.razor` | Payment preferences configuration |
| `computer_project.Web/Services/UserSession.cs` | PreferredPaymentMethod property |
| `PAYMENT_ENHANCEMENT_GUIDE.md` | Payment feature documentation |

#### Payment Development Tasks
- [ ] Implement real PayPal SDK integration
- [ ] Implement real Stripe Checkout integration
- [ ] Add payment history tracking
- [ ] Add receipt generation
- [ ] Add refund/cancellation support
- [ ] Improve payment method validation
- [ ] Add cryptocurrency payment options

---

## Coding Standards

### C# Style Guidelines
- Follow [Microsoft C# Coding Conventions](https://docs.microsoft.com/en-us/dotnet/csharp/fundamentals/coding-style/coding-conventions)
- Use meaningful variable names
- Add XML documentation comments for public APIs
- Keep methods focused and under 50 lines when possible

### Razor Component Guidelines
- Use `@code` blocks for component logic
- Keep presentation logic in the `.razor` file
- Move complex business logic to services
- Use dependency injection for services
- Implement proper disposal for `IDisposable` components

### PowerShell Script Guidelines
- Use approved verbs (Get, Set, New, Remove, etc.)
- Include parameter validation
- Add help comments at script start
- Use Write-Host with colors for user feedback
- Test scripts on clean PowerShell 5.1 and 7+

### Git Commit Messages
- Use present tense ("Add feature" not "Added feature")
- First line: brief summary (50 chars or less)
- Blank line, then detailed description if needed
- Reference issue numbers: `Fixes #123`

Examples:
```
Add MSI installer build script

- Create WiX source file for SmartBit
- Add automated build pipeline
- Include silent installation support

Fixes #45
```

---

## Testing Guidelines

### Manual Testing Checklist
Before submitting a PR, test:
- [ ] Application builds without errors
- [ ] Application runs successfully
- [ ] UI renders correctly in both light and dark themes
- [ ] Responsive layout works on mobile/tablet viewports
- [ ] All navigation links work
- [ ] Database operations complete successfully
- [ ] No console errors in browser dev tools

### Installer Testing Checklist
- [ ] MSI installs successfully
- [ ] Application launches from Start Menu shortcut
- [ ] Application launches from Desktop shortcut
- [ ] Application runs correctly when installed
- [ ] Uninstaller removes all files
- [ ] Silent installation works (`/quiet` flag)
- [ ] Installation log shows no errors

---

## Pull Request Process

1. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Changes**
   - Follow coding standards
   - Test thoroughly
   - Update documentation

3. **Commit Changes**
   ```bash
   git add .
   git commit -m "Add your feature description"
   ```

4. **Push to GitHub**
   ```bash
   git push origin feature/your-feature-name
   ```

5. **Create Pull Request**
   - Use descriptive title
   - Describe what changed and why
   - Link related issues
   - Request review from team members

6. **Code Review**
   - Address reviewer comments
   - Make requested changes
   - Re-request review when ready

7. **Merge**
   - Squash commits if needed
   - Delete branch after merge

---

## Documentation Standards

### When to Update Documentation

Update docs when you:
- Add a new feature
- Change existing behavior
- Add/remove dependencies
- Modify installation process
- Change build procedures

### Documentation Files to Update

| File | When to Update |
| --- | --- |
| `README.md` | Major features, system requirements, getting started |
| `CONTRIBUTING.md` | Contribution process, team member responsibilities |
| `MSI-INSTALLER-COMPLETE.md` | Installation instructions, deployment options |
| `MSI-INSTALLER-GUIDE.md` | Installer build process |
| `PAYMENT_ENHANCEMENT_GUIDE.md` | Payment features, integration steps |

---

## Getting Help

### Resources
- **Project Documentation**: Check `.md` files in repository root
- **GitHub Issues**: [SmartBit Issues](https://github.com/ZeroTrace0245/SmartBit/issues)
- **GitHub Discussions**: [SmartBit Discussions](https://github.com/ZeroTrace0245/SmartBit/discussions)
- **.NET Documentation**: [docs.microsoft.com/dotnet](https://docs.microsoft.com/dotnet)
- **Blazor Documentation**: [docs.microsoft.com/blazor](https://docs.microsoft.com/blazor)
- **WiX Toolset**: [wixtoolset.org](https://wixtoolset.org)

### Team Communication
- Use GitHub Issues for bug reports and feature requests
- Use GitHub Discussions for questions and general discussion
- Tag team members in comments using `@username`

---

## License

By contributing to SmartBit, you agree that your contributions will be licensed under the MIT License.

---

**Thank you for contributing to SmartBit!** 🎉
