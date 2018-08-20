package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.repositories.UserRepository;

import java.util.List;


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
    public User getUserByFacebookId(String facebookId) {
        //return userRepository.getUserByFacebookId(facebookId);
        User user =new User();
        user.setFirstName("Gouranga");
        user.setLastName("Das");
        user.setUserId(facebookId);
        user.setFacebookId("write2gd");
        return user;
    }

    @Override
    public User getUserByName(String firstName, String lastName) {
        List<User> users = userRepository.getUserByName(firstName, lastName);
        if(users.size() == 0)
            return null;
        return users.get(0);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
