-- Directors
CREATE TABLE directors
(
    name character varying NOT NULL,
    PRIMARY KEY (name)
);

-- Genres
CREATE TABLE genres
(
    name character varying NOT NULL,
    PRIMARY KEY (name)
);

-- Films
CREATE TABLE films
(
    id          character varying NOT NULL,
    PRIMARY KEY (id),
    title       character varying NOT NULL,
    year        smallint          NOT NULL,
    genre       character varying NOT NULL,
    director    character varying NOT NULL,
    poster_link character varying NOT NULL,
    description character varying NOT NULL
);
ALTER TABLE films
    ADD FOREIGN KEY (director) REFERENCES directors (name);
ALTER TABLE films
    ADD FOREIGN KEY (genre) REFERENCES genres (name);


-- Users
CREATE TABLE users
(
    id int8 NOT NULL,
    PRIMARY KEY (id)
);
-- User Favorite Genres
CREATE TABLE user_favorite_genres
(
    user_id int4              NOT NULL,
    genre   character varying NOT NULL
);

ALTER TABLE user_favorite_genres
    ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_favorite_genres
    ADD FOREIGN KEY (genre) REFERENCES genres (name);
ALTER TABLE user_favorite_genres
    ADD CONSTRAINT user_to_genre PRIMARY KEY (user_id, genre);

-- User Watched Films
CREATE TABLE user_watched_films
(
    user_id int4 NOT NULL,
    film_id character varying NOT NULL
);
ALTER TABLE user_watched_films
    ADD FOREIGN KEY (film_id) REFERENCES films (id);
ALTER TABLE user_watched_films
    ADD FOREIGN KEY (user_id) REFERENCES users (id)