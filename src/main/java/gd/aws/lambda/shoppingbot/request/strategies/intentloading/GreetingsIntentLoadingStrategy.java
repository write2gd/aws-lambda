package gd.aws.lambda.shoppingbot.request.strategies.intentloading;

import java.util.Map;

import gd.aws.lambda.shoppingbot.intents.GreetingsIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

public class GreetingsIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        request.setUserName(getSlotValueFor(slots, GreetingsIntent.Slot.UserName, null));
    }
}
