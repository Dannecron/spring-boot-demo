insert into product (guid, name, description, price, created_at) values
    (gen_random_uuid(), 'salt', 'simple salt', 1200, now()),
    (gen_random_uuid(), 'pepper', 'black pepper', 2099, now()),
    (gen_random_uuid(), 'sugar', 'sweet sugar', 3129, now());