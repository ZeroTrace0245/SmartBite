using computer_project.Web;
using computer_project.Web.Components;
using computer_project.Web.Services;
using Microsoft.Extensions.Http.Resilience;
using Azure.AI.OpenAI;
using System.ClientModel;
using OpenAI.Chat;

var builder = WebApplication.CreateBuilder(args);

// Add service defaults & Aspire client integrations.
builder.AddServiceDefaults();

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddOutputCache();
builder.Services.AddResponseCompression();

// Add Redis Distributed Caching automatically via Aspire
builder.AddRedisDistributedCache("cache");

// Add Background Service for Wearable & Health API Polling
builder.Services.AddHostedService<WearableSyncBackgroundService>();

builder.Services.AddScoped<UserSession>();
builder.Services.AddHttpClient<SmartBiteApiClient>(client =>
    {
        client.BaseAddress = new("https+http://apiservice");
    });

// Add GitHub Models ChatClient for NLP Features
var githubToken = builder.Configuration["GitHubToken"];
if (!string.IsNullOrEmpty(githubToken))
{
    var aiClient = new AzureOpenAIClient(
        new Uri("https://models.inference.ai.azure.com"),
        new ApiKeyCredential(githubToken));
    builder.Services.AddSingleton<ChatClient>(sp => aiClient.GetChatClient("gpt-4o"));
}

// Increase resilience timeouts — AI inference via Foundry Local can take 1-2 minutes
// for complex prompts (workout plans, meal suggestions, health reports)
builder.Services.ConfigureAll<HttpStandardResilienceOptions>(options =>
{
    options.AttemptTimeout.Timeout = TimeSpan.FromMinutes(3);
    options.TotalRequestTimeout.Timeout = TimeSpan.FromMinutes(5);
    options.CircuitBreaker.SamplingDuration = TimeSpan.FromMinutes(10);
});


var app = builder.Build();


if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseResponseCompression();

app.UseAntiforgery();

app.UseOutputCache();

app.MapStaticAssets();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.MapDefaultEndpoints();

app.Run();
