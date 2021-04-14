
CREATE NONCLUSTERED INDEX ix_product_create_date ON product(create_date DESC)
GO

ALTER TABLE customer ADD address nvarchar(255)
GO

ALTER TABLE [customer] ADD [external_id_upper] AS (UPPER([external_id]))
GO

CREATE NONCLUSTERED INDEX ix_customer_external_id_upper ON customer(external_id_upper)
GO

BEGIN TRANSACTION
GO

INSERT INTO product(id, name, create_date) VALUES (next value for seq_product, N'Savings Account', Convert(nvarchar(30),N'1/1/2021',102))
GO

COMMIT
GO

