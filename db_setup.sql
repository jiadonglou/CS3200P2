
-- IMDB Database
create table actor (id integer primary key, fname varchar(50), lname varchar(50), gender char);
create table movie (id integer primary key, name varchar(200), year integer);
create table directors (id integer primary key, fname varchar(50), lname varchar(50));
create table casts (pid integer, mid integer, role varchar(100),
foreign key(pid) references actor,
foreign key(mid) references movie);
create table movie_directors (did integer, mid integer,
foreign key(did) references directors,
foreign key(mid) references movie);
create table genre (mid integer, genre varchar(50));


create table Location(location_id int, city varchar(100), state varchar(50), primary key(location_id));

-- Movie(mid primary key) a list of movies:
create table Movie(movie_id int primary key);

-- Plan table:
-- plan id PK, plan_name, max_rental, fee
create table Plan(plan_id int,
                 plan_name varchar(100),
                 max_rentals int,
                 fee int,
                  PRIMARY KEY(plan_id));

--------------------------------------------------------------------------
-- customer databse


-- Customer id PK, username, password, phone number, location id, plan id
create table Customer(customer_id int,
                     login varchar(20) unique,
                     password varchar(50) NOT NULL,
                     phone_number bigint,
                     location_id int references Location(location_id),
                     plan_id int references Plan(plan_id),
                     firstname varchar(50),
                     lastname varchar(50),
                     primary key(customer_id));

create table ActiveRental(movie_id int references Movie(movie_id),
                         customer_id int references Customer(customer_id),
                         dateRented date,
                         primary key(customer_id,movie_id));
                         
                         
-- RentalHistory table:
create table RentalHistory(movie_id int references Movie(movie_id),
                          customer_id int references customer(customer_id),
                          primary key(customer_id, movie_id));




---------
-- Movie:
insert into Movie values (12345);
insert into Movie values (13579);
insert into Movie values (24680);
insert into Movie values (54321);
insert into Movie values (56789);
insert into Movie values (98765);
insert into Movie values (32573);
insert into Movie values (107448);


-- Plan:
-- Basic Plan: ID - 01; Max Rentals - 3 movies; Cost - $8
-- Super Plan: ID - 02; Max Rentals - 8 movies; Cost - $12
-- Premium Plan: ID - 03; Max Rentals - 15 movies; Cost - $20
insert into Plan values (01, 'Basic', 3, 8);
insert into Plan values (02, 'Super', 5, 10);
insert into Plan values (03, 'Premium', 15, 20);



-- Customer:
insert into Customer values(1111, 'James', '000000', 6172850000,617,01, 'James', 'Smith');
insert into Customer values(2222, 'John', '111111', 7183960000,718,02, 'John', 'Johnson');
insert into Customer values(3333, 'Robert', '222222', 3234650000,323,03, 'Robert', 'Williams');


-- Location:
insert into Location values (617, 'Boston', 'MA' );
insert into Location values (718, 'New York City', 'NY' );
insert into Location values (323, 'Los Angeles', 'CA' );
insert into Location values (773, 'Chicago', 'IL' );
insert into Location values (305, 'Miami', 'FL' );
insert into Location values (215, 'Philadelphia', 'PA' );
insert into Location values (512, 'Austin', 'TX' );
insert into Location values (504, 'New Orleans', 'LA' );

--Active Rental:
insert into ActiveRental values(32573, 1111,'2018-02-20');
insert into ActiveRental values(107448, 2222,'2018-02-21');


-- Rental History:
insert into RentalHistory(12345, 1111);
insert into RentalHistory(13579, 1111);
insert into RentalHistory(98765, 2222);
insert into RentalHistory(24680, 3333);


-- To run the program
cd /where/you/unzipped/the/starter/code
export CLASSPATH=.:postgresql-9.2-1002.jdbc4.jar
javac -g VideoStore.java Query.java
java VideoStore James 000000 