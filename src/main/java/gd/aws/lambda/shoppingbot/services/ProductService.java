package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.Product;

public interface ProductService {
    Product getByProductId(String productId);
}
