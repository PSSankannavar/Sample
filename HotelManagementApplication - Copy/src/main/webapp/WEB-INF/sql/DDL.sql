create database HotelManagementApplication;
use HotelManagementApplication;


CREATE TABLE CityInfo (
	city_id int NOT NULL AUTO_INCREMENT,
	city_name varchar(255) NOT NULL,
	PRIMARY KEY (city_id)
);

CREATE TABLE HotelInfo (
	  hotel_id int NOT NULL AUTO_INCREMENT,
	  hotel_name varchar(255) NOT NULL,
	  city_id int NOT NULL,
	  number_of_rooms int NOT NULL,
	  star_rating DOUBLE,
	  tariff DOUBLE,
	  PRIMARY KEY (hotel_id),
	  FOREIGN KEY (city_id) REFERENCES CityInfo(city_id)
);

insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelBangalore1',1,10,5,500);
insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelBangalore2',1,20,2,1000);
insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelBangalore4',1,25,2,200);
insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelBangalore8',1,15,2,300);
insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelBangalore12',1,40,2,400);
insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelBangalore14',1,20,2,500);

insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelMysore3',2,10,4,600);
insert into HotelInfo(hotel_name,city_id,number_of_rooms,star_rating,tariff) values ('HotelMysore5',2,20,2,2000);

CREATE TABLE RoomInfo (
	room_number int NOT NULL AUTO_INCREMENT,
	room_cost int(11),
	hotel_id int,
	PRIMARY KEY (room_number),
	FOREIGN KEY (hotel_id) REFERENCES HotelInfo(hotel_id)
);

CREATE TABLE CustomerInfo (
	  customer_id int NOT NULL AUTO_INCREMENT,
	  customer_name varchar(255) NOT NULL,
	  city_id int NOT NULL,
	  number_of_rooms int NOT NULL,
	  check_out_date DATE NOT NULL,
	  check_in_date DATE NOT NULL,
	  hotel_id int NOT NULL,
	  PRIMARY KEY (customer_id),
	  FOREIGN KEY (city_id) REFERENCES CityInfo(city_id),
	  FOREIGN KEY (hotel_id) REFERENCES HotelInfo(hotel_id)
);

insert into cityinfo(city_name) values ('Bangalore');
insert into cityinfo(city_name) values ('Mysore');

drop table CustomerInfo;
drop table RoomInfo;
drop table HotelInfo;
drop table CityInfo;

Select sum(number_of_rooms) from CustomerInfo where hotel_id = 1 and city_id = 1  ;
Select * from HotelInfo where hotel_id = 6;
Select * from RoomInfo;
Select * from HotelInfo where city_id = 1  order by tariff asc LIMIT 5 ;
Select * from CityInfo;
update HotelInfo set hotel_id = '1' where hotel_id = '11';
update HotelInfo set hotel_id = '2' where hotel_id = '12';
update HotelInfo set hotel_id = '3' where hotel_id = '13';
update HotelInfo set hotel_id = '4' where hotel_id = '14';
update HotelInfo set hotel_id = '5' where hotel_id = '15';
update HotelInfo set hotel_id = '6' where hotel_id = '16';
update HotelInfo set hotel_id = '7' where hotel_id = '17';
update HotelInfo set hotel_id = '8' where hotel_id = '18';

Select * from CustomerInfo


update CityInfo set city_id = '2' where city_id = '4'
delete from HotelInfo where hotel_name = 'Hotel2';

delete  from HotelInfo
delete from CityInfo
delete from CustomerInfo;
