DROP TABLE IF EXISTS users, categories, compilations, events, events_compilations, requests, friend_requests CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT,
    lon FLOAT
);
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(250) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN     NOT NULL,
    title  VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS events
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation        VARCHAR(2000) NOT NULL,
    description       VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    paid              BOOLEAN       NOT NULL,
    participant_limit  BIGINT       NOT NULL,
    request_moderation BOOLEAN       NOT NULL,
    title             VARCHAR(120)  NOT NULL,
    state             VARCHAR(10)       NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    initiator_id      BIGINT REFERENCES users (id) ON DELETE CASCADE,
    category_id     BIGINT REFERENCES categories (id) ON DELETE CASCADE,
    location_id       BIGINT REFERENCES locations (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS events_compilations
(
    events_id       BIGINT REFERENCES events (id) ON DELETE CASCADE,
    compilations_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE,
    status       VARCHAR(255) NOT NULL,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    event_id    BIGINT REFERENCES events (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS friend_requests
(
	id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	requester_id BIGINT NOT NULL REFERENCES users (id),
    friend_id    BIGINT NOT NULL REFERENCES users (id),
    status       VARCHAR(9) NOT NULL,
    created      TIMESTAMP NOT NULL,
    CONSTRAINT uq_friendship UNIQUE(requester_id, friend_id)
);