package gd.aws.lambda.shoppingbot.entities;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import gd.aws.lambda.shoppingbot.entities.converters.InvoiceItemConverter;

import java.util.List;

@DynamoDBTable(tableName = "Invoice")
public class Invoice extends OrderInfo<InvoiceItem> {

    @Override
    @DynamoDBHashKey(attributeName = "user_id")
    public String getUserId() {
        return super.getUserId();
    }

    @Override
    @DynamoDBAttribute(attributeName = "updated_on")
    public String getUpdatedOn() {
        return super.getUpdatedOn();
    }

    @Override
    @DynamoDBAttribute(attributeName = "items")
    @DynamoDBTypeConverted(converter = InvoiceItemConverter.class)
    public List<InvoiceItem> getItems() {
        return super.getItems();
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "userId='" + userId + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
