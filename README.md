# ExploreWithMe
Приложение ExploreWithMe - афиша, которая позволяет пользователям делиться информацией об интересных событиях и находить компанию для участия в них.

Инструкция по развертыванию проекта:
 скачать данный репозиторий
 mvn clean package
 mvn install
 docker-compose build
 docker-compose up -d
 основной сервис: http://localhost:8080
 сервис статистики: http://localhost:9090
