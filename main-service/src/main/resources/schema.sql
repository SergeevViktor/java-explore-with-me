DROP TABLE IF EXISTS users, categories, locations, events, participation_requests, compilations, compilations_events;

CREATE TABLE IF NOT EXISTS users ( 
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
	name VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories ( 
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations ( 
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
	lat REAL NOT NULL,
	lon REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
	annotation VARCHAR(2000) NOT NULL,
	description VARCHAR(7000) NOT NULL,
	event_date TIMESTAMP NOT NULL,
	paid Boolean NOT NULL,
	request_moderation Boolean NOT NULL,
	participant_limit INT NOT NULL,
	title VARCHAR(120) NOT NULL,
	state VARCHAR(10) NOT NULL,
	created_on TIMESTAMP NOT NULL,
	published_on TIMESTAMP
	category_id BIGINT NOT NULL REFERENCES categories (id),
	location_id BIGINT NOT NULL REFERENCES locations (id),
	initiator_id BIGINT NOT NULL REFERENCES users (id),
);

CREATE TABLE IF NOT EXISTS participation_requests (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
	created TIMESTAMP NOT NULL,
	status VARCHAR(10) NOT NULL,
	event_id BIGINT NOT NULL REFERENCES events (id),
	requester_id BIGINT NOT NULL REFERENCES users (id),
	CONSTRAINT uq_request UNIQUE(event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
	pinned Boolean NOT NULL,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations_events (
	compilation_id BIGINT REFERENCES compilations (id),
	event_id BIGINT REFERENCES events (id),
	PRIMARY KEY (compilation_id, event_id)
)



