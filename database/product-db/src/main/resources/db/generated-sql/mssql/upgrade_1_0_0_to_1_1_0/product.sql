-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: db.changelog-master.xml
-- Ran at: 2/16/21 9:26 AM
-- Against: admin@offline:mssql
-- Liquibase version: 3.7.0
-- *********************************************************************

-- Changeset changelog/db.changelog-1.1.0.xml::1_1_0_001::backbase
-- create index in the product table for the create date column
CREATE NONCLUSTERED INDEX ix_product_create_date ON product(create_date DESC)
GO

-- Changeset changelog/db.changelog-1.1.0.xml::1_1_0_002::backbase
