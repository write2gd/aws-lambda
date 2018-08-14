package gd.aws.lambda.shoppingbot.repositories;


public interface RepositoryFactory{
    UserRepository createUserRepository();
    ShoppingCartRepository createShoppingCartRepository();
    OrderRepository createOrderRepository();
    ProductRepository createProductRepository();

    void shutdown();
}
