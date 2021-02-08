--  create the product additions table
CREATE TABLE product_additions (product_additions_id BIGINT NOT NULL COMMENT 'related product id', property_value VARCHAR(255) NULL COMMENT 'value of the property', `property_key` VARCHAR(50) NOT NULL COMMENT 'key of the property') COMMENT='Table to store additional product info';

ALTER TABLE product_additions COMMENT = 'Table to store additional product info';
