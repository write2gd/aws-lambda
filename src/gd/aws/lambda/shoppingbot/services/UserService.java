package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.User;

public interface UserService {
    User getUserById(String userId);
    User getUserByFacebookId(String facebookId);
    User getUserByName(String firstName, String lastName);
    void save(User user);
}
