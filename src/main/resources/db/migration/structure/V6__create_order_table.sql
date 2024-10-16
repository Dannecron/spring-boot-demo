create table "order" (
    id            bigserial primary key,
    guid          uuid not null,
    customer_id   bigint not null,
    delivered_at  timestamptz,
    created_at    timestamptz not null,
    updated_at    timestamptz,
    CONSTRAINT order_customer_foreign
        FOREIGN KEY(customer_id)
        REFERENCES customer(id)
        ON DELETE CASCADE
);

create unique index order_guid_idx ON "order" (guid);
