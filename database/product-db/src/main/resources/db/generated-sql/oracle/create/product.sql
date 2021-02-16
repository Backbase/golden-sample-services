-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: db.changelog-master.xml
-- Ran at: 2/16/21 9:14 AM
-- Against: admin@offline:oracle?version=12.1.0.3
-- Liquibase version: 3.7.0
-- *********************************************************************

SET DEFINE OFF;

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_001::backbase
-- create sequence for the product table
CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 5;

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_002::backbase
-- create the product table
CREATE TABLE product (id NUMBER(38, 0) NOT NULL, create_date TIMESTAMP NOT NULL, name VARCHAR2(255), weight NUMBER(5), CONSTRAINT pk_product PRIMARY KEY (id));

COMMENT ON TABLE product IS 'Table to store the Products from our store';

COMMENT ON COLUMN product.create_date IS 'The date when the product was created';

COMMENT ON COLUMN product.name IS 'The name of the product';

COMMENT ON COLUMN product.weight IS 'The weight of the product in kgs';

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_003::backbase
-- create sequence for the customer table
CREATE SEQUENCE seq_customer START WITH 1 INCREMENT BY 1;

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_004::backbase
-- create customer table
CREATE TABLE customer (id NUMBER(38, 0) NOT NULL, internal_id RAW(16) NOT NULL, external_id VARCHAR2(36) NOT NULL, name VARCHAR2(128) NOT NULL, surname VARCHAR2(128) NOT NULL, birthdate date NOT NULL, is_active NUMBER(1) DEFAULT 1 NOT NULL, create_date TIMESTAMP(3) NOT NULL, last_update_date TIMESTAMP(3), additions CLOB, CONSTRAINT pk_customer PRIMARY KEY (id));

COMMENT ON COLUMN customer.internal_id IS 'Internal customer identifier - GUID';

COMMENT ON COLUMN customer.external_id IS 'External customer identifier';

COMMENT ON COLUMN customer.name IS 'Customer name';

COMMENT ON COLUMN customer.surname IS 'Customer surname';

COMMENT ON COLUMN customer.birthdate IS 'Customer birthdate';

COMMENT ON COLUMN customer.is_active IS 'Is customer active';

COMMENT ON COLUMN customer.create_date IS 'The date when the customer was created';

COMMENT ON COLUMN customer.last_update_date IS 'The date when the customer was modified last time';

COMMENT ON COLUMN customer.additions IS 'Data model extention for customer';

ALTER TABLE customer ADD CONSTRAINT uk_customer_01 UNIQUE (name, surname);

ALTER TABLE customer ADD CONSTRAINT uk_customer_02 UNIQUE (internal_id);

ALTER TABLE customer ADD CONSTRAINT uk_customer_03 UNIQUE (external_id);

CREATE INDEX ix_customer_04 ON customer(surname);

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_005::backbase
-- create sequence for the account table
CREATE SEQUENCE seq_account START WITH 1 INCREMENT BY 1;

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_006::backbase
-- create account table
CREATE TABLE account (id NUMBER(38, 0) NOT NULL, internal_id RAW(16) NOT NULL, external_id VARCHAR2(36) NOT NULL, customer_id NUMBER(38, 0) NOT NULL, name VARCHAR2(128) NOT NULL, iban VARCHAR2(128) NOT NULL, currency CHAR(3) NOT NULL, balance NUMBER(23, 5) NOT NULL, available_balance NUMBER(23, 5) NOT NULL, is_active NUMBER(1) DEFAULT 1 NOT NULL, create_date TIMESTAMP(3) NOT NULL, last_update_date TIMESTAMP(3), additions CLOB, CONSTRAINT pk_account PRIMARY KEY (id));

COMMENT ON COLUMN account.internal_id IS 'Internal account identifier - GUID';

COMMENT ON COLUMN account.external_id IS 'External account identifier';

COMMENT ON COLUMN account.customer_id IS 'Reference to customer ID in customer table';

COMMENT ON COLUMN account.name IS 'Account name';

COMMENT ON COLUMN account.iban IS 'IBAN account number';

COMMENT ON COLUMN account.currency IS 'Currency code ISO';

COMMENT ON COLUMN account.balance IS 'Account balance';

COMMENT ON COLUMN account.available_balance IS 'Account available balance';

COMMENT ON COLUMN account.is_active IS 'Is account active';

COMMENT ON COLUMN account.create_date IS 'The date when the account was created';

COMMENT ON COLUMN account.last_update_date IS 'The date when the account was modified last time';

COMMENT ON COLUMN account.additions IS 'Data model extention for account';

ALTER TABLE account ADD CONSTRAINT uk_account_01 UNIQUE (internal_id);

ALTER TABLE account ADD CONSTRAINT uk_account_02 UNIQUE (external_id);

CREATE INDEX ix_account_03 ON account(customer_id);

CREATE INDEX ix_account_04 ON account(iban);

ALTER TABLE account ADD CONSTRAINT fk_account2customer_01 FOREIGN KEY (customer_id) REFERENCES customer (id);

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_007::backbase
-- create sequence for the account_transaction table
CREATE SEQUENCE seq_account_transaction START WITH 1 INCREMENT BY 1;

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_008::backbase
-- create account_transaction table
CREATE TABLE account_transaction (id NUMBER(38, 0) NOT NULL, internal_id RAW(16) NOT NULL, external_id VARCHAR2(36) NOT NULL, account_id NUMBER(38, 0) NOT NULL, description VARCHAR2(128) NOT NULL, is_debit NUMBER(1) NOT NULL, amount NUMBER(23, 5) NOT NULL, booking_date date, transaction_date TIMESTAMP(3) NOT NULL, additions CLOB, CONSTRAINT pk_account_transaction PRIMARY KEY (id));

COMMENT ON COLUMN account_transaction.internal_id IS 'Internal transaction identifier - GUID';

COMMENT ON COLUMN account_transaction.external_id IS 'External transaction identifier';

COMMENT ON COLUMN account_transaction.account_id IS 'Reference to account ID';

COMMENT ON COLUMN account_transaction.is_debit IS 'DR CR identifier';

COMMENT ON COLUMN account_transaction.amount IS 'Transaction amount';

COMMENT ON COLUMN account_transaction.booking_date IS 'Transaction booking date';

COMMENT ON COLUMN account_transaction.transaction_date IS 'Transaction datetime';

COMMENT ON COLUMN account_transaction.additions IS 'Data model extention for transaction';

CREATE INDEX ix_account_transaction_01 ON account_transaction(account_id, transaction_date);

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_009::backbase
-- Partition account_transaction table for mysql mssql
-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_010::backbase
-- Partition account_transaction table for oracle if partitioning is enabled
ALTER TABLE account_transaction DROP CONSTRAINT pk_account_transaction;

DROP INDEX ix_account_transaction_01;

ALTER TABLE account_transaction MODIFY
        PARTITION BY RANGE (transaction_date) INTERVAL (NUMTOYMINTERVAL(1,'MONTH'))
        SUBPARTITION BY HASH (account_id) SUBPARTITIONS 4
        (PARTITION dummy VALUES LESS THAN (TO_DATE('01-JAN-2021','dd-MON-yyyy')));

CREATE UNIQUE INDEX pk_account_transaction ON account_transaction (transaction_date, account_id, id) LOCAL;

ALTER TABLE account_transaction ADD PRIMARY KEY (transaction_date, account_id, id);

CREATE INDEX ix_account_transaction_01 ON account_transaction (account_id, transaction_date) LOCAL;

-- Changeset changelog/db.changelog-1.0.0.xml::1_0_0_011::backbase
-- Changeset changelog/db.changelog-1.1.0.xml::1_1_0_001::backbase
-- create index in the product table for the create date column
CREATE INDEX ix_product_create_date ON product(create_date DESC);

-- Changeset changelog/db.changelog-1.1.0.xml::1_1_0_002::backbase
