# Beans Profile Service

**Beans Profile Service** – это микросервис для управления профилями пользователей кофейни. Он поддерживает регистрацию гостей и бариста, аутентификацию с использованием JWT токенов и привязку пользователей к конкретным кофейням. Также реализовано асинхронное взаимодействие с другими микросервисами через Kafka.

## Ключевые функции

- **Регистрация пользователей:** Поддерживается регистрация гостей и бариста.
- **Аутентификация:** Выдача JWT токенов для безопасного доступа к защищённым ресурсам.
- **Привязка к кофейне:** Возможность привязывать профили пользователей к конкретной кофейне.
- **Асинхронные события:** Отправка события `user-created` при регистрации и обработка события `loyalty-account-created`.
- **Управление безопасностью:** Использование Spring Security и JWT для разграничения доступа.

---

## Требования

Для запуска сервиса требуется следующая инфраструктура:

1. **База данных:** PostgreSQL
    - Подключение настраивается в `application.yml`.
2. **Сообщения Kafka:**
    - Конфигурация брокеров Kafka указывается в `application.yml`.
3. **Переменные окружения:**
    - `JWT_SECRET_KEY` – секретный ключ для генерации и проверки JWT токенов.

---

## Инструкция по сборке и запуску

### 1. **Клонирование репозитория**

```bash
git clone https://github.com/your-organization/beans-profile-service.git
cd beans-profile-service
```
2. Сборка проекта

Убедитесь, что у вас установлен Java 17+ и Gradle. Затем выполните:

```./gradlew clean build```

3. Запуск сервиса

Перед запуском установите необходимые переменные окружения:

```export JWT_SECRET_KEY=your-secret-key```

Затем выполните команду для запуска:

`java -jar target/beans-profile-service-0.0.1-SNAPSHOT.jar`

4. Тестирование

Сервис будет доступен по адресу http://localhost:8001. Основные эндпоинты:

```    
    POST /auth/login – аутентификация пользователя и получение JWT токена.
    POST /profile – регистрация пользователя.
    PUT /profile – привязка пользователя к кофейне.
```