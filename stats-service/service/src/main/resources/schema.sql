DROP TABLE IF EXISTS stats;

create table if not exists stats
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app       VARCHAR(255)                NOT NULL,
    uri       VARCHAR(512)                NOT NULL,
    ip        VARCHAR(255)                NOT NULL,
    timestamp TIMESTAMP NOT NULL
);