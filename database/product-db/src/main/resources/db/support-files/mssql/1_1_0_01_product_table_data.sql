-- Create current account product in product table
BEGIN TRANSACTION
GO

INSERT INTO product(id, name, create_date) VALUES (next value for seq_product, N'Savings Account', Convert(nvarchar(30),N'1/1/2021',102))
GO

COMMIT
GO