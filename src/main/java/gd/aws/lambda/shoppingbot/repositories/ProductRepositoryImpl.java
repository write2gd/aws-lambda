package gd.aws.lambda.shoppingbot.repositories;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import gd.aws.lambda.shoppingbot.entities.Product;
import gd.aws.lambda.shoppingbot.entities.UnitPrice;

import java.util.Arrays;
import java.util.List;

public class ProductRepositoryImpl extends RepositoryImpl implements ProductRepository {
    public final class Attr {

        public final static String ProductId = "product_id";
        public final static String NameForms = "name_forms";
        public final static String Price = "prices";
    }
    public final static String TableName = "Product";

    public ProductRepositoryImpl(AmazonDynamoDB dynamodb, DynamoDBMapper dbMapper) {
        super(dynamodb, dbMapper);
    }

    @Override
    public void save(Product product) {
        dbMapper.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
       // trialsave("Apple");
        return dbMapper.scan(Product.class, new DynamoDBScanExpression());
    }

    @Override
    public Product getByProductId(String productId) {
        //trialsave("Milk");
        return dbMapper.load(Product.class, productId);
    }

    private UnitPrice createUnitPrice(Double price, String unit, String[] unitForms) {
        UnitPrice unitPrice = new UnitPrice();
        unitPrice.setPrice(price);
        unitPrice.setUnit(unit);
        unitPrice.setUnitForms(Arrays.asList(unitForms));
        return unitPrice;
    }
    private void trialsave(String name){
        final String[] unitFormsForKilograms = new String[]{"kilogram", "kilograms", "kilo", "kilos", "kg"};
        final String[] unitFormsForPieces = new String[]{"piece",  UnitPrice.UnitPieces};
        Product product1 = new Product();
        product1.setProductId(name);
        product1.getUnitPrices()
                .add(createUnitPrice(1.0, "kilograms", unitFormsForKilograms));
        product1.getUnitPrices()
                .add(createUnitPrice(0.15,  UnitPrice.UnitPieces, unitFormsForPieces));
        dbMapper.save(product1);
    }
}
