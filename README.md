# java-filmorate
Template repository for Filmorate project.

Ссылка на диаграмму https://app.quickdatabasediagrams.com/#/d/OlBLkG

![а вот и картинка](https://github.com/ushniihk/java-filmorate/blob/main/QuickDBD-export.png?raw=true)

    public Collection<Film> findAll() (SELECT * FROM Film)

    public Film findFilm(@PathVariable Integer id) (SELECT * FROM Film WHERE film_id = "id")
    
    public Collection<Film> getTopFilms (SELECT * FROM film LEFT JOIN film_likes ON film.film_id = film_likes.film_id GROUP BY film.name HAVING MAX(COUNT(user_id))
    
    public Collection<User> findAll() (SELECT * FROM user)
    
    public User getUser(@PathVariable Integer id) (SELECT * FROM user WHERE user_id = "id")
    
    public Collection<User> getFriends(@PathVariable Integer id) (SELECT * FROM user WHERE user_id IN (SELECT user.user_id FROM user JOIN friends ON user.user_id = friends.user_id) WHERE user_id = "id")
    
    public Collection<User> showCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) (SELECT * 
    FROM user 
    WHERE user_id IN (SELECT user.user_id 
    FROM user JOIN friends ON user.user_id = friends.user_id 
    WHERE user_id = "id") 
    AND user_id IN (SELECT user.user_id 
    FROM user JOIN friends ON user.user_id = friends.user_id) 
    WHERE user_id = "another_id") 
    
