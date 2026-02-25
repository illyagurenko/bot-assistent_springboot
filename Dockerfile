# Используем легкий образ Java 21 (или 17, если у тебя она)
FROM dockerhub.timeweb.cloud/library/eclipse-temurin:21-jre-alpine

# Указываем рабочую папку
WORKDIR /app

# Копируем наш собранный jar-файл в контейнер
COPY target/*.jar app.jar

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]