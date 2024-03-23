create table if not exists users
(
    id
    bigint
    auto_increment
    primary
    key,
    username
    varchar
(
    256
) not null,
    email varchar
(
    256
) not null,
    password varchar
(
    256
) not null,
    role varchar
(
    256
) not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    updated_at timestamp default CURRENT_TIMESTAMP,
    status varchar
(
    25
) default 'ACTIVE' null,
    constraint users_pk_2
    unique
(
    username
),
    constraint users_pk_3
    unique
(
    email
)
    );
