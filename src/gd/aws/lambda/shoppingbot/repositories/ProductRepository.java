package gd.aws.lambda.shoppingbot.repositories;


import gd.aws.lambda.shoppingbot.entities.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();
    Product getByProductId(String productId);
    void save(Product product);
}
