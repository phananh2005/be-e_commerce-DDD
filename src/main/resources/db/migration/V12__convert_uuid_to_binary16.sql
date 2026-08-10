-- Convert users.uuid
ALTER TABLE users ADD COLUMN uuid_bin BINARY(16);
UPDATE users SET uuid_bin = UUID_TO_BIN(uuid) WHERE uuid IS NOT NULL;
ALTER TABLE users DROP COLUMN uuid;
ALTER TABLE users CHANGE COLUMN uuid_bin uuid BINARY(16) UNIQUE NOT NULL;

-- Convert products.uuid
ALTER TABLE products ADD COLUMN uuid_bin BINARY(16);
UPDATE products SET uuid_bin = UUID_TO_BIN(uuid) WHERE uuid IS NOT NULL;
ALTER TABLE products DROP COLUMN uuid;
ALTER TABLE products CHANGE COLUMN uuid_bin uuid BINARY(16) UNIQUE NOT NULL;

-- Convert orders.uuid
ALTER TABLE orders ADD COLUMN uuid_bin BINARY(16);
UPDATE orders SET uuid_bin = UUID_TO_BIN(uuid) WHERE uuid IS NOT NULL;
ALTER TABLE orders DROP COLUMN uuid;
ALTER TABLE orders CHANGE COLUMN uuid_bin uuid BINARY(16) UNIQUE NOT NULL;
