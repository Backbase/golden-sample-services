-- Account Transaction partitioning for MySQL

-- Additional index is required for autoincrement ID column. So we can build complex Primary Key later
CREATE INDEX ix_account_transaction_id ON account_transaction (id);

-- Drop temporary Primary Key to create complex primary key required for Partitioning
ALTER TABLE account_transaction DROP PRIMARY KEY;

-- Drop index
DROP INDEX ix_account_transaction_01 ON account_transaction;

-- Primary Key should contain columns used for partitioning/sub-partitioning
ALTER TABLE account_transaction ADD CONSTRAINT pk_account_transaction PRIMARY KEY (transaction_date, account_id, id);


-- Partition table by RANGE by transaction date, Subpartition by hash by account id.
ALTER TABLE account_transaction PARTITION BY RANGE COLUMNS (transaction_date)
SUBPARTITION BY KEY (account_id)
SUBPARTITIONS 4
(
PARTITION p_dummy VALUES LESS THAN ('2021-01-01'),
PARTITION p_2021_01 VALUES LESS THAN ('2021-02-01'),
PARTITION p_2021_02 VALUES LESS THAN ('2021-03-01'),
PARTITION p_2021_03 VALUES LESS THAN ('2021-04-01'),
PARTITION p_max_value VALUES LESS THAN (MAXVALUE));

-- Create index with leading accountid
CREATE INDEX ix_account_transaction_01 ON account_transaction (account_id,transaction_date);
