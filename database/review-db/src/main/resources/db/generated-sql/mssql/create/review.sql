
CREATE SEQUENCE seq_review START WITH 1 INCREMENT BY 5
GO

CREATE TABLE review (id bigint NOT NULL, author nvarchar(50), content nvarchar(255), product_id bigint, subject nvarchar(255), CONSTRAINT PK_REVIEW PRIMARY KEY (id))
GO

ALTER TABLE review ADD CONSTRAINT ix_review_unique UNIQUE (product_id, id)
GO

ALTER TABLE review ADD stars tinyint
GO

