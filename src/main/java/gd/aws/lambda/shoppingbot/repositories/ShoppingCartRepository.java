package gd.aws.lambda.shoppingbot.repositories;


import gd.aws.lambda.shoppingbot.entities.ShoppingCart;

import java.util.List;

public interface ShoppingCartRepository extends Repository {
    List<ShoppingCart> getAllShoppingCarts();
    ShoppingCart getShoppingCartByUserId(String userId);
    void save(ShoppingCart cart);
    void delete(ShoppingCart cart);
}
