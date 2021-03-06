# Бэкенд чата на akka

Дедлайн 16 марта в 23:59

Модель акторов может быть неплохо переиспользована для реализации простого бэкенда чата. В задаче нужно его реализовать. 

Должен быть реализован механизм публичных каналов.

Каждый пользователь может создавать канал, постить в канал и читать любой канал.

Предполагаем, что пользователи разумны и много каналов создавать не будут.

В канале должно быть доступно 100 последних сообщений. При постинге сообщений должны дропаться более старые, которые не укладываются в лимит.


Реализовать как web api. 

## Методы API:

- `GET /api/channels` - получить список всех каналов в виде пар (id, name)
- `POST /api/channels` - создать новый канал. В теле передается название канала, в ответ приходит его id
- `GET /api/channels/{channelId}/messages` - получить список сообщений канала в виде кортежей (timestamp, senderName, message)
- `PUT /api/channels/{channelId}/messages` - запостить в канал сообщение. В теле передается имя отправителя и сообщение.

## Технические требования:

- Для работы с http использовать akka http (документация: https://doc.akka.io/docs/akka-http/current/server-side/index.html).
- Логику реализовать на akka classic actors.
- Логика должны быть покрыта unit-тестами (для тестирования акторов использовать https://doc.akka.io/docs/akka/current/testing.html)
- Все данные должны храниться в памяти приложения (да, это будет stateful-бэкенд).
- Реализовать в отдельной ветке
