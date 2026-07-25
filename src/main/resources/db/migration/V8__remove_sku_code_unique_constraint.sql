ALTER TABLE product_variants DROP INDEX sku_code;

ALTER TABLE product_variants MODIFY sku_code VARCHAR(255) NOT NULL;
