package gd.aws.lambda.shoppingbot.request.strategies.intentloading;


import gd.aws.lambda.shoppingbot.intents.VegetableDepartmentIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class VegetableDepartmentIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, VegetableDepartmentIntent.Slot.Product, VegetableDepartmentIntent.Slot.Amount, VegetableDepartmentIntent.Slot.Unit);
    }
}
