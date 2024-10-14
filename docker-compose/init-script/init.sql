create table IF NOT EXISTS user(
	id int NOT NULL AUTO_INCREMENT,
	name varchar(255),
	primary key (id)
);

insert into user(name) values ('fahim'), ('fahad');

