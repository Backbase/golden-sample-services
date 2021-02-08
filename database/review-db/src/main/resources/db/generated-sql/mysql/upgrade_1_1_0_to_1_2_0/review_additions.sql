--  create the review additions table
CREATE TABLE review_additions (review_additions_id BIGINT NOT NULL COMMENT 'related review id', property_value VARCHAR(255) NULL COMMENT 'value of the property', property_key VARCHAR(50) NOT NULL COMMENT 'key of the property') COMMENT='Table to store additional review info';

ALTER TABLE review_additions COMMENT = 'Table to store additional review info';
