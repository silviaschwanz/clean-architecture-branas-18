CREATE TABLE account (
    account_id UUID not null PRIMARY KEY,
    name VARCHAR(254) not null,
    email VARCHAR(254) not null,
    cpf VARCHAR(11) not null,
    car_plate VARCHAR(254),
    is_passenger boolean not null,
    is_driver boolean not null,
    password VARCHAR(254) not null,
    password_algorithm VARCHAR(254) not null
);

CREATE TABLE ride (
    ride_id UUID not null PRIMARY KEY,
    passenger_id UUID not null,
    driver_id UUID not null,
    status VARCHAR(20) not null,
    fare NUMERIC(18) not null,
    from_lat NUMERIC(18) not null,
    from_long NUMERIC(18) not null,
    to_lat NUMERIC(18) not null,
    to_long NUMERIC(18) not null,
    distance NUMERIC(18),
    date TIMESTAMP not null
);

CREATE TABLE position (
    position_id UUID not null PRIMARY KEY,
    ride_id UUID not null,
    lat NUMERIC(18) not null,
    long NUMERIC(18) not null,
    date TIMESTAMP not null
);

