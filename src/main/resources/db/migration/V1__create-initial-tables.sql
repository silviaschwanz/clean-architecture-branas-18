CREATE TABLE account (
    account_id UUID not null PRIMARY KEY,
    name VARCHAR(254) not null,
    email VARCHAR(254) not null UNIQUE,
    cpf VARCHAR(11) not null,
    car_plate VARCHAR(254),
    is_passenger boolean not null,
    is_driver boolean not null,
    password_algorithm VARCHAR(254) not null
);

CREATE TABLE ride (
    ride_id UUID not null PRIMARY KEY,
    passenger_id UUID not null,
    driver_id UUID,
    status VARCHAR(20) not null,
    fare DOUBLE PRECISION,
    from_lat DOUBLE PRECISION not null,
    from_long DOUBLE PRECISION not null,
    to_lat DOUBLE PRECISION not null,
    to_long DOUBLE PRECISION not null,
    distance DOUBLE PRECISION,
    date TIMESTAMP not null
);

CREATE TABLE position (
    position_id UUID not null PRIMARY KEY,
    ride_id UUID not null,
    lat DOUBLE PRECISION not null,
    long DOUBLE PRECISION not null,
    date TIMESTAMP not null
);

