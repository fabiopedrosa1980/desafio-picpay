create table if not exists user_type(
    id serial primary key,
    name varchar not null
);

create table if not exists users(
    id serial primary key,
    name varchar not null ,
    document varchar not null UNIQUE,
    email varchar not null UNIQUE,
    password varchar not null,
    user_type int references user_type(id),
    balance decimal default 0
);

create table if not exists transfers(
    id serial primary key,
    amount decimal not null,
    payer int references users(id),
    payee int references users(id)
);



