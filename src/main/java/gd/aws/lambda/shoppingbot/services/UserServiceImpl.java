package gd.aws.lambda.shoppingbot.services;

import java.util.List;

import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.repositories.UserRepository;

public class UserServiceImpl extends Service implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, Logger logger) {
        super(logger);
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User getUserByName(String userName) {
        List<User> users = userRepository.getUserByName(userName);
        if (users.size() == 0)
            return null;
        return users.get(0);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
