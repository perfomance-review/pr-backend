## Api приложения

### Получение опросов

`GET /polls`

Возвращает массив из объектов с полями:

* **title** - название опроса
* **deadline** - дедлайн
* **questionsCount** - количество вопросов
* **respondentsCount** - количество респондентов
* **status** - статус