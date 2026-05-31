package com.example.sporthub.llm

import android.graphics.Bitmap
import android.util.Log
import com.example.sporthub.data.sporthub.FaceEntity
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

    private fun data(
        user: User,
        healthData: HealthEntity,
        faceData: FaceEntity
    ): String {
        return """
        Данные пользователя: ${user.name}, Вес: ${user.weight}, Рост: ${user.height}, Пол: ${user.gender}
        Здоровье пользователя: Сон: ${healthData.sleep}, Шаги: ${healthData.steps}, Кислород: ${healthData.oxygen}, Калории: ${healthData.calories}, Вода: ${healthData.water}
        Данные кожи пользователя: Чувствительность: ${faceData.sensitive}, Акне: ${faceData.acne}%, Сухость: ${faceData.dryness}%, Увлажненность: ${faceData.moisture}%
        
        Твоя цель помогать пользователю с его задачами по теме здоровья, спорта, еды и т.п., не отвечая на другие темы.
        Ответ должен быть в формате сплошного текста, без заголовков, отступов и т.д., ты работаешь в виде чата. 
        Если на фото только еда, то определи КБЖУ и дай совет насколько еда полезная и т.д.
        Язык ответа соответствует языку промпта пользователя.
    """.trimIndent()
    }

    suspend fun analyze(
        description: String,
        history: List<Content>,
        user: User,
        healthData: HealthEntity,
        faceData: FaceEntity
    ): String? {
        Log.d("MyLog", "Начало анализа: $description")
        val context = data(user, healthData, faceData)

        val chatSession = model.startChat(history = history)

        return try {
            val prompt = "$context, Промпт пользователя: $description"
            val response = chatSession.sendMessage(prompt)

            Log.d("MyLog", "Анализ успешно завершен. Результат: '$response'")
            response.text
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка при анализе текста", e)
            null
        }
    }

    suspend fun analyzeFace(
        bitmap: Bitmap,
        faceData: FaceEntity
    ): List<Int>? {
        Log.d("MyLog", "Начало анализа лица")
        val faceDataContext = faceData.let {
            """
                    Данные по коже пользователя:
                    ${it.sensitive}
                """.trimIndent()
        }

        val context = """
                Твоя цель — проанализировать фотографию лица и определить три показателя кожи: "Acne" (акне), "Dryness" (сухость), "Moisture" (увлажненность).
                Приложение не является медицинским. 
                Твой ответ должен содержать ТОЛЬКО 3 числа в процентах (от 0 до 100) через запятую.
                Пример ответа: 25, 40, 85
                Порядок: Acne, Dryness, Moisture.
                Если не можешь определить точно, выдай наиболее вероятное приближение.
                $faceDataContext         
            """.trimIndent()

        return try {
            val prompt = content {
                image(bitmap)
                text(context)
            }

            val response = model.generateContent(prompt)
            val responseText = response.text ?: ""

            Log.d("MyLog", "Анализ успешно завершен. Результат: '$responseText'")

            responseText.split(",")
                .map { it.trim().filter { char -> char.isDigit() }.toIntOrNull() ?: 0}
                .take(3)

        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка при анализе лица", e)
            null
        }
    }

    suspend fun analyzePhoto(
        description: String,
        bitmap: Bitmap,
        history: List<Content>,
        user: User,
        healthData: HealthEntity,
        faceData: FaceEntity
    ): String? {
        Log.d("MyLog", "Начало анализа фото")
        val context = data(user, healthData, faceData)

        val chatSession = model.startChat(history = history)

        return try {
            val prompt = content {
                image(bitmap)
                text("$context, Проанализуруй это фото в контексте вопроса: $description")
            }

            val response = chatSession.sendMessage(prompt)
            response.text

        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка при анализе фото", e)
            null
        }
    }
}