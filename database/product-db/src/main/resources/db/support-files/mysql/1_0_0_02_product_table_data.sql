-- Create current account product in product table

INSERT INTO product (name, create_date) VALUES ('Current Account', str_to_date('01/01/2021','%d/%m/%Y'));

COMMIT;