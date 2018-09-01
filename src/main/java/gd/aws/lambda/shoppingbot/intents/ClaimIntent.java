package gd.aws.lambda.shoppingbot.intents;


public class ClaimIntent {
    public static final String Name = "Claims";

    public class Slot {
        public static final String ClaimId = "claimId";
        public static final String ClaimType = "claimType";
        public static final String Unit = "claimUnit";
    }
}
