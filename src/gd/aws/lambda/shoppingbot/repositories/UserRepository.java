package gd.aws.lambda.shoppingbot.repositories;


import gd.aws.lambda.shoppingbot.entities.User;

import java.util.List;

public interface UserRepository extends Repository {
    List<User> getAllUsers();
    User getUserById(String userId);
    User getUserByFacebookId(String facebookId);
    List<User> getUserByName(String firstName, String lastName);
    void save(User user);
}
