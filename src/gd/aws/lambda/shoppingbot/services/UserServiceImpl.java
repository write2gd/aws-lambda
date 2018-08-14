package gd.aws.lambda.shoppingbot.services;


import java.util.List;

import com.sun.istack.internal.NotNull;

import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.repositories.UserRepository;

public class UserServiceImpl extends Service implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(@NotNull UserRepository userRepository, @NotNull Logger logger) {
        super(logger);
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User getUserByFacebookId(String facebookId) {
        return userRepository.getUserByFacebookId(facebookId);
    }

    @Override
    public User getUserByName(String firstName, String lastName) {
        List<User> users = userRepository.getUserByName(firstName, lastName);
        if(users.size() == 0)
            return null;
        return users.get(0);//TODO: implement a strategy to specify users by address or other criteria
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
