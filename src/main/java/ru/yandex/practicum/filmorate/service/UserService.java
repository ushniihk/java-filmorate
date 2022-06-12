package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;


@Service
public class UserService {

    public Integer addFriend(User user1, User user2) {
        user1.addFriend(user2.getId());
        user2.addFriend(user1.getId());
        return user1.getFriends().size();
    }

    public Integer deleteFriend(User user1, User user2) {
        user1.removeFriend(user2.getId());
        user2.removeFriend(user1.getId());
        return user1.getFriends().size();
    }

}
