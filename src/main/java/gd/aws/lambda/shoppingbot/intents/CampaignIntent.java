package gd.aws.lambda.shoppingbot.intents;


public class CampaignIntent {
    public static final String Name = "Campaigns";

    public class Slot {
        public static final String CampaignId = "campaignId";
        public static final String CampaignType = "campaignType";
        public static final String Unit = "campaignUnit";
    }
}
