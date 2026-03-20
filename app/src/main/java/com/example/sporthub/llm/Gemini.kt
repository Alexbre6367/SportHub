package com.example.sporthub.llm

import android.util.Log
import com.example.sporthub.data.sporthub.HealthEntity
import com.example.sporthub.data.sporthub.User
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content

object Gemini {
    private val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
        modelName = "gemini-2.5-flash-lite",
        systemInstruction = content { text("Ты агент в приложении для спорта и здоровья.") }
    )

    suspend fun analyze(
        description: String,
        history: List<Content>,
        user: User,
        healthData: HealthEntity
    ): String? {
        Log.d("MyLog", "Начало анализа: $description")
        val userDataContext = user.let {
            """
                    Данные пользователя:
                    ${it.name}
                    ${it.weight} 
                    ${it.height} 
                    ${it.birthdate} 
                    ${it.gender} 
                """.trimIndent()
        }

        val healthContext = healthData.let {
            """
                    ${it.dateId} 
                    ${it.sleep}
                    ${it.steps}                   
                    ${it.oxygen} 
                    ${it.calories} 
                    ${it.water} 
                """.trimIndent()
        }
        val context = """
                Ты агент в приложении для спорта и здоровья.
                Твоя цель помогать пользоввтелю с его задачами по теме здоровья, спорта и т.п, не отвечая на дрегие темы.
                Ответ должен быть в формате сплошного текста, без заоголовков, отступов и т.д, ты работаешь в виде чата. 
                Язык ответа соответсвует языку промпта пользователя.
                $userDataContext, $healthContext             
            """.trimIndent()

        val chatSession = model.startChat(history = history)

        return try {
            val prompt = "$context, $description"
            val response = chatSession.sendMessage(prompt)

            Log.d("MyLog", "Анализ успешно завершен. Результат: '$response'")
            response.text
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка при анализе текста", e)
            null
        }
    }
}