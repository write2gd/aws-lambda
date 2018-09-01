package gd.aws.lambda.shoppingbot.processing;

import java.util.LinkedHashMap;
import java.util.Map;

import gd.aws.lambda.shoppingbot.intents.ClaimIntent;
import gd.aws.lambda.shoppingbot.intents.OrderIntent;
import gd.aws.lambda.shoppingbot.intents.GreetingsIntent;
import gd.aws.lambda.shoppingbot.intents.CampaignIntent;
import gd.aws.lambda.shoppingbot.intents.InvoiceIntent;
import gd.aws.lambda.shoppingbot.intents.ProductIntent;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.processing.strategies.OrderIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.GreetingsIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.IntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.ProductIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.InvoiceIntentProcessor;
import gd.aws.lambda.shoppingbot.processing.strategies.UnsupportedIntentProcessor;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.services.OrderService;
import gd.aws.lambda.shoppingbot.services.ProductService;
import gd.aws.lambda.shoppingbot.services.InvoiceService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class UchpBotProcessor {
    private final IntentProcessor unsupportedIntentProcessor;
    private final Map<String, IntentProcessor> processingStrategies = new LinkedHashMap<>();

    public UchpBotProcessor(UserService userService, InvoiceService invoiceService, ProductService productService, OrderService orderService,
               Logger logger) {
        unsupportedIntentProcessor = new UnsupportedIntentProcessor(logger);
        ProductIntentProcessor productIntentProcessor = new ProductIntentProcessor(invoiceService, userService, productService, logger);
        processingStrategies.put(ClaimIntent.Name, productIntentProcessor);
        processingStrategies.put(CampaignIntent.Name, productIntentProcessor);
        processingStrategies.put(ProductIntent.Name, productIntentProcessor);
        processingStrategies.put(GreetingsIntent.Name, new GreetingsIntentProcessor(userService, productService, logger));
        processingStrategies.put(InvoiceIntent.Name, new InvoiceIntentProcessor(invoiceService, userService, logger));
        processingStrategies.put(OrderIntent.Name, new OrderIntentProcessor(invoiceService, orderService, userService, logger));
    }

    public LexResponse Process(LexRequest lexRequest) {
        return getProcessingStrategy(lexRequest.getIntentName()).Process(lexRequest);
    }

    private IntentProcessor getProcessingStrategy(String intentName) {
        return processingStrategies.getOrDefault(intentName, unsupportedIntentProcessor);
    }
}
