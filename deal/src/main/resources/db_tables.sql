create table client (
                        client_id uuid primary key,
                        last_name varchar,
                        first_name varchar,
                        middle_name varchar,
                        birth_date date,
                        email varchar,
                        gender varchar,
                        marital_status varchar,
                        dependent_amount int,
                        passport_id jsonb,
                        employment_id jsonb,
                        account_number varchar
);

create table credit (
                        credit_id uuid primary key,
                        amount decimal,
                        term int,
                        monthly_payment decimal,
                        rate decimal,
                        psk decimal,
                        payment_schedule jsonb,
                        insurance_enabled boolean,
                        salary_client boolean,
                        credit_status varchar
);

create table statement (
                           statement_id uuid primary key,
                           client_id uuid references client(client_id) unique,
                           credit_id uuid references credit(credit_id) unique,
                           status varchar,
                           creation_date timestamp,
                           applied_offer jsonb,
                           sign_date timestamp,
                           ses_code varchar,
                           status_history jsonb
);