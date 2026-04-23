alter table currency_price
    add column if not exists volume_24h decimal;
