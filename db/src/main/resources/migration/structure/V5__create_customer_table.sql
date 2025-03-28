create table customer (
    id          bigserial primary key,
    guid        uuid not null,
    name        varchar(255) not null,
    city_id     bigint,
    created_at  timestamptz not null,
    updated_at  timestamptz,
    CONSTRAINT customer_city_foreign
        FOREIGN KEY(city_id)
        REFERENCES city(id)
        ON DELETE SET NULL
);

create unique index customer_guid_idx ON customer (guid);