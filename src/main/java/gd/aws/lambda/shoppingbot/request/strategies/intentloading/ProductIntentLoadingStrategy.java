package gd.aws.lambda.shoppingbot.request.strategies.intentloading;

import java.util.Map;

import gd.aws.lambda.shoppingbot.intents.ProductIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

public class ProductIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readProductSlots(request, slots, ProductIntent.Slot.Make, ProductIntent.Slot.ProductType, ProductIntent.Slot.ChassisSeries,
                         ProductIntent.Slot.ChassisNo);
    }
}
