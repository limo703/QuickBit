create table currency
(
    id          bigint generated by default as identity primary key,
    name        varchar not null,
    description varchar not null,
    is_fiat     boolean not null,
    avatar_id   integer
);

create table currency_price
(
    id          bigint generated by default as identity primary key,
    price       decimal   not null,
    currency_id integer   not null,

    created_at  TIMESTAMP NOT NULL DEFAULT current_timestamp
);

alter table currency
    add constraint currency_name_unique unique (name);

insert into currency (name, description, is_fiat)
values ('RUB', '2806', true),
       ('USD', '2781', true),
       ('EUR', '2790', true),
       ('BTC', 'bitcoin', false),
       ('ETH', 'ethereum', false),
       ('DOGE', 'dogecoin', false),
       ('TON', 'toncoin', false),
       ('PEPE', 'pepe', false),
       ('BNB', 'bnb', false),
       ('SOL', 'solana', false);