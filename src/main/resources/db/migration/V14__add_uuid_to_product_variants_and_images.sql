-- For product_variants
ALTER TABLE product_variants ADD COLUMN uuid BINARY(16) AFTER id;
UPDATE product_variants SET uuid = UUID_TO_BIN(UUID()) WHERE uuid IS NULL;
ALTER TABLE product_variants MODIFY COLUMN uuid BINARY(16) NOT NULL;
ALTER TABLE product_variants ADD CONSTRAINT UK_product_variants_uuid UNIQUE (uuid);

-- For variant_image
ALTER TABLE variant_image ADD COLUMN uuid BINARY(16) AFTER id;
UPDATE variant_image SET uuid = UUID_TO_BIN(UUID()) WHERE uuid IS NULL;
ALTER TABLE variant_image MODIFY COLUMN uuid BINARY(16) NOT NULL;
ALTER TABLE variant_image ADD CONSTRAINT UK_variant_image_uuid UNIQUE (uuid);
