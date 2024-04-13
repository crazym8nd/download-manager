SET FOREIGN_KEY_CHECKS=0;
insert into users (username, email, password, role,status) values
('testuser', 'testmail@gmail.com', 'mvwAEso29ZSiJ2VckPpPvoJQ32Ht+CYCenecgiXUMjY=', 'USER','ACTIVE'),
('Alex', 'alex@gmail.com', 'af17zMKqhPzkVaIkPj5W3cjrNI3+gTaqAkZ90WnQ06w=', 'MODERATOR','ACTIVE'),
('Vitaly', 'vitlaly@gmail.com', 'QvZ8ivl5zRLsJgFvNW6SvvC8qs3XnTh60FcaYInE4Wk=', 'ADMIN','ACTIVE');

insert into files (file_name, location, status, user_id) values
                ('123.jpg','https://storage.yandexcloud.net/springfluxr2dbc/123.jpg','ACTIVE','1'),
                ('222.jpg','https://storage.yandexcloud.net/springfluxr2dbc/222.jpg','ACTIVE','1'),
                ('333.jpg','https://storage.yandexcloud.net/springfluxr2dbc/333.jpg','ACTIVE','1'),
                ('444.jpg','https://storage.yandexcloud.net/springfluxr2dbc/444.jpg','ACTIVE','1'),
                ('555.jpg','https://storage.yandexcloud.net/springfluxr2dbc/555.jpg','ACTIVE','2'),
                ('666.jpg','https://storage.yandexcloud.net/springfluxr2dbc/666.jpg','ACTIVE','3'),
                ('777.jpg','https://storage.yandexcloud.net/springfluxr2dbc/777.jpg','ACTIVE','2'),
                ('888.jpg','https://storage.yandexcloud.net/springfluxr2dbc/888.jpg','ACTIVE','3');

insert into events (user_id, file_id, status) values
                                                  ('1','1','ACTIVE'),
                                                  ('2','2','ACTIVE'),
                                                  ('3','3','ACTIVE');
SET FOREIGN_KEY_CHECKS=1;