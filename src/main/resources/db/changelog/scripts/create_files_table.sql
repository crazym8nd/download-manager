create table if not exists files
(
    id         bigint auto_increment
        primary key,
    file_name  varchar(256)                          not null,
    location      varchar(256)                       not null,
    created_at timestamp   default CURRENT_TIMESTAMP not null,
    updated_at timestamp   default CURRENT_TIMESTAMP not null,
    status     varchar(25) default 'ACTIVE'          not null,
    constraint files_pk_2
        unique (file_name)
);
