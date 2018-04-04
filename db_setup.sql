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


-- Customer table:
-- Customer id PK, username, password, phone number, location id, plan id
create table Customer(customer_id int,
                     login varchar(20) unique,
                     password varchar(50) NOT NULL,
                     phone_number int,
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