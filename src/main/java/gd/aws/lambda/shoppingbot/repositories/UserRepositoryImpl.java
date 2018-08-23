package gd.aws.lambda.shoppingbot.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import gd.aws.lambda.shoppingbot.entities.User;

public class UserRepositoryImpl extends RepositoryImpl implements UserRepository {
    public final static String TableName = "User";

    public final class Attr {
        public final static String UserId = "user_id";
        public final static String UserName = "user_name";
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
    public List<User> getUserByName(String userName) {
        String attrValueUserName = ":v_user_name";
        String filterExpression = String.format("%s=%s", Attr.UserName, attrValueUserName);
        Map<String, AttributeValue> expressionValueMap = new HashMap<>();
        expressionValueMap.put(attrValueUserName, new AttributeValue().withS(userName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                                                                            .withExpressionAttributeValues(expressionValueMap);
        return dbMapper.scan(User.class, scanExpression);
    }

    @Override
    public void save(User user) {
        dbMapper.save(user);
    }
}
