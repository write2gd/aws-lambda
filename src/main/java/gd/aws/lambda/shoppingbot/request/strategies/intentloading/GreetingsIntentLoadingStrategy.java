package gd.aws.lambda.shoppingbot.request.strategies.intentloading;


import gd.aws.lambda.shoppingbot.intents.GreetingsIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class GreetingsIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        request.setFirstName(getSlotValueFor(slots, GreetingsIntent.Slot.FirstName, null));
        request.setLastName(getSlotValueFor(slots, GreetingsIntent.Slot.LastName, null));
    }
}
