insert into users (username, password, email, role, created_at, updated_at)
values ('testname', 'kWU0fNsZLVOkvva15FokABcm8ZzeOZOmuo7WoytRP/M=', 'testmail@gmail.com', 'USER',
        '2021-01-03 00:00:00', '2021-01-03 00:00:00'),
       ('Alex', 'af17zMKqhPzkVaIkPj5W3cjrNI3+gTaqAkZ90WnQ06w=', 'alex@gmail.com', 'MODERATOR', '2021-01-02 00:00:00',
        '2021-01-02 00:00:00'),
       ('Vitaly', 'QvZ8ivl5zRLsJgFvNW6SvvC8qs3XnTh60FcaYInE4Wk=', 'vitlaly@gmail.com', 'ADMIN', '2021-01-01 00:00:00',
        '2021-01-01 00:00:00');

/*
 "testname":"test"
 "Alex":"test1"
 "Vitaly":"testtest"
 */

insert into files (file_name, location, user_id)
VALUES ('123.jpg', 'https://storage.yandexcloud.net/springfluxr2dbc/123.jpg',1),
       ('222.jpg', 'https://storage.yandexcloud.net/springfluxr2dbc/222.jpg', 2),
       ('333.jpg', 'https://storage.yandexcloud.net/springfluxr2dbc/333.jpg', 3);

insert into events (user_id, file_id)
values (1, 1),
       (2, 2),
       (3, 3);