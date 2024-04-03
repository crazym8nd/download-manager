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
create table if not exists files
(
    id
               bigint
        auto_increment
        primary
            key,
    file_name
               varchar
                   (
                   256
                   ) not null,
    location varchar
                   (
                   256
                   ) not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null,
    status varchar
                   (
                   25
                   ) default 'ACTIVE' not null,
    constraint files_pk_2
        unique
        (
         file_name
            ),
    user_id
               bigint
        not
                         null,
    constraint fk_files_user_id
        foreign key
            (user_id) references users (id)
);

create table if not exists events
(
    id
        bigint
        auto_increment
        primary
            key,
    user_id
        bigint
                               not
                                   null,
    file_id
        bigint
                               not
                                   null,
    created_at
        timestamp
              default
                          CURRENT_TIMESTAMP
                               not
                                   null,
    updated_at
        timestamp
              default
                          CURRENT_TIMESTAMP
                               not
                                   null,
    status
        varchar
            (
            25
            ) default 'ACTIVE' not null,
    constraint users_pk_2
        unique
        (
         user_id,
         file_id
            ),
    constraint fk_events_user_id
        foreign key
            (
             user_id
                ) references users
            (
             id
                ),
    constraint fk_events_file_id
        foreign key
            (
             file_id
                ) references files
            (
             id
                )
);
