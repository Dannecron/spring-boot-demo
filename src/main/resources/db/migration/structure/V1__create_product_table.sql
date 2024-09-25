create table product (
    id          bigserial primary key,
    guid        uuid not null,
    name        varchar(255) not null,
    description text,
    price       bigint not null,
    created_at  timestamptz not null,
    updated_at  timestamptz
)
