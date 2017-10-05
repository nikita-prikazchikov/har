import java.util.HashMap;

public class HarDetails {

    private HashMap<String, Integer> orderSteps = new HashMap<String, Integer>();

    public HarDetails() {
        prepareOrderSteps();
    }

    private void prepareOrderSteps(){
        orderSteps.put("customer wants to 'CHECKOUT' from add to bag popup", 2);
        orderSteps.put("When customer applies promo code '${Coupons.percentOff.coupon1.promoCode}' and do 'continue shopping'", 5);
        orderSteps.put("When customer apply '${testexecution.rewardCertificates.coupon1.code}' reward with serial number '${testexecution.rewardCertificates.coupon1.serialNo}' and do 'continue shopping'", 4);
        orderSteps.put("customer click on 'apply code' link of '1' available coupon and verifies its functioning", 5);
    }

    HashMap<String, BddFileDetails> getHarDetails() {
        HashMap<String, BddFileDetails> scenarios = new HashMap<>();

        String prefix = "scenarios.yoda.checkout.yk.yoda_cc_smoke_extended_yk.bdd.";
        String scenarioName;

        scenarioName = "CartandCheckout_Smoke_1_ProdAddToCart_Validate add to bag for sephora product";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.SEPHORA_ITEM.items1.webId}'",
                                "And customer selects required product options",
                                "And customer adds product to bag",
                                "And customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "And customer closes the current session"},
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_2_Validate order summary for coupons and reward applied  in cart before checkout";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given fetch working 'rewardCertificates.coupon' rewards '1' quantity",
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.mattress.items2.webId}'",
                                "When customer selects required product options",
                                "Then customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "When customer applies promo code '${Coupons.percentOff.coupon1.promoCode}' and do 'continue shopping'",
                                "When customer apply '${testexecution.rewardCertificates.coupon1.code}' reward with serial number '${testexecution.rewardCertificates.coupon1.serialNo}' and do 'continue shopping'",
                                "Then customer validate pricing summery on shopping bag",
                                "Then customer closes the current session"
                                },
                        orderSteps));


        return scenarios;
    }
}
