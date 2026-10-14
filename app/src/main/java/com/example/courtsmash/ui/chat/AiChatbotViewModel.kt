package com.example.courtsmash.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.remote.NetworkClient
import com.example.courtsmash.data.remote.OpenRouterMessage
import com.example.courtsmash.data.remote.OpenRouterRequest
import com.example.courtsmash.data.remote.OpenRouterResponseFormat
import com.example.courtsmash.domain.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            text = "Hello! 🏸 I am your CourtSmash AI Badminton Coach & Expert. Ask me anything about singles & doubles serving rules, smash power & defense tactics, 3 racket balance types, and string tension from 23 to 32 lbs!",
            isUser = false
        )
    ),
    val inputText: String = "",
    val isLoading: Boolean = false
)

class AiChatbotViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage(userText: String = _uiState.value.inputText) {
        val trimmed = userText.trim()
        if (trimmed.isEmpty() || _uiState.value.isLoading) return

        val userMessage = ChatMessage(text = trimmed, isUser = true)
        val updatedMessages = _uiState.value.messages + userMessage

        _uiState.value = _uiState.value.copy(
            messages = updatedMessages,
            inputText = "",
            isLoading = true
        )

        viewModelScope.launch {
            val aiReplyText = fetchAiChatResponse(updatedMessages)
            val aiMessage = ChatMessage(text = aiReplyText, isUser = false)
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + aiMessage,
                isLoading = false
            )
        }
    }

    private suspend fun fetchAiChatResponse(conversation: List<ChatMessage>): String = withContext(Dispatchers.IO) {
        val systemPrompt = """
            You are CourtSmash AI Badminton Coach & Expert.
            Answer all user questions in clear, friendly, encouraging, and structured ENGLISH using clear headings and bullet points.
            1. For Rackets: Always explain 3 distinct racket balance types:
               - Head Heavy (Maximum power for steep smashes)
               - Even Balance (All-around versatility for offense & defense)
               - Head Light / Speed (Rapid maneuverability, fast drives, and net defense)
            2. For String Tension: Cover the full range from 23 to 32 lbs:
               - 23-25 lbs: Beginner / Intermediate (Large sweet spot, easy spring power)
               - 26-28 lbs: Intermediate / Advanced (Crisp shuttle feel, solid control & speed)
               - 29-32 lbs: Professional / Elite (Maximum shuttle control & repulsion, requires fast wrist speed)
            3. For Serving Rules: Explain BOTH Singles (long & narrow court) and Doubles (short & wide court), noting the 1.15m height limit and stationary feet.
            4. For Attacking & Defense Tactics: Explain how to smash hard (Pronation & full body rotation), how to defend smashes effectively (wide athletic stance, soft grip block), and how to return quality counter-attacks (steep drops & counter-drives into open spaces).
        """.trimIndent()

        val apiMessages = mutableListOf(OpenRouterMessage("system", systemPrompt))
        conversation.takeLast(8).forEach { msg ->
            val role = if (msg.isUser) "user" else "assistant"
            apiMessages.add(OpenRouterMessage(role, msg.text))
        }

        val apiKey = NetworkClient.openRouterApiKey
        if (apiKey.isNotBlank()) {
            try {
                val request = OpenRouterRequest(
                    model = "google/gemini-2.0-flash-lite-001",
                    messages = apiMessages,
                    responseFormat = OpenRouterResponseFormat("text")
                )
                val response = NetworkClient.openRouterApiService.createChatCompletion(
                    authorization = "Bearer $apiKey",
                    request = request
                )
                val reply = response.choices?.firstOrNull()?.message?.content
                if (!reply.isNullOrBlank()) {
                    return@withContext reply
                }
            } catch (e: Exception) {
                // Fall back to local rule-based response
            }
        }

        return@withContext generateLocalBadmintonAnswer(conversation.last().text)
    }

    private fun generateLocalBadmintonAnswer(question: String): String {
        val q = question.lowercase()
        return when {
            q.contains("racket") || q.contains("string") || q.contains("tension") || q.contains("lbs") || q.contains("ไม้") || q.contains("เอ็น") || q.contains("ปอนด์") ->
                "🏸 **Badminton Racket & String Tension Guide (23–32 lbs):**\n\n" +
                "**1. Racket Balance Types (3 Formats):**\n" +
                "• **Head Heavy:** Concentrated frame weight for extra leverage and maximum smash power.\n" +
                "• **Even Balance:** Equal weight distribution for versatile all-around play in singles & doubles.\n" +
                "• **Head Light / Speed:** Aerodynamic light frame for lightning-fast drive speeds, rapid defense, and net play.\n\n" +
                "**2. String Tension Range (23–32 lbs):**\n" +
                "• **23–25 lbs (Beginner / Intermediate):** Large sweet spot, easy trampoline effect, requiring minimal wrist exertion.\n" +
                "• **26–28 lbs (Intermediate / Advanced):** Crisp shuttle feel, superb control, accuracy, and balanced power.\n" +
                "• **29–32 lbs (Professional / Elite):** Maximum shuttle precision and instant repulsion. Requires strong wrist technique and fast arm speed."

            q.contains("rule") || q.contains("fault") || q.contains("serve") || q.contains("กฎ") || q.contains("เสิร์ฟ") ->
                "🏸 **Badminton Serving Rules (Singles & Doubles):**\n\n" +
                "**1. Singles Serving Rules:**\n" +
                "• **Court Boundaries:** Played on a long and narrow service court (extends to the rearmost boundary line).\n" +
                "• **Service Position:** Even score (0, 2, 4...) = serve from right court | Odd score (1, 3, 5...) = serve from left court.\n\n" +
                "**2. Doubles Serving Rules:**\n" +
                "• **Court Boundaries:** Played on a short and wide service court (shuttle must not pass the rear doubles service line).\n" +
                "• **Court Switching:** Serving team switches service boxes only after winning a point on their own serve.\n\n" +
                "**3. Universal Serving Regulations:**\n" +
                "• Shuttle impact point must be below **1.15 meters** from the court surface.\n" +
                "• Both feet must remain stationary on the court surface without touching boundary lines until the serve is struck."

            q.contains("attack") || q.contains("defend") || q.contains("tactic") || q.contains("smash") || q.contains("ตบ") || q.contains("รับ") || q.contains("strategy") ->
                "🏸 **Mastering Power Smashes, Solid Defense & Counter-Attacks:**\n\n" +
                "**1. Power Smash Technique:**\n" +
                "• **Body Engagement:** Rotate shoulders and hips to generate full-body rotational power.\n" +
                "• **Contact Point:** Strike the shuttle at highest reach in front of your body with forearm pronation.\n" +
                "• **Grip Pressure:** Relax grip during backswing, then squeeze handle tightly at the instant of impact.\n\n" +
                "**2. Solid Defense Against Heavy Smashes:**\n" +
                "• **Wide Low Athletic Stance:** Lower your center of gravity and hold racket frame extended out front.\n" +
                "• **Soft Grip Block:** Cushion the impact by relaxing your grip to drop the shuttle softly back over the net.\n\n" +
                "**3. Quality Counter-Attacking Returns:**\n" +
                "• **Flat Drives:** Push shuttle flat parallel to net into open court spaces.\n" +
                "• **Cross-Court Drop Shots:** Slice shuttle diagonally to force opponents to lift high under pressure."

            else ->
                "🏸 **AI Coach Tip:** Flawless footwork (split-step & chassé steps) is the foundation of badminton mastery. Always recover to central base position immediately after executing every shot!"
        }
    }
}
