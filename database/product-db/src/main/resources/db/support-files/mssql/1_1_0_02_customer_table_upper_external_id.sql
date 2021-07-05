-- Add Computed column for customer table UPPER(external_id)
-- By default computed value is not persisted in database, it is calculated on fly when fetched. So it is not recommended to fetch this column in projection part of SQL.
-- If data is only searched by UPPER(external_id) index on this column will store calculated value and there is no need to persist column.
-- If computed column is frequently fetched - it is better to persist this column in database, it will require additional storage.

ALTER TABLE [customer] ADD [external_id_upper] AS (UPPER([external_id]))
GO