var builder = DistributedApplication.CreateBuilder(args);

var chatModel = builder.AddGitHubModel("chatModel", "gpt-4o-mini");

var db = builder.AddPostgres("postgres")
                .AddDatabase("smartbitedb");

var cache = builder.AddRedis("cache");

var apiService = builder.AddProject<Projects.computer_project_ApiService>("apiservice")
    .WithReference(chatModel)
    .WithReference(db)
    .WithReference(cache)
    .WithHttpHealthCheck("/health");

builder.AddProject<Projects.computer_project_Web>("webfrontend")
    .WithExternalHttpEndpoints()
    .WithHttpHealthCheck("/health")
    .WithReference(apiService)
    .WithReference(cache)
    .WaitFor(apiService);

builder.Build().Run();
