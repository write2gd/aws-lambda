package gd.aws.lambda.shoppingbot.intents;

public class ProductIntent {
    public static final String Name = "Products";

    public class Slot {
        public static final String ProductType = "productType";
        public static final String Make = "make";
        public static final String ChassisSeries = "chassisSeries";
        public static final String ChassisNo = "chassisNo";
    }
}