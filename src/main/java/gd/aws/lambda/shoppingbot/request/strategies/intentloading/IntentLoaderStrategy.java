package gd.aws.lambda.shoppingbot.request.strategies.intentloading;

import java.util.Map;

import gd.aws.lambda.shoppingbot.request.LexRequest;

public abstract class IntentLoaderStrategy {

    protected static void readDepartmentSlots(LexRequest request, Map<String, Object> slots, String type, String id, String unit) {
        request.setProduct(getSlotValueFor(slots, type, null));
        request.setId(getSlotValueFor(slots, id, null));
        request.setUnit(getSlotValueFor(slots, unit, null));
    }

    protected static void readProductSlots(LexRequest request, Map<String, Object> slots, String productType, String make, String chassisSeries,
               String chassisNo) {
        request.setProduct(getSlotValueFor(slots, productType, null));
        request.setId(getSlotValueFor(slots, make, null));
        request.setUnit(getSlotValueFor(slots, chassisSeries, null));
        request.setUnit(getSlotValueFor(slots, chassisNo, null));
    }

    protected static String getSlotValueFor(Map<String, Object> slots, String productSlotName, String defaultValue) {
        String slotValue = (String) slots.get(productSlotName);
        return slotValue != null ? slotValue : defaultValue;
    }

    public abstract void load(LexRequest request, Map<String, Object> slots);
}
