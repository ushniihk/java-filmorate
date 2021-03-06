INSERT INTO FRIENDSHIP (FRIENDSHIP_ID, status) VALUES (1, 'CONFIRMED');
INSERT INTO FRIENDSHIP (friendship_id, status) VALUES (2, 'UNCONFIRMED');

INSERT INTO RATING_MPA (RATING_MPA_ID, NAME) values (1, 'G');
INSERT INTO RATING_MPA (RATING_MPA_ID, NAME) values (2, 'PG');
INSERT INTO RATING_MPA (RATING_MPA_ID, NAME) values (3, 'PG-13');
INSERT INTO RATING_MPA (RATING_MPA_ID, NAME) values (4, 'R');
INSERT INTO RATING_MPA (RATING_MPA_ID, NAME) values (5, 'NC-17');

INSERT INTO GENRE (GENRE_ID, NAME) VALUES (1, 'Комедия');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (2, 'Драма');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (3, 'Мультфильм');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (4, 'Триллер');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (5, 'Документальный');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (6, 'Боевик');

INSERT INTO USERS (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (1, 'ONE@mail.ru', 'first', '', '2001-01-27');
INSERT INTO USERS (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (2, 'TWO@mail.ru', 'second', '', '2002-01-27');
INSERT INTO USERS (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (3, 'THREE@mail.ru', 'third', '', '2003-01-27');

INSERT INTO FRIENDS (USER_ID, FRIEND_ID) values (1, 2);
INSERT INTO FRIENDS (USER_ID, FRIEND_ID) values (1, 3);
INSERT INTO FRIENDS (USER_ID, FRIEND_ID) values (2, 3);

INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, RATING_MPA_ID)
VALUES (1, 'matrix', 'about neo', '2001-01-27', 100, 1, 1);
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, RATING_MPA_ID)
VALUES (2, 'matrix2', 'about two', '2002-01-27', 100, 1, 1);
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, RATING_MPA_ID)
VALUES (3, 'matrix3', 'about three', '2003-01-27', 100, 1, 1);



