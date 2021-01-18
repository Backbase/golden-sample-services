
CREATE SEQUENCE seq_review START WITH 1 INCREMENT BY 5;

CREATE TABLE review (id NUMBER(38, 0) NOT NULL, author VARCHAR2(50), content VARCHAR2(255), subject VARCHAR2(255), product_id NUMBER(38, 0), CONSTRAINT PK_REVIEW PRIMARY KEY (id));

COMMENT ON TABLE review IS 'Table to store the Reviews of our Products';

COMMENT ON COLUMN review.author IS 'The author of the Review';

COMMENT ON COLUMN review.content IS 'The content of the Review';

COMMENT ON COLUMN review.subject IS 'the Subject of the Review';

COMMENT ON COLUMN review.product_id IS 'The id of product this review belongs to';

ALTER TABLE review ADD UNIQUE (product_id, id);

ALTER TABLE review ADD stars NUMBER(3);

COMMENT ON COLUMN review.stars IS 'number of stars from 1-5 the reviewer gives to the Product';

