-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: db.changelog-master.xml
-- Ran at: 2/16/21 9:23 AM
-- Against: admin@offline:oracle?version=12.1.0.3
-- Liquibase version: 3.7.0
-- *********************************************************************

SET DEFINE OFF;

-- Changeset changelog/db.changelog-1.1.0.xml::1_1_0_001::backbase
-- create index in the product table for the create date column
CREATE INDEX ix_product_create_date ON product(create_date DESC);

-- Changeset changelog/db.changelog-1.1.0.xml::1_1_0_002::backbase
