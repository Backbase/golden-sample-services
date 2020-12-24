
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 5;

CREATE TABLE product (id NUMBER(38, 0) NOT NULL, create_date TIMESTAMP, name VARCHAR2(255), weight NUMBER(5), CONSTRAINT PK_PRODUCT PRIMARY KEY (id));

CREATE UNIQUE INDEX public.idx_create_date ON public.product(create_date DESC);

