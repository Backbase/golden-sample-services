
ALTER TABLE review ADD stars tinyint
GO

DECLARE @TableName SYSNAME set @TableName = N'review'; DECLARE @FullTableName SYSNAME set @FullTableName = N'dbo.review'; DECLARE @ColumnName SYSNAME set @ColumnName = N'stars'; DECLARE @MS_DescriptionValue NVARCHAR(3749); SET @MS_DescriptionValue = N'number of stars from 1-5 the reviewer gives to the Product';DECLARE @MS_Description NVARCHAR(3749) set @MS_Description = NULL; SET @MS_Description = (SELECT CAST(Value AS NVARCHAR(3749)) AS [MS_Description] FROM sys.extended_properties AS ep WHERE ep.major_id = OBJECT_ID(@FullTableName) AND ep.minor_id=COLUMNPROPERTY(ep.major_id, @ColumnName, 'ColumnId') AND ep.name = N'MS_Description'); IF @MS_Description IS NULL BEGIN EXEC sys.sp_addextendedproperty @name  = N'MS_Description', @value = @MS_DescriptionValue, @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = @TableName, @level2type = N'COLUMN', @level2name = @ColumnName; END ELSE BEGIN EXEC sys.sp_updateextendedproperty @name  = N'MS_Description', @value = @MS_DescriptionValue, @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = @TableName, @level2type = N'COLUMN', @level2name = @ColumnName; END
GO

