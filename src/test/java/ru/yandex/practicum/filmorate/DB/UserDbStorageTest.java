package ru.yandex.practicum.filmorate.DB;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void shouldFindUserById() throws NotFoundParameterException {
        assertEquals(userStorage.get(1).get().getEmail(), "ONE@mail.ru");
        assertEquals(userStorage.get(3).get().getLogin(), "third");
        assertEquals(userStorage.get(2).get().getId(), 2);
    }

    @Test
    public void shouldFindAllUsers() throws NotFoundParameterException {
        assertTrue(userStorage.findAll().contains(userStorage.get(2).get()));
        assertTrue(userStorage.findAll().contains(userStorage.get(1).get()));
        assertTrue(userStorage.findAll().contains(userStorage.get(3).get()));
    }

    @Test
    public void shouldCreateAndUpdateUser() throws CreatingException, UpdateException {
        User user = new User(0, "mail@mail.ru", "login", new ArrayList<>(), "", Date.valueOf("2000-01-27"));
        userStorage.create(user);
        assertEquals(userStorage.findAll().size(), 4);
        User user2 = new User(0, "mail@mail.ru", "login", new ArrayList<>(), "igar", Date.valueOf("2000-01-27"));
        userStorage.update(user2);
        assertEquals(userStorage.findAll().size(), 4);
    }

    @Test
    public void shouldShowFriends() throws NotFoundParameterException {
        assertEquals(userStorage.showAllFriends(1).size(), 2);
    }

    @Test
    public void shouldADDFriendAndDelete() throws NotFoundParameterException {
        assertEquals(userStorage.showAllFriends(3).size(), 0);
        userStorage.addFriend(3, 1);
        assertEquals(userStorage.showAllFriends(3).size(), 1);
        userStorage.deleteFriend(3, 1);
        assertEquals(userStorage.showAllFriends(3).size(), 0);
    }

    @Test
    public void shouldShowCommonFriends() throws NotFoundParameterException {
        assertEquals(userStorage.showCommonFriends(1, 2).size(), 1);
        List<User> list = List.copyOf(userStorage.showCommonFriends(1, 2));
        assertEquals(list.get(0).getLogin(), "third");
    }

}
