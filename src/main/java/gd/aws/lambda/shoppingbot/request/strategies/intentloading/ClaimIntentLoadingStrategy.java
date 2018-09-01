package gd.aws.lambda.shoppingbot.request.strategies.intentloading;


import gd.aws.lambda.shoppingbot.intents.ClaimIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class ClaimIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, ClaimIntent.Slot.ClaimType, ClaimIntent.Slot.ClaimId, ClaimIntent.Slot.Unit);
    }
}
