package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCartByUserId(String userId);
    void save(ShoppingCart cart);
    void delete(ShoppingCart shoppingCart);
}
