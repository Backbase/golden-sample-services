-- create the review additions table
CREATE TABLE review_additions (review_additions_id NUMBER(38, 0) NOT NULL, property_value VARCHAR2(255), property_key VARCHAR2(50) NOT NULL);

COMMENT ON TABLE review_additions IS 'Table to store additional review info';

COMMENT ON COLUMN review_additions.review_additions_id IS 'related review id';

COMMENT ON COLUMN review_additions.property_value IS 'value of the property';

COMMENT ON COLUMN review_additions.property_key IS 'key of the property';

