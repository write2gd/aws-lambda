package gd.aws.lambda.shoppingbot.services;


import java.util.List;
import java.util.Map;

import gd.aws.lambda.shoppingbot.entities.Product;

public interface ProductService {
    Product getByProductId(String productId);
    Map<Integer,  List<String>> getAvailableProducts();
    void saveProduct(Product p);
}
