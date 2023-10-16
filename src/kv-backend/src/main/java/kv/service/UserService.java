package kv.service;

import kv.mapper.UserMapper;
import kv.pojo.dbo.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByUsername(String username) {
        return userMapper.selectByPrimaryKey(username);
    }

    public void updateUser(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    public boolean login(User user) {
        User userDb = getUserByUsername(user.getUsername());

        if (userDb == null) return false;

        if (user.getPassword().equals(userDb.getPassword())) {  // First login
            userDb.setPassword(BCrypt.hashpw(userDb.getPassword(), BCrypt.gensalt()));
            updateUser(userDb);
        }

        return BCrypt.checkpw(user.getPassword(), userDb.getPassword());
    }

    public void changePassword(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        updateUser(user);
    }
}
