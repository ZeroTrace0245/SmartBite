# SmartBite

SmartBite is a Blazor Server health and fitness companion built on .NET 11.

---
To run locally

> **.NET 10** or **.NET 11 Preview SDK** _(Earlier SDK versions are not supported)_
> **Docker** must be installed

### ℹ️ Notes
- If you encounter a migration error, it is **not critical** and can be safely ignored.
---

> To test out the mobile version (Android only) please visit to the [Release Page](https://github.com/ZeroTrace0245/SmartBite/releases/tag/v0.1.3-alpha)

> To test out the web application please visit to the [web sites](https://webfrontend.victoriousbush-9c923eb9.southeastasia.azurecontainerapps.io)

It combines nutrition tracking, hydration, goals, shopping flow, pathway-based dashboards, and AI-assisted coaching in a single app.

## ✨ Current Highlights 🚀

- Blazor interactive server UI (`computer_project.Web`)
- Minimal API backend (`computer_project.ApiService`)
- EF Core data layer with PostgreSQL support via Aspire (`smartbitedb`)
- Redis distributed cache integration
- AI-powered chat, nutrition estimation, recommendations, and report summaries
- Role-aware experience (`Admin` and `EndUser`)

## 🧩 Features 🎯

### Core health tracking
- Meal logging and macro tracking (`/meals`)
- Water intake tracking + AI hydration advice (`/water`)
- Health report generation and performance summary (`/reports`, `/dashboard`)
- User goals for calories/macros/hydration (`/goals/{userId}` API)

### Pathway-based experience
- **Diet pathway:** calorie intake, protein tracking, weight analytics, nutrient score
- **Gym pathway:** workout volume, calories burned, supplements, recovery
- **General pathway:** shared dashboard and essential tools

### Lifestyle and utility pages
- Sleep tracking (`/sleep`)
- Step tracking (`/steps`)
- Shopping list and checkout flow (`/shoppinglist`)
- Third-party payment simulation (`/payment/thirdparty`)
- Feedback, help/support, community, policies, labs

### AI capabilities
- AI Coach chat (`/ai`)
- Meal nutrition estimation (`/ai/estimate`)
- AI recommendations (`/recommendations`)
- AI-generated health summaries (`/stats`)
- AI health status endpoint (`/ai/status`)

## 🏗️ Architecture

### Projects
- `computer_project.Web` - Blazor Server frontend
- `computer_project.ApiService` - ASP.NET Core Minimal API backend
- `computer_project.ServiceDefaults` - shared Aspire defaults (resilience, telemetry, discovery)
- `computer_project.AppHost` - Aspire host project

### Data model (main entities)
- `User`
- `Meal`
- `WaterIntake`
- `ShoppingListItem`
- `UserGoal`
- `HealthReport`
- `AIRecommendation`

## 🛠️ Technology Stack

| Layer | Technology |
| --- | --- |
| Frontend | Blazor Server (.NET 11) |
| Backend | ASP.NET Core Minimal APIs |
| Data Access | Entity Framework Core |
| Database | PostgreSQL via Aspire (`smartbitedb`) |
| Caching | Redis distributed cache |
| Observability | OpenTelemetry (ServiceDefaults) |
| AI | Azure AI / GitHub Models integrations |
| Orchestration | .NET Aspire AppHost |

## 🚀 Getting Started (Development)

### Prerequisites
- .NET 11 SDK
- Visual Studio 2026 (or latest compatible)

### Setup
1. Clone the repository
2. Restore dependencies:
   - `dotnet restore`
3. Run from Visual Studio using the solution startup configuration.

> Note: The web project calls the API through service discovery (`https+http://apiservice`). Keep the full multi-project setup running so discovery and shared infrastructure are available.

## 🌐 Hosted Web Demo (Azure)

You can test the hosted app here:

- 🔗 https://webfrontend.victoriousbush-9c923eb9.southeastasia.azurecontainerapps.io/

> ⚠️ Availability note: this demo may be temporarily offline because it is hosted using Azure student benefits linked to a university account.

## 🔌 API Surface (summary)

### Users
- `GET /users`
- `GET /users/{id}`
- `POST /users`
- `POST /login`
- `PUT /users/{id}`
- `DELETE /users/{id}`

### Meals
- `GET /meals`
- `POST /meals`

### Shopping
- `GET /shoppinglist`
- `POST /shoppinglist`
- `PUT /shoppinglist/{id}`
- `DELETE /shoppinglist/{id}`

### Reports, goals, hydration
- `GET /stats`
- `GET /goals/{userId}`
- `PUT /goals/{userId}`
- `GET /water`
- `POST /water`
- `GET /water/advice`

### AI
- `POST /ai/chat`
- `GET /ai/estimate`
- `GET /ai/status`
- `GET /recommendations`

## 📸 Screenshots

### Web app (Old vs New)

| Area | Old | New |
| --- | --- | --- |
| Home | ![Old Home](<screenshots/Home page.png>) | ![New Home](<screenshots/Web app NEW/New homepage.png>) |
| Dashboard | ![Old Dashboard](<https://github.com/ZeroTrace0245/SmartBite/blob/660ad95083b145e4f9bcc78db00bc43bad0b8c43/screenshots/Dashboard.gif>) | ![New Dashboard](<screenshots/Web app NEW/New pathway pesonalized Dashboard (genaral users).png>) |
| Hydration | ![Old Hydration](<screenshots/Hydration.png>) | ![New Hydration](<screenshots/Web app NEW/New Hydration with AI.png>) |
| Performance | ![Old Performance](<screenshots/Performance.png>) | ![New Performance](<screenshots/Web app NEW/New Performance.png>) |
| Labs | ![Old Labs](<screenshots/Labs.png>) | ![New Labs](<screenshots/Web app NEW/New labs.png>) |

### 🧭 Pathway Features Showcase (Web)

#### 🥗 Diet Pathway

| Feature | Screenshot |
| --- | --- |
| Personalized Diet Dashboard | ![Diet dashboard](<screenshots/Web app NEW/New pathway pesonalized Dashboard (diet).png>) |
| Calorie Intake | ![Diet calorie intake](<screenshots/Web app NEW/diet Pathway specific features caloie intack.png>) |
| Protein Tracking | ![Diet protein tracking](<screenshots/Web app NEW/diet Pathway specific features Protein Tracking.png>) |
| Weight Analytics | ![Diet weight analytics](<screenshots/Web app NEW/diet Pathway specific features Weight Analytics.png>) |
| Nutrient Score | ![Diet nutrient score](<screenshots/Web app NEW/diet Pathway specific features Nutrient Score.png>) |

#### 🏋️ Gym Pathway

| Feature | Screenshot |
| --- | --- |
| Personalized Gym Dashboard | ![Gym dashboard](<screenshots/Web app NEW/New pathway pesonalized Dashboard (gym).png>) |
| Calories Burned | ![Gym calories burned](<screenshots/Web app NEW/GYM Pathway specific features calories burned.png>) |
| Recovery Status | ![Gym recovery status](<screenshots/Web app NEW/GYM Pathway specific features Recovery status.png>) |
| Supplement Stack | ![Gym supplements](<screenshots/Web app NEW/GYM Pathway specific features Supplement Stack.png>) |

#### 🌿 General Pathway

| Feature | Screenshot |
| --- | --- |
| Personalized General Dashboard | ![General dashboard](<screenshots/Web app NEW/New pathway pesonalized Dashboard (genaral users).png>) |
| General Pathway Features | ![General pathway features](<screenshots/Web app NEW/Pathway specific features (genaral).png>) |
| Mood Log | ![General mood log](<screenshots/Web app NEW/Pathway genaral specific features Mood log.png>) |
| Pathway Selection | ![Pathway selection](<screenshots/Web app NEW/New pathway selection.png>) |

### 🧭 Pathway Features Showcase (Mobile)

#### 🥗 Diet Pathway (Mobile)

| Feature | Screenshot |
| --- | --- |
| Diet Dashboard | ![Mobile diet dashboard](<screenshots/Mobile app/Mobile Pathway Dashboard (Diet) .png>) |
| Diet Dashboard (Alt) | ![Mobile diet dashboard alt](<screenshots/Mobile app/Mobile Pathway Dashboard (Diet) 2.png>) |
| Meals Tracking | ![Mobile meals](<screenshots/Mobile app/Mobile meals.png>) |

#### 🏋️ Gym Pathway (Mobile)

| Feature | Screenshot |
| --- | --- |
| Gym Dashboard | ![Mobile gym dashboard](<screenshots/Mobile app/Mobile Pathway Dashboard (gym) .png>) |
| Gym Dashboard (Alt) | ![Mobile gym dashboard alt](<screenshots/Mobile app/Mobile Pathway Dashboard (gym) 2.png>) |
| Step Tracking | ![Mobile steps](<screenshots/Mobile app/Mobile step Tracker 2.png>) |

#### 🌿 General Pathway (Mobile)

| Feature | Screenshot |
| --- | --- |
| General Dashboard | ![Mobile general dashboard](<screenshots/Mobile app/Mobile Pathway Dashboard (general) .png>) |
| General Dashboard (Alt) | ![Mobile general dashboard alt](<screenshots/Mobile app/Mobile Pathway Dashboard (general) 2.png>) |
| Mood/Utility View | ![Mobile settings pathway theme](<screenshots/Mobile app/Mobile change the pathway theme only.png>) |
| Pathway Selection | ![Mobile pathway selection](<screenshots/Mobile app/Mobile Pathway selection.png>) |

### 🤖 AI Features Showcase

#### 🌐 Web AI Features

| AI Feature | Screenshot |
| --- | --- |
| AI Coach | ![Web AI coach](<screenshots/Web app NEW/New AI coach.png>) |
| AI Dashboard Insights | ![Web AI dashboard](<screenshots/Web app NEW/New AI feature for the dashboard.png>) |
| AI Meal Assist | ![Web AI meal](<screenshots/Web app NEW/AI features for the Log meal.png>) |
| AI Grocery Assist | ![Web AI groceries](<screenshots/Web app NEW/AI features for groceries.png>) |
| AI Grocery Autofill | ![Web AI grocery autofill](<screenshots/Web app NEW/auto fill groceries with AI.png>) |
| AI Hydration Assist | ![Web AI hydration](<screenshots/Web app NEW/New Hydration AI feature.png>) |
| AI Performance Insight | ![Web AI performance](<screenshots/Web app NEW/AI insight for performance.png>) |
| AI Help & Support | ![Web AI help](<screenshots/Web app NEW/Help and support with AI.png>) |

#### 📱 Mobile AI Features

| AI Feature | Screenshot |
| --- | --- |
| Mobile AI Coach | ![Mobile AI coach](<screenshots/Mobile app/Mobile AI Coach .png>) |

### Mobile app screenshots

| Screen | Preview |
| --- | --- |
| Startup | ![Mobile startup](<screenshots/Mobile app/Mobile startup screen.png>) |
| Account creation | ![Mobile account creation](<screenshots/Mobile app/Mobile account creation.png>) |
| Navigation bar | ![Mobile nav bar](<screenshots/Mobile app/Mobile nav bar.png>) |
| Pathway selection | ![Mobile pathway selection](<screenshots/Mobile app/Mobile Pathway selection.png>) |
| General dashboard | ![Mobile general dashboard](<screenshots/Mobile app/Mobile Pathway Dashboard (general) .png>) |
| General dashboard (Part 2) | ![Mobile general dashboard 2](<screenshots/Mobile app/Mobile Pathway Dashboard (general) 2.png>) |
| Gym dashboard | ![Mobile gym dashboard](<screenshots/Mobile app/Mobile Pathway Dashboard (gym) .png>) |
| Gym dashboard (Part 2) | ![Mobile gym dashboard 2](<screenshots/Mobile app/Mobile Pathway Dashboard (gym) 2.png>) |
| Diet dashboard | ![Mobile diet dashboard](<screenshots/Mobile app/Mobile Pathway Dashboard (Diet) .png>) |
| Diet dashboard (Part 2) | ![Mobile diet dashboard 2](<screenshots/Mobile app/Mobile Pathway Dashboard (Diet) 2.png>) |
| AI Coach | ![Mobile AI coach](<screenshots/Mobile app/Mobile AI Coach .png>) |
| Meals | ![Mobile meals](<screenshots/Mobile app/Mobile meals.png>) |
| Shopping | ![Mobile shopping](<screenshots/Mobile app/Mobile Shopping list.png>) |
| Shopping (Part 2) | ![Mobile shopping 2](<screenshots/Mobile app/Mobile Shopping list 2.png>) |
| Shopping (Part 3) | ![Mobile shopping 3](<screenshots/Mobile app/Mobile Shopping list 3.png>) |
| Performance | ![Mobile performance](<screenshots/Mobile app/Mobile performance.png>) |
| Sleep analysis | ![Mobile sleep analysis](<screenshots/Mobile app/Mobile sleep Analysis.png>) |
| Step tracker | ![Mobile steps](<screenshots/Mobile app/Mobile step Tracker.png>) |
| Step tracker (Part 2) | ![Mobile steps 2](<screenshots/Mobile app/Mobile step Tracker 2.png>) |
| Water tracker | ![Mobile water](<screenshots/Mobile app/Mobile water Tracker.png>) |
| Settings | ![Mobile settings](<screenshots/Mobile app/Mobile settings.png>) |
| Settings (Part 2) | ![Mobile settings 2](<screenshots/Mobile app/Mobile settings 2.png>) |
| Settings (Part 3) | ![Mobile settings 3](<screenshots/Mobile app/Mobile settings 3.png>) |
| Pathway theme switch | ![Mobile pathway theme switch](<screenshots/Mobile app/Mobile change the pathway theme only.png>) |

## 🤖 Mobile AI Feature Setup (Token)

For mobile AI features, configure **your own GitHub token** locally.

- ✅ Use your personal token in local secrets/environment settings
- ❌ Do not commit tokens to source control
- 🔐 Do not share tokens in chat, issues, or screenshots

Example secret key used by this solution:

- `Parameters:chatModel-Token`

## 📁 Project File Structure

```text
.
├─ computer_project.Web/
│  ├─ Components/
│  │  ├─ Layout/
│  │  └─ Pages/
│  ├─ Services/
│  ├─ Program.cs
│  ├─ SmartBiteApiClient.cs
│  └─ Models.cs
├─ computer_project.ApiService/
│  ├─ Data/
│  │  └─ AppDbContext.cs
│  ├─ Services/
│  │  └─ AIService.cs
│  ├─ Program.cs
│  └─ Models.cs
├─ computer_project.ServiceDefaults/
│  └─ Extensions.cs
├─ computer_project.AppHost/
│  └─ (Aspire host project files)
├─ screenshots/
├─ docs/
└─ README.md
```

## ⚠️ Current Known Limitations

- Demo-style authentication (passwords are not hashed yet)
- Most demo flows are centered around seeded/default data
- No dedicated automated test project yet
- Some integrations (wearable polling / advanced labs) are scaffolded or simulated

## 🗂️ Repository Structure

```text
.
├─ computer_project.Web/
├─ computer_project.ApiService/
├─ computer_project.ServiceDefaults/
├─ computer_project.AppHost/
├─ docs/
├─ screenshots/
└─ README.md
```

## 🤝 Contributing

Please follow [CONTRIBUTING.md](CONTRIBUTING.md).

## 📄 License

MIT - see [LICENSE.md](LICENSE.md).
