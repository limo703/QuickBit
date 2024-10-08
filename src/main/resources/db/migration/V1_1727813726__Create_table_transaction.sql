create table transaction
(
    id              bigint generated by default as identity primary key,
    user_id         integer,
    amount          numeric,
    operation_price numeric,
    currency_id     integer,
    type_opp        boolean
);

alter table wallet
    add column reserved_amount numeric default 0;
