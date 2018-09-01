package gd.aws.lambda.shoppingbot.repositories;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import gd.aws.lambda.shoppingbot.entities.Invoice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class InvoiceRepositoryImpl extends RepositoryImpl implements InvoiceRepository {
    public final static String TableName = "Invoice";
    public final class Attr {
        public final static String UserId = "user_id";
    }

    public InvoiceRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public void save(Invoice cart) {
        cart.setUpdatedOn(ZonedDateTime.now(ZoneId.of("UTC")).toString());
        dbMapper.save(cart);
    }

    @Override
    public List<Invoice> getAllShoppingCarts() {
        return dbMapper.scan(Invoice.class, new DynamoDBScanExpression());
    }

    public Invoice getShoppingCartByUserId(String userId) {
        return dbMapper.load(Invoice.class, userId);
    }

    @Override
    public void delete(Invoice cart) {
        dbMapper.delete(cart);
    }
}
