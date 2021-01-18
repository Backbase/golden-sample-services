# Introduction

Let's compare the most common surrogate primary key strategies:

IDENTITY
SEQUENCE
TABLE (SEQUENCE)

## IDENTITY

The IDENTITY generator allows an integer/bigint column to be auto-incremented on demand. 
The increment process happens outside of the current running transaction, 
so a roll-back may end-up discarding already assigned values (value gaps may happen).

The increment process is very efficient since it uses a database internal lightweight 
locking mechanism as opposed to the more heavyweight transactional course-grain locks.

The only drawback is that we can’t know the newly assigned value prior to executing the INSERT 
statement. This restriction is hindering the transactional write-behind strategy adopted by Hibernate. 
> For this reason, Hibernate cannot use JDBC batching when persisting entities that are using the IDENTITY generator.
> Also the IDENTITY generator strategy doesn’t work with the TABLE_PER_CLASS inheritance model because there could be 
> multiple subclass entities having the same identifier and a base class query will end up retrieving 
> entities with the same identifier (even if belonging to different types).

## SEQUENCE

A SEQUENCE is a database object that generates incremental integers on each successive request. 
SEQUENCES are much more flexible than IDENTIFIER columns because:

- A SEQUENCE is table free and the same sequence can be assigned to multiple columns or tables
- A SEQUENCE may preallocate values to improve performance
- A SEQUENCE may define an incremental step, allowing us to benefit from a “pooled” Hilo algorithm
- A SEQUENCE doesn’t restrict Hibernate JDBC batching
- A SEQUENCE doesn’t restrict Hibernate inheritance models

## TABLE

There is another database-independent alternative to generating sequences. One or multiple tables can be used to hold 
the identifier sequence counter. But it means trading write performance for database portability.

While IDENTITY and SEQUENCES are transaction-less, using a database table mandate ACID, 
for synchronizing multiple concurrent id generation requests.

This is made possible by using row-level locking which comes at a higher cost than IDENTITY or SEQUENCE generators.

The sequence must be calculated in a separate database transaction and this requires the IsolationDelegate mechanism, 
which has support for both local (JDBC) and global(JTA) transactions.

For local transactions, it must open a new JDBC connection, therefore putting more pressure on the current connection pooling mechanism.
For global transactions, it requires suspending the current running transaction. 
After the sequence value is generated, the actual transaction has to be resumed. This process has its own cost, 
so the overall application performance might be affected.

[Why you should never use the TABLE identifier generator with JPA and Hibernate](https://vladmihalcea.com/why-you-should-never-use-the-table-identifier-generator-with-jpa-and-hibernate/)

## AUTO

Using `AUTO GenerationType` is not an option because since Hibernate picks the TABLE generator 
instead of IDENTITY when the underlying database does not support sequences. 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

To fix that we could use

    @Id
    @GeneratedValue(
        strategy= GenerationType.AUTO,
        generator="native"
    )
    @GenericGenerator(
        name = "native",
        strategy = "native"
    )
    private Long id;
    
But then we can't customize the SEQUENCE generator. 

## Conclusion

Our services need to run on MySQL (which does not support database sequences), 
as well as Oracle and SQL Server, so portability is our primary concern.
As previously explained, the TABLE identifier generator is database-independent but 
does not scale, so we will avoid it. 

We will just use SEQUENCE by default, and override this with the IDENTITY strategy for MySQL.
Check how we achieve that:

- [Config Class](https://github.com/Backbase/golden-sample-services/tree/main/review/src/main/java/com/backbase/goldensample/review/config/IdentityStrategyOverrideConfiguration.java)
- [Config YML File](https://github.com/Backbase/golden-sample-services/tree/main/review/src/main/resources/db/mapping/mysql_entities_identity_strategy_override.yml)
- [MySQL ORM File](https://github.com/Backbase/golden-sample-services/tree/main/review/src/main/resources/db/mapping/mysql-orm.xml)
- [Liquibase changelog](https://github.com/Backbase/golden-sample-services/tree/main/review/src/main/resources/db/changelog/000-create.yaml)
                                                                                                     
