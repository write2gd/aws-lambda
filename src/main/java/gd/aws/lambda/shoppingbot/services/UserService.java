package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.User;

public interface UserService {
    User getUserById(String userId);
    User getUserByName(String userName);
    void save(User user);
}
