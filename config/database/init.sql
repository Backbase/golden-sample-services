CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'product_user';
CREATE USER IF NOT EXISTS 'review_user'@'%' IDENTIFIED BY 'review_user';
CREATE USER IF NOT EXISTS 'rebrand_product_user'@'%' IDENTIFIED BY 'rebrand_product_user';
CREATE USER IF NOT EXISTS 'rebrand_review_user'@'%' IDENTIFIED BY 'rebrand_review_user';

CREATE DATABASE IF NOT EXISTS product;
CREATE DATABASE IF NOT EXISTS review;
CREATE DATABASE IF NOT EXISTS rebrand_product;
CREATE DATABASE IF NOT EXISTS rebrand_review;

GRANT ALL PRIVILEGES ON product.* TO 'product_user'@'%';
GRANT ALL PRIVILEGES ON review.* TO 'review_user'@'%';
GRANT ALL PRIVILEGES ON rebrand_product.* TO 'rebrand_product_user'@'%';
GRANT ALL PRIVILEGES ON rebrand_review.* TO 'rebrand_review_user'@'%';
