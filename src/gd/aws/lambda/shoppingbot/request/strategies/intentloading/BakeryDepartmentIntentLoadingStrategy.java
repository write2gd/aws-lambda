package gd.aws.lambda.shoppingbot.request.strategies.intentloading;


import gd.aws.lambda.shoppingbot.intents.BakeryDepartmentIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class BakeryDepartmentIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, BakeryDepartmentIntent.Slot.Product, BakeryDepartmentIntent.Slot.Amount, BakeryDepartmentIntent.Slot.Unit);
    }
}
