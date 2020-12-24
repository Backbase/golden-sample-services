
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 5
GO

CREATE TABLE product (id bigint NOT NULL, create_date datetime, name varchar(255), weight smallint, CONSTRAINT PK_PRODUCT PRIMARY KEY (id))
GO

CREATE UNIQUE NONCLUSTERED INDEX idx_create_date ON [public].product(create_date DESC)
GO

