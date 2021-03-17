CREATE TABLE product (id BIGINT AUTO_INCREMENT NOT NULL, create_date datetime NOT NULL COMMENT 'The date when the product was created', name VARCHAR(255) NOT NULL COMMENT 'The name of the product', weight SMALLINT NULL COMMENT 'The weight of the product in kgs', CONSTRAINT pk_product PRIMARY KEY (id)) COMMENT='Table to store the Products from our store';

ALTER TABLE product COMMENT = 'Table to store the Products from our store';

ALTER TABLE product ADD CONSTRAINT uq_product_name UNIQUE (name);

CREATE TABLE customer (id BIGINT AUTO_INCREMENT NOT NULL, internal_id VARBINARY(16) NOT NULL COMMENT 'Internal customer identifier - GUID', external_id VARCHAR(36) NOT NULL COMMENT 'External customer identifier', name VARCHAR(128) NOT NULL COMMENT 'Customer name', surname VARCHAR(128) NOT NULL COMMENT 'Customer surname', birthdate date NOT NULL COMMENT 'Customer birthdate', is_active BIT(1) DEFAULT 1 NOT NULL COMMENT 'Is customer active', create_date datetime(3) NOT NULL COMMENT 'The date when the customer was created', last_update_date datetime(3) NULL COMMENT 'The date when the customer was modified last time', additions LONGTEXT NULL COMMENT 'Data model extention for customer', CONSTRAINT pk_customer PRIMARY KEY (id));

ALTER TABLE customer ADD CONSTRAINT uq_customer_internal_id UNIQUE (internal_id);

ALTER TABLE customer ADD CONSTRAINT uq_customer_external_id UNIQUE (external_id);

CREATE INDEX ix_customer_surname ON customer(surname);

CREATE TABLE account (id BIGINT AUTO_INCREMENT NOT NULL, internal_id VARBINARY(16) NOT NULL COMMENT 'Internal account identifier - GUID', external_id VARCHAR(36) NOT NULL COMMENT 'External account identifier', customer_id BIGINT NOT NULL COMMENT 'Reference to customer ID in customer table', name VARCHAR(128) NOT NULL COMMENT 'Account name', iban VARCHAR(128) NOT NULL COMMENT 'IBAN account number', currency CHAR(3) NOT NULL COMMENT 'Currency code ISO', balance DECIMAL(23, 5) NOT NULL COMMENT 'Account balance', available_balance DECIMAL(23, 5) NOT NULL COMMENT 'Account available balance', is_active BIT(1) DEFAULT 1 NOT NULL COMMENT 'Is account active', create_date datetime(3) NOT NULL COMMENT 'The date when the account was created', last_update_date datetime(3) NULL COMMENT 'The date when the account was modified last time', additions LONGTEXT NULL COMMENT 'Data model extention for account', CONSTRAINT pk_account PRIMARY KEY (id));

ALTER TABLE account ADD CONSTRAINT uq_account_internal_id UNIQUE (internal_id);

ALTER TABLE account ADD CONSTRAINT uq_account_external_id UNIQUE (external_id);

CREATE INDEX ix_account_03 ON account(customer_id);

CREATE INDEX ix_account_iban ON account(iban);

ALTER TABLE account ADD CONSTRAINT fk_account2customer_01 FOREIGN KEY (customer_id) REFERENCES customer (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE account_transaction (id BIGINT AUTO_INCREMENT NOT NULL, internal_id VARBINARY(16) NOT NULL COMMENT 'Internal transaction identifier - GUID', external_id VARCHAR(36) NOT NULL COMMENT 'External transaction identifier', account_id BIGINT NOT NULL COMMENT 'Reference to account ID', `description` VARCHAR(128) NOT NULL, is_debit BIT(1) NOT NULL COMMENT 'DR CR identifier', amount DECIMAL(23, 5) NOT NULL COMMENT 'Transaction amount', booking_date date NULL COMMENT 'Transaction booking date', transaction_date datetime(3) NOT NULL COMMENT 'Transaction datetime', additions LONGTEXT NULL COMMENT 'Data model extention for transaction', CONSTRAINT pk_account_transaction PRIMARY KEY (id));

CREATE INDEX ix_account_transaction_01 ON account_transaction(account_id, transaction_date);

CREATE INDEX ix_account_transaction_id ON account_transaction (id);

ALTER TABLE account_transaction DROP PRIMARY KEY;

DROP INDEX ix_account_transaction_01 ON account_transaction;

ALTER TABLE account_transaction ADD CONSTRAINT pk_account_transaction PRIMARY KEY (transaction_date, account_id, id);

ALTER TABLE account_transaction PARTITION BY RANGE COLUMNS (transaction_date)
SUBPARTITION BY KEY (account_id)
SUBPARTITIONS 4
(
PARTITION p_dummy VALUES LESS THAN ('2021-01-01'),
PARTITION p_2021_01 VALUES LESS THAN ('2021-02-01'),
PARTITION p_2021_02 VALUES LESS THAN ('2021-03-01'),
PARTITION p_2021_03 VALUES LESS THAN ('2021-04-01'),
PARTITION p_max_value VALUES LESS THAN (MAXVALUE));

CREATE INDEX ix_account_transaction_01 ON account_transaction (account_id,transaction_date);

INSERT INTO product (name, create_date) VALUES ('Current Account', str_to_date('01/01/2021','%d/%m/%Y'));

COMMIT;

CREATE INDEX ix_product_create_date ON product(create_date DESC);

ALTER TABLE customer ADD address VARCHAR(255) NULL;

ALTER TABLE customer ADD COLUMN external_id_upper VARCHAR(36) AS (UPPER(EXTERNAL_ID));

CREATE INDEX ix_customer_external_id_upper ON customer(external_id_upper);

INSERT INTO product (name, create_date) VALUES ('Savings Account', str_to_date('01/01/2021','%d/%m/%Y'));

COMMIT;
