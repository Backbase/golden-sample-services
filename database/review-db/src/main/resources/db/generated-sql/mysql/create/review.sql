
CREATE TABLE review (id BIGINT AUTO_INCREMENT NOT NULL, author VARCHAR(50) NULL COMMENT 'The author of the Review', content VARCHAR(255) NULL COMMENT 'The content of the Review', subject VARCHAR(255) NULL COMMENT 'the Subject of the Review', product_id BIGINT NULL COMMENT 'The id of product this review belongs to', CONSTRAINT PK_REVIEW PRIMARY KEY (id)) COMMENT='Table to store the Reviews of our Products';

ALTER TABLE review COMMENT = 'Table to store the Reviews of our Products';

ALTER TABLE review ADD UNIQUE (product_id, id);

ALTER TABLE review ADD stars TINYINT NULL COMMENT 'number of stars from 1-5 the reviewer gives to the Product';

