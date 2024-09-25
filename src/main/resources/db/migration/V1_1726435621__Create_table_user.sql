create table users
(
    id              bigint generated by default as identity primary key,
    username        varchar   not null,
    email           varchar   not null,
    password        varchar   not null,
    first_name      varchar,
    last_name       varchar,
    role            varchar   not null,
    wallet_id        integer,
    avatar_image_id integer,

    created_at      TIMESTAMP NOT NULL DEFAULT current_timestamp,
    updated_at      TIMESTAMP NOT NULL DEFAULT current_timestamp,
    uuid            varchar   not null
);

create table wallet
(
    id          bigint generated by default as identity primary key,
    score       decimal   not null default 0,
    currency_id integer   not null,
    user_id     integer   not null,

    created_at  TIMESTAMP NOT NULL DEFAULT current_timestamp,
    updated_at  TIMESTAMP NOT NULL DEFAULT current_timestamp,
    uuid        varchar   not null
);

create table image
(
    id                 bigint generated by default as identity primary key,
    file_path          varchar   not null,
    original_file_name varchar   not null,
    user_id            integer   not null,

    created_at         TIMESTAMP NOT NULL DEFAULT current_timestamp,
    updated_at         TIMESTAMP NOT NULL DEFAULT current_timestamp,
    uuid               varchar   not null
);

create index on users (username);

alter table users
    add constraint users_valet_id_unique unique (id);
alter table users
    add constraint users_username_unique unique (username);
alter table users
    add constraint users_email_unique unique (email);

alter table wallet
    add constraint valet_user_id_unique unique (user_id);
