-- 1. Add column
ALTER TABLE cart_items ADD COLUMN uuid BINARY(16) AFTER id;

-- 2. Fill existing rows with random UUIDs
UPDATE cart_items SET uuid = UUID_TO_BIN(UUID()) WHERE uuid IS NULL;

-- 3. Make it NOT NULL and add UNIQUE constraint
ALTER TABLE cart_items MODIFY COLUMN uuid BINARY(16) NOT NULL;
ALTER TABLE cart_items ADD CONSTRAINT UK_cart_items_uuid UNIQUE (uuid);
