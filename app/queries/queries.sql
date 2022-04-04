create table swiatwoblerow.customer_address(
id serial primary key not null,
country varchar(25),
city varchar(25),
street varchar(50),
house_number varchar(5)
);

create table swiatwoblerow.customer(
id serial primary key not null,
username varchar(50) not null unique,
password varchar(85) not null,
first_name varchar(50) not null,
last_name varchar(50) not null,
address integer references swiatwoblerow.customer_address(id),
email varchar(50) not null unique,
telephone varchar(15)
);

create table swiatwoblerow.roles(
id serial primary key not null,
name varchar(30) not null unique
);

create table swiatwoblerow.customer_roles(
customer_id integer references swiatwoblerow.customer(id),
role_id integer references swiatwoblerow.roles(id) 
);

///////////////////////////////////

create table swiatwoblerow.product_details(
id serial primary key not null,
customer_id integer references swiatwoblerow.customer(id),
thumbsup integer not null,
thumbsdown integer not null,
quantity integer not null,
message varchar(5000)
);

create table swiatwoblerow.product(
id serial primary key not null,
name varchar(1000) not null,
price double precision not null,
localization varchar(255) not null,
created_at timestamp not null,
product_details_id integer references swiatwoblerow.product_details(id)
);

create table swiatwoblerow.condition(
id serial primary key not null,
name varchar(30) not null unique
);

create table swiatwoblerow.product_condition(
product_id integer references swiatwoblerow.product(id),
condition_id integer references swiatwoblerow.condition(id) 
);

create table swiatwoblerow.opinion(
id serial primary key not null,
customer_id integer references swiatwoblerow.customer(id),
message varchar(1000) not null,
created_at timestamp not null,
product_details_id integer references swiatwoblerow.product_details(id)
);

create table swiatwoblerow.product_details_thumbs_up(
product_details_id integer references swiatwoblerow.product_details(id),
customer_id integer references swiatwoblerow.customer(id)
);

create table swiatwoblerow.product_details_thumbs_down(
product_details_id integer references swiatwoblerow.product_details(id),
customer_id integer references swiatwoblerow.customer(id)
);


insert into swiatwoblerow.customer_address values(0,NULL,NULL,NULL,NULL);

insert into swiatwoblerow.roles values(1,'ROLE_USER');
insert into swiatwoblerow.roles values(2,'ROLE_MODERATOR');
insert into swiatwoblerow.roles values(3,'ROLE_ADMIN');

insert into swiatwoblerow.customer_roles values(4,3);

insert into swiatwoblerow.condition values(1,'NEW');
insert into swiatwoblerow.condition values(2,'DAMAGED');
insert into swiatwoblerow.condition values(3,'USED');