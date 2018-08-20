package gd.aws.lambda.shoppingbot.processing;


import java.util.LinkedHashMap;
import java.util.Map;

import gd.aws.lambda.shoppingbot.intents.BakeryDepartmentIntent;
import gd.aws.lambda.shoppingbot.intents.CompleteOrderIntent;
import gd.aws.lambda.shoppingbot.intents.GreetingsIntent;
import gd.aws.lambda.shoppingbot.intents.MilkDepartmentIntent;
import gd.aws.lambda.shoppingbot.intents.ReviewShoppingCartIntent;
import gd.aws.lambda.shoppingbot.intents.VegetableDepartmentIntent;
import gd.aws.lambda.shoppingbot.log.CompositeLogger;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.processing.strategies.CompleteOrderIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.GreetingsIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.IntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.ProductIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.ReviewShoppingCartIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.UnsupportedIntentProcessor;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.services.OrderService;
import gd.aws.lambda.shoppingbot.services.ProductService;
import gd.aws.lambda.shoppingbot.services.ShoppingCartService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class GShopBotProcessor {
    private CompositeLogger logger = new CompositeLogger();
    private final IntentProcessor unsupportedIntentProcessor;
    private final Map<String, IntentProcessor> processingStrategies = new LinkedHashMap<>();

    public GShopBotProcessor(UserService userService, ShoppingCartService shoppingCartService, ProductService productService, OrderService orderService, Logger logger) {
        unsupportedIntentProcessor = new UnsupportedIntentProcessor(logger);
        ProductIntentProcessor productIntentProcessor = new ProductIntentProcessor(shoppingCartService, userService, productService, logger);
        processingStrategies.put(BakeryDepartmentIntent.Name, productIntentProcessor);
        processingStrategies.put(MilkDepartmentIntent.Name, productIntentProcessor);
        processingStrategies.put(VegetableDepartmentIntent.Name, productIntentProcessor);
        processingStrategies.put(GreetingsIntent.Name, new GreetingsIntentProcessor(userService, logger));
        processingStrategies.put(ReviewShoppingCartIntent.Name, new ReviewShoppingCartIntentProcessor(shoppingCartService, userService, logger));
        processingStrategies.put(CompleteOrderIntent.Name, new CompleteOrderIntentProcessor(shoppingCartService, orderService, userService, logger));
    }

    public LexResponse Process(LexRequest lexRequest) {
        logger.log(String.format("Request Intent is: %s", lexRequest.getIntentName()));
        return getProcessingStrategy(lexRequest.getIntentName()).Process(lexRequest);
    }

    private IntentProcessor getProcessingStrategy(String intentName) {
        return processingStrategies.getOrDefault(intentName, unsupportedIntentProcessor);
    }
}
