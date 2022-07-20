Drop table IF EXISTS FILM_GENRE;
Drop table IF EXISTS FILM_LIKES;
Drop table IF EXISTS FILMS_DIRECTORS;
Drop table IF EXISTS FILMS;
Drop table IF EXISTS RATING_MPA;
Drop table IF EXISTS FRIENDS;
Drop table IF EXISTS FRIENDSHIP;
Drop table IF EXISTS GENRE;
Drop table IF EXISTS USERS;
Drop table IF EXISTS DIRECTORS;
Drop table IF EXISTS REVIEWS;

CREATE TABLE IF NOT EXISTS rating_mpa
(
    rating_mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(5)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          varchar(50),
    description   varchar(200),
    releaseDate   date,
    duration      INTEGER,
    rate      INTEGER,
    rating_mpa_id INTEGER REFERENCES rating_mpa (rating_mpa_id)
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id int PRIMARY KEY,
    name     varchar(14)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  int REFERENCES films (film_id),
    genre_id int REFERENCES genre (genre_id),
    primary key (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar(20),
    login    varchar(20),
    name     varchar(20),
    birthday date
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id int REFERENCES films (film_id),
    user_id int REFERENCES users (user_id),
    primary key (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friendship
(
    friendship_id int PRIMARY KEY,
    status        varchar(11)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id    int REFERENCES users (user_id),
    friend_id  int REFERENCES users (user_id),
    friendship int REFERENCES friendship (friendship_id),
    primary key (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name varchar(69)
);

CREATE TABLE IF NOT EXISTS FILMS_DIRECTORS
(
    director_id int references DIRECTORS (director_id) on delete cascade,
    film_id int references films (film_id) on delete cascade,
    primary key (film_id, director_id)
);

create table IF NOT EXISTS REVIEWS
(
    REVIEW_ID   INTEGER auto_increment,
    CONTENT     CHARACTER VARYING(500) not null,
    IS_POSITIVE BOOLEAN                not null,
    USER_ID     INTEGER                not null,
    USEFUL      INTEGER default 0,
    FILM_ID     INTEGER                not null,

        primary key (REVIEW_ID),

        foreign key (FILM_ID) references FILMS
            on delete cascade,

        foreign key (USER_ID) references USERS
            on delete cascade
);