## Database module

### Folder structure
    database
        └── db
            ├── changelog
            │   └── db.changelog-1.0.0.xml 
            │   └── db.changelog-1.1.0.xml
            │   └── db.changelog-persistence.xml       
            │  
            └── generated-sql
                ├── mssql
                │   └── create
                │   │   └── product.sql
                │   └── upgrade_1_0_0_to_1_1_0
                │       └── product.sql
                ├── mysql
                │   └── create
                │   │   └── product.sql
                │   └── upgrade_1_0_0_to_1_1_0
                │       └── product.sql                  
                └── oracle
                    └── create
                    │   └── product.sql
                    └── upgrade_1_0_0_to_1_1_0
                        └── product.sql     
            └── support-files
                ├── mssql
                │   └── 1_0_0_01_account_transaction_partitioning.sql
                │   └── 1_1_0_01_product_table_data.sql
                ├── mysql
                │   └── 1_0_0_01_account_transaction_partitioning.sql
                │   └── upgrade_1_0_0_to_1_1_0               
                └── oracle
                    └── 1_0_0_01_account_transaction_partitioning.sql
                    └── 1_1_0_01_product_table_data.sql                          

### How to generate DDL scripts

```bash
mvn clean package -P generateSQL
```

> **For Windows users**: before commit make sure line endings of generated scripts are `LF` not `CRLF`
