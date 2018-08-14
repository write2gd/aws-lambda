package gd.aws.lambda.shoppingbot.repositories;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import gd.aws.lambda.shoppingbot.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl extends RepositoryImpl implements UserRepository {
    public final static String TableName = "User";

    public final class Attr {
        public final static String UserId = "user_id";
        public final static String FirstName = "first_name";
        public final static String LastName = "last_name";
        public final static String Address = "address";
    }

    public UserRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public List<User> getAllUsers() {
        return dbMapper.scan(User.class, new DynamoDBScanExpression());
    }

    @Override
    public User getUserById(String userId) {
        return dbMapper.load(User.class, userId);
    }

    @Override
    public User getUserByFacebookId(String facebookId) {
        List<User> users = scan(User.class, "facebook_id", facebookId);
        return (users.size() == 0) ? null : users.get(0);
    }

    @Override
    public List<User> getUserByName(String firstName, String lastName) {
        String attrValueFirstName = ":v_first_name";
        String attrValueLastName = ":v_last_name";
        String filterExpression = String.format("%s=%s and %s=%s", Attr.FirstName, attrValueFirstName,
                                                                   Attr.LastName, attrValueLastName);
        Map<String, AttributeValue> expressionValueMap = new HashMap<>();
        expressionValueMap.put(attrValueFirstName, new AttributeValue().withS(firstName));
        expressionValueMap.put(attrValueLastName, new AttributeValue().withS(lastName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                                                .withFilterExpression(filterExpression)
                                                .withExpressionAttributeValues(expressionValueMap);
        return dbMapper.scan(User.class, scanExpression);
    }

    @Override
    public void save(User user) {
        dbMapper.save(user);
    }
}
