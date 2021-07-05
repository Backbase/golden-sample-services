-- Account Transaction partitioning for SQL Server

DROP INDEX [ix_account_transaction_01] ON [account_transaction]
GO

ALTER TABLE [account_transaction] DROP CONSTRAINT [pk_account_transaction]
GO

CREATE PARTITION FUNCTION transactionDateRangePF (datetime2(3))
AS RANGE RIGHT FOR VALUES ('20210101', '20210201', '20210301')
GO

-- define partition scheme: for simplicity of internal testing - put all partitions into Primary filegroup
-- customers can define custom filegroups and files, map partitions to filegroups to simplify backup, archiving and make improve performance by distributing IO between disk drives

CREATE PARTITION SCHEME transactionPartitionScheme
AS PARTITION transactionDateRangePF ALL TO ( [PRIMARY] );
GO

ALTER TABLE [account_transaction]
ADD PRIMARY KEY NONCLUSTERED ([transaction_date], [account_id], [id])
ON transactionPartitionScheme(transaction_date);
GO

CREATE NONCLUSTERED INDEX [ix_account_transaction_01] ON [account_transaction]([account_id],[transaction_date])
GO