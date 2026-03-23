var builder = DistributedApplication.CreateBuilder(args);

var apiService = builder.AddProject<Projects.computer_project_ApiService>("apiservice")
    .WithHttpHealthCheck("/health");

builder.AddProject<Projects.computer_project_Web>("webfrontend")
    .WithExternalHttpEndpoints()
    .WithHttpHealthCheck("/health")
    .WithReference(apiService)
    .WaitFor(apiService);

builder.Build().Run();
