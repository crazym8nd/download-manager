SET FOREIGN_KEY_CHECKS=0;
insert into users (username, email, password, role,status) values
('testuser', 'testmail@gmail.com', 'mvwAEso29ZSiJ2VckPpPvoJQ32Ht+CYCenecgiXUMjY=', 'USER','ACTIVE'),
('Alex', 'alex@gmail.com', 'af17zMKqhPzkVaIkPj5W3cjrNI3+gTaqAkZ90WnQ06w=', 'MODERATOR','ACTIVE'),
('Vitaly', 'vitlaly@gmail.com', 'QvZ8ivl5zRLsJgFvNW6SvvC8qs3XnTh60FcaYInE4Wk=', 'ADMIN','ACTIVE');

insert into files (file_name, location, status, user_id) values
                ('123.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','1'),
                ('222.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','1'),
                ('333.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','1'),
                ('444.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','1'),
                ('555.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','2'),
                ('666.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','3'),
                ('777.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','2'),
                ('888.jpg','http://springfluxr2dbc.s3.localhost.localstack.cloud:4566/springfluxr2dbc/111.jpeg','ACTIVE','3');

insert into events (user_id, file_id, status) values
                                                  ('1','1','ACTIVE'),
                                                  ('2','2','ACTIVE'),
                                                  ('3','3','ACTIVE'),
                                                  ('1','4','ACTIVE');
SET FOREIGN_KEY_CHECKS=1;