package gd.aws.lambda.shoppingbot.repositories;


public interface RepositoryFactory{
    UserRepository createUserRepository();
    InvoiceRepository createShoppingCartRepository();
    OrderRepository createOrderRepository();
    ProductRepository createProductRepository();

    void shutdown();
}
