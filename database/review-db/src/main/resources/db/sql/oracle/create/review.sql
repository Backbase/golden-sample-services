
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 5;

CREATE TABLE review (id NUMBER(38, 0) NOT NULL, author VARCHAR2(50), content VARCHAR2(255), product_id NUMBER(38, 0), subject VARCHAR2(255), CONSTRAINT PK_REVIEW PRIMARY KEY (id));

ALTER TABLE review ADD CONSTRAINT review_unique_idx UNIQUE (product_id, id);

ALTER TABLE review ADD stars NUMBER(3);

