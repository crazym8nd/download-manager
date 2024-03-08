Необходимо реализовать REST API, которое взаимодействует с файловым хранилищем AWS S3 и предоставляет возможность
получать доступ к файлам и истории загрузок.Логика безопасности должна быть реализована средствами JWT токена.
Приложение должно быть докеризировано и готового к развертыванию в виде Docker контейнера.
<br>Сущности:<br>
User (List<Event> events, Status status, …)<br>
Event (User user, File file, Status status)<br>
File (id, location, Status status ...)<br>
User -> … List<Events> events ...<br>
Взаимодействие с S3 должно быть реализовано с помощью AWS SDK.<br>
Уровни доступа:<br>
ADMIN - полный доступ к приложению<br>
MODERATOR - права USER + чтение всех User + чтение/изменение/удаление всех Events + чтение/изменение/удаление всех
Files<br>
USER - только чтение всех своих данных + загрузка файлов для себя<br>
<br>Технологии: Java, MySQL, Spring (Boot, Reactive Data, WebFlux, Security), AWS SDK, MySQL, Docker, JUnit, Mockito,
Gradle.
