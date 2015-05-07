CREATE TABLE IF NOT EXISTS artist (
    id integer NOT NULL,
    name text NOT NULL,
    print text NOT NULL,
    birthday date,
    country char(3),
    location varchar,
    url varchar,
    remarks varchar,
    "timestamp" timestamp
);