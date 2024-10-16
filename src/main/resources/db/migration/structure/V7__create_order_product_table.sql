create table order_product (
    guid          uuid primary key,
    order_id      bigint not null,
    product_id    bigint not null,
    created_at    timestamptz not null,
    updated_at    timestamptz,
    CONSTRAINT order_product_order_foreign
        FOREIGN KEY(order_id)
        REFERENCES "order"(id)
        ON DELETE CASCADE,
    CONSTRAINT order_product_product_foreign
        FOREIGN KEY(product_id)
        REFERENCES product(id)
        ON DELETE CASCADE
);
