package com.smartbit.mobile.data.remote

import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// GitHub Models API DTOs
data class GitHubMessage(
    @Json(name = "role")
    val role: String, // "user", "assistant", "system"
    @Json(name = "content")
    val content: String
)

data class GitHubChatRequest(
    @Json(name = "model")
    val model: String = "gpt-4o-mini",
    @Json(name = "messages")
    val messages: List<GitHubMessage>,
    @Json(name = "temperature")
    val temperature: Double = 0.7,
    @Json(name = "max_tokens")
    val maxTokens: Int = 1024
)

data class GitHubChoice(
    @Json(name = "message")
    val message: GitHubMessage,
    @Json(name = "finish_reason")
    val finishReason: String,
    @Json(name = "index")
    val index: Int
)

data class GitHubChatResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "object")
    val objectType: String,
    @Json(name = "created")
    val created: Long,
    @Json(name = "model")
    val model: String,
    @Json(name = "choices")
    val choices: List<GitHubChoice>,
    @Json(name = "usage")
    val usage: ChatUsage?
)

data class ChatUsage(
    @Json(name = "prompt_tokens")
    val promptTokens: Int,
    @Json(name = "completion_tokens")
    val completionTokens: Int,
    @Json(name = "total_tokens")
    val totalTokens: Int
)

interface GitHubModelsApi {
    @POST("openai/deployments/gpt-4o-mini/chat/completions")
    suspend fun sendMessage(
        @Body request: GitHubChatRequest,
        @Header("Authorization") authorization: String
    ): GitHubChatResponse
}
