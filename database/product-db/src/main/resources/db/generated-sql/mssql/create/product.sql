
CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 5
GO

CREATE TABLE product (id bigint NOT NULL, create_date datetime NOT NULL, name nvarchar(255), weight smallint, CONSTRAINT PK_PRODUCT PRIMARY KEY (id, create_date))
GO

CREATE NONCLUSTERED INDEX ix_product_create_date ON product(create_date DESC)
GO

