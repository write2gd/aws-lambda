package gd.aws.lambda.shoppingbot.repositories;


import gd.aws.lambda.shoppingbot.entities.User;

import java.util.List;

public interface UserRepository extends Repository {
    List<User> getAllUsers();
    User getUserById(String userId);
    List<User> getUserByName(String userName);
    void save(User user);
}
