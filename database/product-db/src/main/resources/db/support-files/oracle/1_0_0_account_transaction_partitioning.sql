-- Account Transaction partitioning for Oracle

-- Drop existing Primary Key constraint
ALTER TABLE account_transaction DROP CONSTRAINT pk_account_transaction;

-- Drop existing index
DROP INDEX ix_account_transaction_01;

-- Partition table by range by transaction_date and by hash by account_id
ALTER TABLE account_transaction MODIFY
        PARTITION BY RANGE (transaction_date) INTERVAL (NUMTOYMINTERVAL(1,'MONTH'))
        SUBPARTITION BY HASH (account_id) SUBPARTITIONS 4
        (PARTITION dummy VALUES LESS THAN (TO_DATE('01-JAN-2021','dd-MON-yyyy')));

-- Create Unique local index which can be used for Primary Key constraint
CREATE UNIQUE INDEX pk_account_transaction ON account_transaction (transaction_date, account_id, id) LOCAL;

-- Add Primary Key constraint using existing unique local index
ALTER TABLE account_transaction ADD PRIMARY KEY (transaction_date, account_id, id);

CREATE INDEX ix_account_transaction_01 ON account_transaction (account_id, transaction_date) LOCAL;