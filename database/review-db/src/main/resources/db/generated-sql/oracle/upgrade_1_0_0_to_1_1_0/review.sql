
ALTER TABLE review ADD stars NUMBER(3);

COMMENT ON COLUMN review.stars IS 'number of stars from 1-5 the reviewer gives to the Product';

