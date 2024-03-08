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
