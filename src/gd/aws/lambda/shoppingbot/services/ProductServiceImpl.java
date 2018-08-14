package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.Product;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.repositories.ProductRepository;

public class ProductServiceImpl extends Service implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository, Logger logger) {
        super(logger);
        this.productRepository = productRepository;
    }

    @Override
    public Product getByProductId(String productId) {
        return productRepository.getByProductId(productId);
    }
}
