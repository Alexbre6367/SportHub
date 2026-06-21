SportHub — это мобильное приложение для отслеживания физической активности, питания и прогресса с использованием машинного зрения (ML Kit) и LLM на базе (Gemini).

## Основные возможности:
- AI-трекинг упражнений: Использование ML Kit для анализа поз (Pose Detection) и подсчета повторений в реальном времени.
- Интеллектуальный помощник: Интегрированный чат на базе Google Gemini для получения персональных рекомендаций по тренировкам и диете.
- Анализ состояния кожи: Использование Face Detection для мониторинга состояния пользователя.
- Безопасный аккаунт: Полноценная система авторизации (Firebase) с поддержкой Google Sign-In и соблюдением GDPR
- Трекинг показателей: Удобный учет веса, потребления воды и показателей активности с визуализацией прогресса.

## Технический стек:
- UI: Полностью на Jetpack Compose
- Architecture: MVVM + Clean Architecture.
- Language: Kotlin + Coroutines & Flow
- Auth: Firebase Auth + Credential Manager(Google/Apple).
- Database:
    - Local: Room (кэширование данных, оффлайн-режим). 
    - Cloud: Firebase Firestore (синхронизация в реальном времени).
- ML & AI: Google ML Kit(Pose/Face Detection), Firebase AI Logic(Gemini).
- Camera: CameraX (оптимизированная работа с камерой и анализом кадров)
- Security: EncryptedSharedPreferences (SecureStorage) для хранения конфиденциальных данных.

Для успешной компиляции и работы приложения вам необходимо добавить собственные конфигурационные файлы, которые исключены из репозитория.

- google-services.json с Firebase(настроить правила безопасности и AI Logic)
- API Keys c Google Cloud console для работы авторизации через Google Account
