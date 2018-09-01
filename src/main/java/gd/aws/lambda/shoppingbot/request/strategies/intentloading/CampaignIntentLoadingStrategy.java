package gd.aws.lambda.shoppingbot.request.strategies.intentloading;


import gd.aws.lambda.shoppingbot.intents.CampaignIntent;
import gd.aws.lambda.shoppingbot.request.LexRequest;

import java.util.Map;

public class CampaignIntentLoadingStrategy extends IntentLoaderStrategy {
    @Override
    public void load(LexRequest request, Map<String, Object> slots) {
        readDepartmentSlots(request, slots, CampaignIntent.Slot.CampaignType, CampaignIntent.Slot.CampaignId, CampaignIntent.Slot.Unit);
    }
}
