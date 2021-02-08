-- create the product additions table
CREATE TABLE product_additions (product_additions_id NUMBER(38, 0) NOT NULL, property_value VARCHAR2(255), property_key VARCHAR2(50) NOT NULL);

COMMENT ON TABLE product_additions IS 'Table to store additional product info';

COMMENT ON COLUMN product_additions.product_additions_id IS 'related product id';

COMMENT ON COLUMN product_additions.property_value IS 'value of the property';

COMMENT ON COLUMN product_additions.property_key IS 'key of the property';
