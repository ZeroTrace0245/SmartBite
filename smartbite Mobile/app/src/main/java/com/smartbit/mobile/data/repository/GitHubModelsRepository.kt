package com.smartbit.mobile.data.repository

import com.smartbit.mobile.data.remote.GitHubChatRequest
import com.smartbit.mobile.data.remote.GitHubMessage
import com.smartbit.mobile.data.remote.GitHubModelsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubModelsRepository @Inject constructor(
    private val api: GitHubModelsApi
) {
    private val conversationHistory = mutableListOf<GitHubMessage>()

    suspend fun sendMessage(userMessage: String, githubToken: String): Result<String> {
        return try {
            // Add user message to history
            conversationHistory.add(GitHubMessage(role = "user", content = userMessage))

            // Create request
            val request = GitHubChatRequest(
                messages = conversationHistory,
                temperature = 0.7,
                maxTokens = 1024
            )

            // Send to GitHub Models API
            val response = api.sendMessage(
                request = request,
                authorization = "Bearer $githubToken"
            )

            // Extract assistant message
            val assistantMessage = response.choices.firstOrNull()?.message?.content
                ?: return Result.failure(Exception("No response from AI"))

            // Add to history
            conversationHistory.add(
                GitHubMessage(role = "assistant", content = assistantMessage)
            )

            Result.success(assistantMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun clearHistory() {
        conversationHistory.clear()
    }

    fun addSystemPrompt(prompt: String) {
        conversationHistory.add(0, GitHubMessage(role = "system", content = prompt))
    }
}
