package gd.aws.lambda.shoppingbot.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<Integer, List<String>> getAvailableProducts() {
        Map<Integer, List<String>> productMap = new HashMap<>();
        for (Product p : productRepository.getAllProducts()) {
            Integer key = p.getUnitPrices()
                           .get(0)
                           .getType();
            List<String> products = productMap.get(key);
            if (products == null) {
                products = new ArrayList<>();
                products.add(p.getProductId());
                productMap.put(key, products);
            } else {
                products.add(p.getProductId());
            }

        }
        return productMap;
    }

    @Override
    public void saveProduct(Product p) {
        productRepository.save(p);
    }
}
