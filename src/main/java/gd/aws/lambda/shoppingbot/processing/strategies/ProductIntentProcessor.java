package gd.aws.lambda.shoppingbot.processing.strategies;

import java.util.Arrays;

import gd.aws.lambda.shoppingbot.common.OperationValueResult;
import gd.aws.lambda.shoppingbot.common.OperationValueResultImpl;
import gd.aws.lambda.shoppingbot.entities.Invoice;
import gd.aws.lambda.shoppingbot.entities.InvoiceItem;
import gd.aws.lambda.shoppingbot.entities.Product;
import gd.aws.lambda.shoppingbot.entities.UnitPrice;
import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.CompositeLogger;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.InvoiceService;
import gd.aws.lambda.shoppingbot.services.ProductService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class ProductIntentProcessor extends UserSessionIntentProcessor {
    private CompositeLogger logger = new CompositeLogger();
    private InvoiceService invoiceService;
    private ProductService productService;

    public ProductIntentProcessor(InvoiceService invoiceService, UserService userService, ProductService productService, Logger logger) {
        super(userService, logger);
        this.invoiceService = invoiceService;
        this.productService = productService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if (!lexRequest.requestedAmountIsSet() && !lexRequest.requestedProductIsSet())
            return createLexErrorResponse(lexRequest, "Product or amount are not specified.");

        OperationValueResult<User> gettingUserResult = getUser(lexRequest);
        if (gettingUserResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUserResult.getErrorsAsString());
        User user = gettingUserResult.getValue();
        OperationValueResult<Product> gettingProductResult = getProduct(lexRequest);
        if (gettingProductResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingProductResult.getErrorsAsString());
        Product product = gettingProductResult.getValue();

        String unit = lexRequest.getUnit();
        OperationValueResult<UnitPrice> gettingUnitPriceResult = getUnitPrice(product, "unit");
        if (gettingUnitPriceResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUnitPriceResult.getErrorsAsString());
        UnitPrice unitPrice = gettingUnitPriceResult.getValue();
        Invoice invoice = getOrCreateShoppingCart(user);
        InvoiceItem cartItem = invoice.getItemByProduct(product.getProductId());
        String requestedAmount = lexRequest.getId();
        double amount = Double.parseDouble(requestedAmount);
        String message = updateCartItemWithRequested(cartItem, unitPrice.getUnit(), unitPrice.getPrice(), amount);
        invoiceService.save(invoice);
        return LexResponseHelper.createLexResponse(lexRequest, message, DialogAction.Type.CLOSE_TYPE,
                                                   DialogAction.FulfillmentState.FULFILLMENT_STATE_FULFILLED);
    }

    private OperationValueResult<UnitPrice> getUnitPrice(Product product, String unit) {
        UnitPrice unitPrice = product.getUnitPriceFor(unit == null ? "pieces" : unit);
        OperationValueResult<UnitPrice> operationResult = new OperationValueResultImpl<>();
        if (unitPrice == null)
            operationResult.addError(String.format("I'm sorry - a price for %s is not found. Can you tell some different unit", unit == null ? "this" : unit));
        else
            operationResult.setValue(unitPrice);
        return operationResult;
    }

    public static UnitPrice createUnitPrice(Double price, String unit, String[] unitForms, int type) {
        UnitPrice unitPrice = new UnitPrice();
        unitPrice.setPrice(price);
        unitPrice.setUnit(unit);
        unitPrice.setUnitForms(Arrays.asList(unitForms));
        unitPrice.setType(type);
        return unitPrice;
    }

    private void addUnitPriceFor(Product bread, double price, String unit, String[] unitForms, int type) {
        bread.getUnitPrices()
             .add(createUnitPrice(price, unit, unitForms, type));
    }

    private Product createProduct(String productName) {
        Product product = new Product();
        product.setProductId(productName);
        return product;
    }

    private OperationValueResult<Product> getProduct(LexRequest lexRequest) {
        String productName = lexRequest.getType();
        //initializeProducts();
        Product product = productService.getByProductId(productName.toLowerCase());
        OperationValueResultImpl<Product> operationResult = new OperationValueResultImpl<>();
        if (product == null)
            operationResult.addError(String.format("I'm sorry - the product \"%s\" is not available. Would you like to order something else?", productName));
        else
            operationResult.setValue(product);
        return operationResult;
    }

    private void initializeProducts() {
        final String kilograms = "kilograms";
        final String[] unitFormsForKilograms = new String[] { "kilogram", kilograms, "kilo", "kilos", "kg" };
        final String pieces = UnitPrice.UnitPieces;
        final String[] unitFormsForPieces = new String[] { "piece", pieces };

        Product product = createProduct("bus");
        addUnitPriceFor(product, 1.5, "unit", new String[] { "unit", "units" }, 1);
        addUnitPriceFor(product, 1.5, pieces, unitFormsForPieces, 1);
        productService.saveProduct(product);
        product = createProduct("truck");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 1);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 1);
        productService.saveProduct(product);

        product = createProduct("Loader");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 1);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 1);
        productService.saveProduct(product);
        product = createProduct("Car");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 1);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 1);
        productService.saveProduct(product);
        product = createProduct("vehicle");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 2);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 2);
        productService.saveProduct(product);

        product = createProduct("parts");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 2);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 2);
        productService.saveProduct(product);
        product = createProduct("campaign");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 2);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 2);
        productService.saveProduct(product);
        product = createProduct("service");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 2);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 2);
        productService.saveProduct(product);
        product = createProduct("coverage");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 2);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 2);
        productService.saveProduct(product);

        product = createProduct("s-service");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 3);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 3);
        productService.saveProduct(product);

        product = createProduct("c-recall");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 3);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 3);
        productService.saveProduct(product);

        product = createProduct("k-commercial");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 3);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 3);
        productService.saveProduct(product);
        product = createProduct("p-policy");
        addUnitPriceFor(product, 1, pieces, unitFormsForPieces, 3);
        addUnitPriceFor(product, 1, "unit", new String[] { "unit", "units" }, 3);
        productService.saveProduct(product);
    }

    private String updateCartItemWithRequested(InvoiceItem cartItem, String unit, double unitPrice, double amount) {
        String existingUnit = cartItem.getUnit();
        String productName = cartItem.getProduct();
        double totalAmount = amount * unitPrice;
        if (existingUnit == null) {
            cartItem.setAmount(amount);
            cartItem.setUnit(unit);
            cartItem.setPrice(unitPrice);
            return String.format("%s (%s) of %s with total price: %s added to Shopping Cart.", amount, unit, productName, totalAmount);
        }
        if (existingUnit.equals(unit)) {
            cartItem.addAmount(amount);
            cartItem.setPrice(unitPrice);
            return String.format("Added %s (%s) of %s to Shopping Cart.", amount, existingUnit, productName);
        }
        Double existingAmount = cartItem.getAmount();
        cartItem.setAmount(amount);
        cartItem.setUnit(unit);
        cartItem.setPrice(unitPrice);
        return String.format("Replaced %s (%s) of %s with %s (%s), price: %s from existing Shopping Cart.", existingAmount, existingUnit, productName, amount,
                             unit, totalAmount);
    }

    private Invoice getOrCreateShoppingCart(User user) {
        Invoice invoice = invoiceService.getShoppingCartByUserId(user.getUserId());
        if (invoice != null)
            return invoice;
        invoice = new Invoice();
        invoice.setUser(user);
        return invoice;
    }
}
