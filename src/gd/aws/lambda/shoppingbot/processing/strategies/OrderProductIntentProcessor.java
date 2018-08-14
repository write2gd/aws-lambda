package gd.aws.lambda.shoppingbot.processing.strategies;

import gd.aws.lambda.shoppingbot.common.OperationValueResult;
import gd.aws.lambda.shoppingbot.common.OperationValueResultImpl;

import gd.aws.lambda.shoppingbot.entities.Product;
import gd.aws.lambda.shoppingbot.entities.ShoppingCart;
import gd.aws.lambda.shoppingbot.entities.ShoppingCartItem;
import gd.aws.lambda.shoppingbot.entities.UnitPrice;
import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.ProductService;
import gd.aws.lambda.shoppingbot.services.ShoppingCartService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class OrderProductIntentProcessor extends UserSessionIntentProcessor {
    private ShoppingCartService shoppingCartService;
    private ProductService productService;

    public OrderProductIntentProcessor(ShoppingCartService shoppingCartService, UserService userService, ProductService productService, Logger logger) {
        super(userService, logger);
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if(!lexRequest.requestedAmountIsSet() || !lexRequest.requestedProductIsSet())
            return createLexErrorResponse(lexRequest, "Product or amount are not specified.");

        OperationValueResult<User> gettingUserResult = getUser(lexRequest);
        if(gettingUserResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUserResult.getErrorsAsString());
        User user = gettingUserResult.getValue();

        OperationValueResult<Product> gettingProductResult = getProduct(lexRequest);
        if(gettingProductResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingProductResult.getErrorsAsString());
        Product product = gettingProductResult.getValue();

        String unit = lexRequest.getRequestedUnit();
        OperationValueResult<UnitPrice> gettingUnitPriceResult = getUnitPrice(product, unit);
        if(gettingUnitPriceResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUnitPriceResult.getErrorsAsString());
        UnitPrice unitPrice = gettingUnitPriceResult.getValue();

        ShoppingCart shoppingCart = getOrCreateShoppingCart(user);
        ShoppingCartItem cartItem = shoppingCart.getItemByProduct(product.getProductId());
        String message = updateCartItemWithRequested(cartItem, unitPrice.getUnit(), unitPrice.getPrice(),
                                                    Double.parseDouble(lexRequest.getRequestedAmount()));
        shoppingCartService.save(shoppingCart);
        return LexResponseHelper.createLexResponse(lexRequest, message,
                                                    DialogAction.Type.Close, DialogAction.FulfillmentState.Fulfilled);
    }

    private OperationValueResult<UnitPrice> getUnitPrice(Product product, String unit) {
        UnitPrice unitPrice = product.getUnitPriceFor(unit == null ? "pieces" : unit);
        OperationValueResult<UnitPrice> operationResult = new OperationValueResultImpl<>();
        if(unitPrice == null)
            operationResult.addError(String.format("I'm sorry - a price for %s is not found", unit == null ? "this" : unit));
        else
            operationResult.setValue(unitPrice);
        return operationResult;
    }

    private OperationValueResult<Product> getProduct(LexRequest lexRequest) {
        String productName = lexRequest.getRequestedProduct();
        Product product = productService.getByProductId(productName);
        OperationValueResultImpl<Product> operationResult = new OperationValueResultImpl<>();
        if(product == null)
            operationResult.addError(String.format("I'm sorry - the product \"%s\" is not found", productName));
        else
            operationResult.setValue(product);
        return operationResult;
    }

    private String updateCartItemWithRequested(ShoppingCartItem cartItem, String unit, double unitPrice, double amount) {
        String existingUnit = cartItem.getUnit();
        String productName = cartItem.getProduct();
        if (existingUnit == null) {
            cartItem.setAmount(amount);
            cartItem.setUnit(unit);
            cartItem.setPrice(unitPrice);
            return String.format("Put %s (%s) of %s, price: %s.", amount, unit, productName, unitPrice);
        }
        if (existingUnit.equals(unit)) {
            cartItem.addAmount(amount);
            cartItem.setPrice(unitPrice);
            return String.format("Added %s (%s) of %s.", amount, existingUnit, productName);
        }
        Double existingAmount = cartItem.getAmount();
        cartItem.setAmount(amount);
        cartItem.setUnit(unit);
        cartItem.setPrice(unitPrice);
        return String.format("Replaced %s (%s) of %s with %s (%s), price: %s.",
                                existingAmount, existingUnit, productName,
                                amount, unit, unitPrice);
    }

    private ShoppingCart getOrCreateShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(user.getUserId());
        if (shoppingCart != null)
            return shoppingCart;
        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCart;
    }
}
