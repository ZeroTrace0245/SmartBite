# Authentication & Profile Chips: Common Issues in Blazor Projects

## Authentication Issues

1. **Insecure Password Storage**
   - Storing plain text passwords instead of hashed values.
   - **Solution:** Use a strong hashing algorithm (e.g., BCrypt).
   - **Example:**
     ```csharp
     var hash = BCrypt.Net.BCrypt.HashPassword(password);
     bool isValid = BCrypt.Net.BCrypt.Verify(password, user.PasswordHash);
     ```

2. **Token Leakage**
   - JWT tokens stored in localStorage are vulnerable to XSS attacks.
   - **Solution:** Prefer secure, HTTP-only cookies for token storage. Use CSP headers.

3. **Token Expiry Handling**
   - Not handling expired tokens gracefully, leading to poor user experience.
   - **Solution:** Implement automatic token refresh or force re-login. Show clear messages.
   - **Example:**
     ```csharp
     if (token.IsExpired) { NavigationManager.NavigateTo("/login"); }
     ```

4. **Improper Token Validation**
   - Failing to validate JWT signature, issuer, or audience, allowing forged tokens.
   - **Solution:** Always validate JWT signature, issuer, audience, and expiration.
   - **Example:**
     ```csharp
     services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
         .AddJwtBearer(options =>
         {
             options.TokenValidationParameters = new TokenValidationParameters
             {
                 ValidateIssuer = true,
                 ValidateAudience = true,
                 ValidateLifetime = true,
                 ValidateIssuerSigningKey = true,
                 // ...
             };
         });
     ```

5. **No HTTPS Enforcement**
   - Allowing authentication over HTTP exposes credentials and tokens.
   - **Solution:** Redirect all HTTP requests to HTTPS. Set `RequireHttpsMetadata = true`.

6. **Lack of Account Lockout**
   - No protection against brute-force login attempts.
   - **Solution:** Implement account lockout after several failed attempts.
   - **Example:**
     ```csharp
     services.Configure<IdentityOptions>(options =>
     {
         options.Lockout.DefaultLockoutTimeSpan = TimeSpan.FromMinutes(5);
         options.Lockout.MaxFailedAccessAttempts = 5;
         options.Lockout.AllowedForNewUsers = true;
     });
     ```

---

## Profile Chips (Role-Based Access) Issues

1. **Role Tampering**
   - Roles stored on the client can be modified, leading to unauthorized access.
   - **Solution:** Never trust roles from the client. Always validate roles server-side using claims from a validated token.

2. **Insufficient Authorization Checks**
   - Not checking user roles on every protected API endpoint or Blazor component.
   - **Solution:** Use `[Authorize(Roles = "...")]` on controllers, pages, and Blazor components.
   - **Example:**
     ```csharp
     [Authorize(Roles = "Admin")]
     public IActionResult AdminOnly() { ... }
     ```

3. **Overly Broad Roles**
   - Using generic roles without fine-grained permissions can cause privilege escalation.
   - **Solution:** Define fine-grained roles or use claims-based authorization.
   - **Example:**
     ```csharp
     [Authorize(Policy = "CanViewReports")]
     ```

4. **Role Changes Not Reflected Immediately**
   - Users may retain old permissions until re-login if their role changes in the database.
   - **Solution:** Invalidate tokens or force re-login when a user’s role changes. Use short-lived tokens.

5. **Data Leakage**
   - Not filtering data by user ID or role, allowing access to other users’ data.
   - **Solution:** Always filter data by user ID and role in queries.
   - **Example:**
     ```csharp
     var userMeals = db.Meals.Where(m => m.UserId == userId);
     ```

6. **Hardcoded Role Strings**
   - Using hardcoded role names increases risk of typos and maintenance issues.
   - **Solution:** Use constants or enums for roles.
   - **Example:**
     ```csharp
     public static class Roles { public const string Admin = "Admin"; }
     ```

---

## Recommendations

- Always hash and salt passwords.
- Store tokens securely and validate them on every request.
- Use `[Authorize(Roles = "...")]` attributes on API endpoints and Blazor components.
- Filter all data queries by user ID and role.
- Use enums or constants for roles, not magic strings.
- Regularly review and test your authentication and authorization logic.
