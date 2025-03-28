create table city (
    id          bigserial primary key,
    guid        uuid not null,
    name        varchar(255) not null,
    created_at  timestamptz not null,
    updated_at  timestamptz,
    deleted_at  timestamptz
);

create unique index city_guid_idx ON city (guid);