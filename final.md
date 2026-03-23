# SmartBit Final Report

## Front Cover Page

**Project Title:** SmartBit – Intelligent Cross-Platform Health Tracking System  
**Module:** PUSL2021 Computing Group Project  
**Institution:** *(Update with university name)*  
**Academic Year:** 2025/2026  
**Supervisor:** *(Update supervisor name)*  
**Team:** *(Update group name/number and student IDs)*  
**Submission Date:** *(Update date)*

---

## Acknowledgements

Gratitude is expressed to the project supervisor for guidance throughout planning, implementation, and review stages. Appreciation is also extended to the development team members for collaborative design, coding, testing, and documentation work. Additional thanks are given to peer reviewers and end users who provided practical feedback during demonstration sessions.

---

## Abstract

SmartBit is a health companion system developed as a modern cross-platform solution to support nutrition tracking, hydration monitoring, shopping list management, and AI-assisted wellness guidance. The project includes a **Blazor web application** (targeting .NET 11) and a complementary **mobile solution** documented in `mobile final.md`.  

The web platform was implemented using Blazor interactive server components, ASP.NET Core Minimal APIs, Aspire service defaults, PostgreSQL integration, Redis distributed caching, and role-aware session management. The user experience was designed with responsive layouts, dashboard summaries, reporting views, and settings management, while the AI features were integrated through **GitHub Models** for intelligent health insights. The interface uses extensive **SVG-based iconography** for a consistent visual language across modules.

Deployment was completed on **Microsoft Azure hosting** (Azure Container Apps endpoint):  
`https://webfrontend--0000006.victoriousbush-9c923eb9.southeastasia.azurecontainerapps.io/`

The project outcome demonstrates technical feasibility, modular architecture, and practical usability for both end users and administrators. The completed system provides a foundation for future improvements, including stronger authentication, expanded analytics, and enhanced mobile/web synchronization.

---

## Table of Contents

- [List of Figures and Tables](#list-of-figures-and-tables)
- [Main Body of Report](#main-body-of-report)
  - [Chapter 01 – Introduction](#chapter-01--introduction)
  - [Chapter 02 – Literature Review](#chapter-02--literature-review)
  - [Chapter 03 – System Analysis](#chapter-03--system-analysis)
  - [Chapter 04 – Requirements Specification](#chapter-04--requirements-specification)
  - [Chapter 05 – System Architecture](#chapter-05--system-architecture)
  - [Chapter 06 – Method of Approach / Methodology](#chapter-06--method-of-approach--methodology)
  - [Chapter 07 – System Testing and Quality Assurance](#chapter-07--system-testing-and-quality-assurance)
  - [Conclusions](#conclusions)
  - [Team Plan & Responsibility Matrix](#team-plan--responsibility-matrix)
- [Reference List](#reference-list)
- [Appendices](#appendices)

---

## List of Figures and Tables

### Figures
- Figure 1: SmartBit Web Landing and Navigation
- Figure 2: Web Dashboard and Core Tracking Interfaces
- Figure 3: AI Coach and Labs Views
- Figure 4: Mobile Application Screens (from `mobile final.md` deliverable)
- Figure 5: High-Level System Architecture

### Tables
- Table 1: Functional Requirements
- Table 2: Non-Functional Requirements
- Table 3: Technology Stack
- Table 4: Test Summary
- Table 5–8: Team Responsibility Matrices (Per Member)
- Table 9: GitHub Commit History (Placeholder)

---

# Main Body of Report

## Chapter 01 – Introduction

### 1.1 Background
Health management applications are often fragmented across different products for meal logging, hydration tracking, and planning activities. This fragmentation can reduce consistency, limit data continuity, and create user fatigue. SmartBit was developed to unify these flows in a single ecosystem, with web-first delivery using Blazor and support for a mobile experience.

### 1.2 Problem Statement
Existing free and lightweight solutions typically do not combine:
- structured meal and macro tracking,
- hydration logging,
- grocery planning,
- role-aware administration,
- and integrated AI coaching,
in one coherent platform.

### 1.3 Aims and Objectives
The project aimed to deliver:
1. A complete Blazor-based web platform on .NET 11.
2. A backend API with persistent storage and scalable service wiring.
3. A role-aware user flow for admins and end users.
4. AI-powered guidance using GitHub Models.
5. Azure cloud hosting for production-style deployment.
6. A report-ready documentation package aligned with final submission structure.

### 1.4 Scope
**In Scope**
- Dashboard, meal logs, hydration, shopping list, reports, AI coach, settings, and support pages.
- API endpoints for users, meals, shopping, water, goals, and AI operations.
- Azure deployment and link-based access.

**Out of Scope (Current Version)**
- enterprise authentication/identity provider integration,
- complete automated test suite,
- finalized production-grade security hardening.

---

## Chapter 02 – Literature Review

### 2.1 Overview of Related Systems
Industry tools such as calorie trackers and wellness apps commonly offer partial solutions focused on one domain only. Academic and open-source systems often prioritize architecture demonstrations but may omit practical UX consistency and deployment maturity.

### 2.2 Technology Trends Relevant to the Project
- **Component-based web frameworks** (e.g., Blazor) improve maintainability and UI reuse.
- **Cloud-native deployment** patterns improve accessibility and demonstration readiness.
- **Embedded AI assistants** improve user engagement when responses are contextual.
- **Cross-platform health tracking** increasingly requires both web and mobile presence.

### 2.3 Rationale for SmartBit Approach
SmartBit combines:
- a maintainable .NET-first stack,
- clear API boundaries,
- modern web UI with SVG iconography,
- AI-assisted interactions through GitHub Models,
- and deployment on Azure hosting.

This combination supports both academic evaluation and practical extension.

---

## Chapter 03 – System Analysis

### 3.1 Existing Process Issues
- Manual tracking is inconsistent and error-prone.
- Users switch between multiple apps for related tasks.
- Lack of unified reporting reduces decision quality.
- Administrative controls are limited in many consumer tools.

### 3.2 Proposed Solution
SmartBit unifies health workflows under one application suite:
- **Web:** primary experience with full dashboard and management tools.
- **Mobile:** companion workflow described in `mobile final.md` for on-the-go usage.
- **API Layer:** shared logic and data services.

### 3.3 Stakeholders
- End users (daily health tracking)
- Admin users (management and configuration)
- Project team (implementation and operations)
- Academic supervisor and assessors (evaluation and validation)

### 3.4 Feasibility Summary
- **Technical feasibility:** High (modern .NET stack, proven services).
- **Operational feasibility:** High (clear UI and role flow).
- **Economic feasibility:** Good (open tooling, scalable hosting model).

---

## Chapter 04 – Requirements Specification

### 4.1 Functional Requirements

| ID | Requirement | Priority |
|---|---|---|
| FR-01 | Register and authenticate users | High |
| FR-02 | Log meals with nutrition values | High |
| FR-03 | Track water intake over time | High |
| FR-04 | Manage shopping list items and purchase status | High |
| FR-05 | Generate dashboard summaries and report views | High |
| FR-06 | Provide AI coach interactions for wellness advice | Medium |
| FR-07 | Manage user goals and preferences | Medium |
| FR-08 | Support admin-only management actions | High |

### 4.2 Non-Functional Requirements

| ID | Requirement | Target |
|---|---|---|
| NFR-01 | Responsive web UI | Desktop and mobile-friendly layouts |
| NFR-02 | Maintainability | Modular components and services |
| NFR-03 | Availability | Stable cloud deployment on Azure |
| NFR-04 | Performance | Acceptable response times for core actions |
| NFR-05 | Usability | Intuitive navigation with consistent icon language |
| NFR-06 | Extensibility | API and service layers suitable for future features |

### 4.3 Constraints and Assumptions
- The current solution relies on backend connectivity for most features.
- Some advanced security controls remain planned enhancements.
- AI quality depends on model availability and prompt design.

---

## Chapter 05 – System Architecture

### 5.1 High-Level Architecture

1. **Web Frontend (`computer_project.Web`)**
   - Blazor interactive server components.
   - Session and state services.
   - SVG-based icon-rich UI in layout and pages.

2. **API Service (`computer_project.ApiService`)**
   - ASP.NET Core Minimal API endpoints.
   - Domain models: User, Meal, WaterIntake, ShoppingListItem, HealthReport, UserGoal, AIRecommendation.

3. **Data Layer**
   - AppDbContext with indexed entities.
   - Npgsql database context configured through Aspire.

4. **Distributed Services**
   - Redis distributed cache.
   - Service defaults through Aspire conventions.

5. **AI Integration**
   - GitHub Models chat integration for recommendations and wellness responses.
   - AI status and helper endpoints in API service.

6. **Hosting**
   - Azure-based public deployment for web frontend.

### 5.2 Technology Stack

| Layer | Technology |
|---|---|
| Frontend | Blazor (.NET 11), Razor Components, CSS |
| Backend | ASP.NET Core Minimal APIs |
| Data Access | Entity Framework Core |
| Database | PostgreSQL (Aspire-managed), indexed schema |
| Caching | Redis Distributed Cache |
| AI | GitHub Models / Azure AI client integration |
| Hosting | Azure Container Apps endpoint |
| Tooling | Visual Studio 2026, PowerShell, GitHub |

### 5.3 UI and Design Language
- Navigation and top-level interactions use reusable layout components.
- **SVG icons** are used throughout nav and feature cards to maintain visual consistency.
- Responsive and modern styling supports both wide and compact displays.

---

## Chapter 06 – Method of Approach / Methodology

### 6.1 Development Method
An iterative, feature-driven process was followed:
1. Baseline architecture setup.
2. Core domain features (meals, water, shopping).
3. Reporting and settings.
4. AI integration and enhancement.
5. Cloud deployment and validation.
6. Documentation consolidation.

### 6.2 Work Breakdown (Summary)
- Sprint 1: Foundation, service setup, data models.
- Sprint 2: Meal and water modules.
- Sprint 3: Shopping and reporting modules.
- Sprint 4: AI coach and quality refinements.
- Sprint 5: Hosting, screenshots, final report packaging.

### 6.3 Version Control and Collaboration
- Repository: `https://github.com/ZeroTrace0245/SmartBit`
- Branch workflow and commit history maintained in GitHub.
- Merge and review workflow used for stabilization.

### 6.4 Documentation Inputs Used
- Existing project README and architecture notes.
- `mobile final.md` as mobile deliverable source.
- Interim report and supporting documents in `/docs`.

---

## Chapter 07 – System Testing and Quality Assurance

### 7.1 Testing Approach
Testing combined manual validation and scenario-based checks:
- navigation and role access checks,
- API endpoint validation,
- data persistence checks,
- AI service status and response checks,
- deployment verification through live Azure endpoint.

### 7.2 Test Summary

| Area | Test Type | Outcome |
|---|---|---|
| Authentication flow | Manual functional | Pass |
| Meal logging CRUD | Manual/API | Pass |
| Water tracking | Manual/API | Pass |
| Shopping list | Manual/API | Pass |
| Reports and stats | Manual functional | Pass |
| AI coach endpoint | Manual/API | Pass (model availability dependent) |
| Role-restricted settings | Manual security | Pass |
| Azure hosted access | Smoke test | Pass |

### 7.3 Known Limitations
- Authentication hardening requires further enhancement.
- Full automated test coverage is pending.
- AI responses vary naturally by prompt and model behavior.

### 7.4 Quality Controls Applied
- Consistent component structure and naming.
- Input validation and controlled endpoint design.
- Error handling in AI and reporting workflows.
- Structured final documentation and evidence mapping.

---

## Web and Mobile Screenshot Evidence

### Web Application Screenshots (Included)

1. Startup  
![Startup](screenshots/Startup%20.png)

2. Home Page  
![Home](screenshots/Home%20page.png)

3. Dashboard  
![Dashboard](screenshots/Dashboard.gif)

4. Log History  
![Log History](screenshots/Log%20History.png)

5. Performance/Reports  
![Performance](screenshots/Performance.png)

6. Hydration  
![Hydration](screenshots/Hydration.png)

7. Groceries  
![Groceries](screenshots/Groceries.gif)

8. Settings (Admin)  
![Settings Admin](screenshots/Setting%20page%20admin%20athu.png)

9. Labs  
![Labs](screenshots/Labs.png)

10. AI/Foundry Related View  
![AI Progress](screenshots/AI%20(working%20on%20Prograess).png)

### Mobile Application Screenshots (To Include from Mobile Deliverable)

Use screenshots from the mobile project referenced in `mobile final.md`.

- Figure M1: Mobile Login / Onboarding Screen *(insert image)*
- Figure M2: Mobile Dashboard Screen *(insert image)*
- Figure M3: Mobile AI Coach Chat Screen *(insert image)*
- Figure M4: Mobile Hydration / Tracking Screen *(insert image)*
- Figure M5: Mobile Shopping / Checklist Screen *(insert image)*

> Note: Mobile architecture and UX details are captured in `mobile final.md` and should be attached in appendix order.

---

## Conclusions

SmartBit achieved its primary objectives by delivering a modern health tracking platform with integrated web functionality, AI-assisted guidance, and cloud deployment. The final implementation demonstrates strong alignment with modular architecture principles and practical user-centered design. The Azure-hosted web endpoint provides a working public deployment, and GitHub Models integration introduces intelligent coaching capability within the product flow.

The project is suitable for continued enhancement toward production readiness, with the highest priorities being authentication hardening, automated test expansion, and deeper mobile-web feature parity.

---

## Team Plan & Responsibility Matrix

### Member 01 Responsibility Matrix

| Area | Responsibility | Evidence |
|---|---|---|
| Planning | Sprint planning and milestone tracking | Meeting logs |
| Architecture | Solution structure and service boundaries | Architecture draft |
| Documentation | Final report sections 1–3 | Report history |

### Member 02 Responsibility Matrix

| Area | Responsibility | Evidence |
|---|---|---|
| Backend | API endpoints and model updates | Source commits |
| Data | DbContext and persistence support | Code review logs |
| QA | API-level test execution | Test notes |

### Member 03 Responsibility Matrix

| Area | Responsibility | Evidence |
|---|---|---|
| Frontend | Blazor pages and layout implementation | UI commits |
| UX | Navigation, styling, icon consistency (SVG) | Screenshot set |
| QA | UI regression checks | Test checklist |

### Member 04 Responsibility Matrix

| Area | Responsibility | Evidence |
|---|---|---|
| AI Integration | GitHub Models and AI prompt workflow | AI service code |
| Hosting | Azure deployment and environment checks | Deployment notes |
| Documentation | Final appendices collation | Appendix mapping |

---

## Reference List

1. Microsoft. (2025). *ASP.NET Core Blazor documentation*.  
2. Microsoft. (2025). *ASP.NET Core Minimal APIs documentation*.  
3. Microsoft. (2025). *Entity Framework Core documentation*.  
4. Microsoft. (2025). *Azure Container Apps documentation*.  
5. GitHub. (2025). *GitHub Models documentation*.  
6. Google. (2025). *Android development with Kotlin (reference for mobile deliverable)*.

---

## Appendices

### Appendix A – Project Source Code Link
- Repository: `https://github.com/ZeroTrace0245/SmartBit`

### Appendix B – GitHub Commit History and Repository Link
- Repository Link: `https://github.com/ZeroTrace0245/SmartBit`
- Commit History Table (to be completed by team):

| Commit Hash | Date | Author | Summary | Module |
|---|---|---|---|---|
| *(fill later)* |  |  |  |  |
| *(fill later)* |  |  |  |  |
| *(fill later)* |  |  |  |  |
| *(fill later)* |  |  |  |  |
| *(fill later)* |  |  |  |  |

### Appendix C – Project Proposal
- Attach approved proposal document.

### Appendix D – Functional & Technical Report
- Attach functional/technical report document and architecture notes.

### Appendix E – Interim Report
- Include interim report file(s) from `/docs` (e.g., `Group_83_Interim.md`, `report.md`).

### Appendix F – Records of Supervisory Meetings
- Include dated meeting records, action points, and follow-up status.

### Appendix G – Other Materials
- Preliminary designs, wireframes, schema drafts, expanded test logs, and supporting screenshots.
- Include `mobile final.md` as the mobile technical companion artifact.

---

## Submission Notes

- This report follows the requested final report structure.
- Web hosting is explicitly documented as **Azure hosting**.
- AI implementation is explicitly documented as **GitHub Models integration**.
- UI icon approach is explicitly documented as **SVG icons**.
- GitHub commit details are intentionally left as placeholders for manual completion.
