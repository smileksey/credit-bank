create table client (
                        client_id uuid primary key,
                        last_name varchar(30) not null,
                        first_name varchar(30) not null,
                        middle_name varchar(30),
                        birth_date date not null,
                        email varchar not null,
                        gender varchar not null,
                        marital_status varchar not null,
                        dependent_amount int,
                        passport_id jsonb not null,
                        employment_id jsonb not null,
                        account_number varchar
);