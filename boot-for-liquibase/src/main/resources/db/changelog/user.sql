--liquibase formatted sql

--changeset czy:1
create table user (
id int primary key,
name varchar(255),
sex int ,
phone varchar(32),
address varchar(255),
email varchar(255)
);


--changeset czy:2
insert into user(id,name,sex,phone,address,email) values(1,'czy',1,'13700000000','hangzhou','632300000@xxx.com');