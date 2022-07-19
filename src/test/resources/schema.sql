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

CREATE TABLE IF NOT EXISTS rating_mpa
(
    rating_mpa_id INTEGER,
    name varchar(5)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id       INTEGER,
    name          varchar(50),
    description   varchar(200),
    releaseDate   date,
    duration      INTEGER,
    rate      INTEGER,
    rating_mpa_id INTEGER
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id int,
    name     varchar(14)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  int,
    genre_id int
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER,
    email    varchar(20),
    login    varchar(20),
    name     varchar(20),
    birthday date
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id int,
    user_id int
);

CREATE TABLE IF NOT EXISTS friendship
(
    friendship_id int,
    status        varchar(11)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id    int,
    friend_id  int,
    friendship int
);

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    director_id INTEGER,
    director_name varchar(69)
);

CREATE TABLE IF NOT EXISTS FILMS_DIRECTORS
(
    director_id int,
    film_id int,
    primary key (film_id, director_id)
);