package gd.aws.lambda.shoppingbot.entities;

import static org.apache.http.util.TextUtils.isEmpty;

import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import gd.aws.lambda.shoppingbot.request.UserIdType;

@DynamoDBTable(tableName = "User")
public class User {
    private String userId;
    private String userName;
    private String address;

    public User() {
        setUserId(UUID.randomUUID()
                      .toString());
    }

    @DynamoDBHashKey(attributeName = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [address=");
        builder.append(address);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", userName=");
        builder.append(userName);
        builder.append("]");
        return builder.toString();
    }

    public boolean sameNamesAs(String userName) {
        return userName.equals(getUserName());
    }

    public boolean hasUserId(String userId, UserIdType userIdType) {
        if (isEmpty(userId) || userIdType == UserIdType.Undefined)
            return false;
        return userId.equals(getUserId());
    }
}
