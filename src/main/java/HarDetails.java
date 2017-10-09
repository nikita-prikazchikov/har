import java.util.HashMap;

class HarDetails {

    private HashMap<String, Integer> orderSteps = new HashMap<String, Integer>();

    HarDetails() {
        prepareOrderSteps();
    }

    private void prepareOrderSteps() {
        orderSteps.put(".*customer wants to '[^']+' from add to bag popup.*", 1);
        orderSteps.put(".*customer selects '[^']+' from added to shopping bag popup.*", 1);
        orderSteps.put(".*customer applies promo code '[^']+' and do '[^']+'.*", 1);
        orderSteps.put(".*customer apply '[^']+' reward with serial number '[^']+' and do 'continue shopping'.*", 1);
        orderSteps.put(".*customer click on 'apply code' link of '[^']+' available coupon and verifies its functioning.*", 1);
        orderSteps.put(".*customer opts to checkout as.*", 1);
        orderSteps.put(".*customer continues to payment section of the checkout page.*", 1);
        orderSteps.put(".*customer enters '[^']+' in shipping form.*", 1);
        orderSteps.put(".*customer continues to review section of the checkout page.*", 1);
        orderSteps.put(".*customer '[^']+' payment with credit card payment method using detail '[^']+' as '[^']+' user.*", 1);
    }

    HashMap<String, BddFileDetails> getHarDetails() {
        HashMap<String, BddFileDetails> scenarios = new HashMap<>();

//        String prefix = "scenarios.yoda.checkout.yk.yoda_cc_smoke_extended_yk.bdd.";
        String prefix = "";
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

        scenarioName = "CartandCheckout_Smoke_3_Validate_Greatest Saving Coupon";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.mattress.items2.webId}'",
                                "When customer selects required product options",
                                "Then customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should be able to see find best coupon link on shopping bag page",
                                "Then customer goes to find a coupon page and verifies find coupons modal is opened	 ",
                                "Then customer validates find best coupon modal displayed on Shopping bag page",
                                "Then customer click on 'apply code' link of '1' available coupon and verifies its functioning",
                                "Then customer validate 'save' message for '1' coupon on coupon modal",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_5_Reg User + Regular Item + STH + PLCC";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "And store into 'testexecution.new_account_data'",
                                "Then customer logs-out from application",
                                "When customer starts JCPenney session",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "Then customer selects required product options",
                                "Then customer adds product to bag",
                                "And verify items in checkout popup",
                                "And customer selects 'checkout' from added to shopping bag popup",
                                "Then customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as '${testexecution.new_account_data}'",
                                "And customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "And customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.jcpenney' as 'registered' user",
                                "And customer provides 'shipping address' as billing address for 'create' block as 'registered' user	",
                                "And customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "And customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_6_Reg User + Regular Item + STH + Visa";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "And store into 'testexecution.new_account_data'",
                                "Then customer logs-out from application",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "Then customer selects required product options",
                                "Then customer adds product to bag",
                                "And verify items in checkout popup",
                                "And customer selects 'checkout' from added to shopping bag popup",
                                "Then customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as '${testexecution.new_account_data}'",
                                "And customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "And customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.visa' as 'registered' user",
                                "And customer provides 'shipping address' as billing address for 'create' block as 'registered' user	",
                                "And customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "And customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_7_Reg User + Gift Card Item + STH + Mastercard";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "And store into 'testexecution.new_account_data'",
                                "Then customer adds 'Customers.HomeOfficeUser' as shipping address and 'decline' to make this default for checkout",
                                "And customer closes the current browser session",
                                "When customer starts JCPenney session",
                                "When customer searches for '${Products.GIFT_CARD.items1.webId}'",
                                "And customer selects required product options",
                                "And customer fills to 'infostretch' and from 'jcp' on product details page",
                                "Then customer adds product to bag",
                                "And verify items in checkout popup",
                                "And customer selects 'checkout' from added to shopping bag popup",
                                "Then customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as '${testexecution.new_account_data}'",
                                "And customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.mastercard' as 'registered' user",
                                "And customer provides 'shipping address' as billing address for 'create' block as 'registered' user	",
                                "And customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "And customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_8_Guest User + regular Item + STH + JCPenney card";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer selects '1' as quantity",
                                "When customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "When customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "And customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "And customer continues to payment section of the checkout page",
                                "When customer 'create' payment with credit card payment method using detail 'CreditCards.jcpenney' as 'guest' user",
                                "And customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "And customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_9_Guest User + Truckable Item + STH + JCP Mastercard";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer searches for '${Products.mattress.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer selects '1' as quantity",
                                "When customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "When customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "And customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "And customer continues to payment section of the checkout page",
                                "When customer 'create' payment with credit card payment method using detail 'CreditCards.jcpmastercard' as 'guest' user",
                                "And customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "And customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_10_Guest User + jewelry Item + STH + Amex";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.JEWELRY_WITH_CARE_PLAN.items2.webId}'",
                                "When customer selects required product options",
                                "Then customer selects '1' as quantity",
                                "When customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "When customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "And customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "And customer continues to payment section of the checkout page",
                                "When customer 'create' payment with credit card payment method using detail 'CreditCards.amex' as 'guest' user",
                                "And customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "And customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_11_Reg User + Regular Item + STS + PLCC";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "And store into 'testexecution.dt_Dt001'",
                                "Then customer adds 'Customers.HomeOfficeUser' as shipping address and 'decline' to make this default for checkout",
                                "Then customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer adds product to bag",
                                "Then verify items in checkout popup",
                                "When customer selects 'checkout' from added to shopping bag popup",
                                "And customer click on checkout button in shopping bag page",
                                "Then customer select ship to store '${Customers.HomeOfficeUser.zip}' and enter customer details 'Customers.HomeOfficeUser'		",
                                "Then customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.jcpenney' as 'registered' user",
                                "Then customer provides 'Customers.HomeOfficeUser' as billing address for 'create' block as 'registered' user",
                                "Then customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "When customer submit the order	",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_12_Reg User + Regular Item + STS + Visa";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "And store into 'testexecution.dt_Dt001'",
                                "Then customer adds 'Customers.HomeOfficeUser' as shipping address and 'decline' to make this default for checkout",
                                "Then customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer adds product to bag",
                                "Then verify items in checkout popup",
                                "When customer selects 'checkout' from added to shopping bag popup",
                                "And customer click on checkout button in shopping bag page",
                                "Then customer select ship to store '${Customers.HomeOfficeUser.zip}' and enter customer details 'Customers.HomeOfficeUser'		",
                                "Then customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.visa' as 'registered' user",
                                "Then customer provides 'Customers.HomeOfficeUser' as billing address for 'create' block as 'registered' user",
                                "Then customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "When customer submit the order	",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_13_Reg User + Regular Item + STS + Mastercard";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "And store into 'testexecution.dt_Dt001'",
                                "Then customer adds 'Customers.HomeOfficeUser' as shipping address and 'decline' to make this default for checkout",
                                "Then customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer adds product to bag",
                                "Then verify items in checkout popup",
                                "When customer selects 'checkout' from added to shopping bag popup",
                                "And customer click on checkout button in shopping bag page",
                                "Then customer select ship to store '${Customers.HomeOfficeUser.zip}' and enter customer details 'Customers.HomeOfficeUser'		",
                                "Then customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.mastercard' as 'registered' user",
                                "Then customer provides 'Customers.HomeOfficeUser' as billing address for 'create' block as 'registered' user",
                                "Then customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "When customer submit the order	",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_14_Guest User + regular Item + STS + Discover";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "Then customer selects required product options",
                                "Then customer adds product to bag",
                                "Then verify items in checkout popup",
                                "Then customer selects 'checkout' from added to shopping bag popup",
                                "Then customer should see personalized shopping bag",
                                "Then customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "Then customer search pick up store from shipping page with zipcode '${Customers.HomeOfficeUser.zip}'",
                                "Then customer selects store '1' from pick up store list",
                                "Then customer enters 'Customers.HomeOfficeUser' in shipping pickup form",
                                "And customer continues to payment section of the checkout page from store section",
                                "When customer 'create' payment with credit card payment method using detail 'CreditCards.discover' as 'guest' user",
                                "Then customer provides 'Customers.HomeOfficeUser' as billing address for 'create' block as 'guest' user",
                                "And customer click on payment continue button for 'create' block",
                                "Then customer should be able to review the order",
                                "Then customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_15_Guest User + regular Item + STS + JCP Mastercard";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer selects '1' as quantity",
                                "When customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "When customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "Then customer search pick up store from shipping page with zipcode '${Customers.HomeOfficeUser.zip}'",
                                "Then customer selects store '1' from pick up store list",
                                "Then customer enters 'Customers.HomeOfficeUser' in shipping pickup form",
                                "And customer continues to payment section of the checkout page from store section",
                                "When customer 'create' payment with credit card payment method using detail 'CreditCards.jcpmastercard' as 'guest' user",
                                "Then customer provides 'Customers.HomeOfficeUser' as billing address for 'create' block as 'guest' user",
                                "And customer click on payment continue button for 'create' block",
                                "Then customer should be able to review the order",
                                "Then customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_16_Guest User + Mixed Bag + STH + Amex";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "Then customer selects required product options",
                                "Then customer adds product to bag",
                                "Then verify items in checkout popup",
                                "Then customer selects 'continue' from added to shopping bag popup",
                                "When customer searches for '${Products.mattress.items1.webId}'",
                                "Then customer selects required product options",
                                "Then customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "Then customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "Then customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "Then customer continues to payment section of the checkout page",
                                "When customer 'create' payment with credit card payment method using detail 'CreditCards.amex' as 'guest' user",
                                "Then customer provides 'Customers.HomeOfficeUser' as billing address for 'create' block as 'guest' user",
                                "And customer click on payment continue button for 'create' block",
                                "Then customer should be able to review the order",
                                "Then customer submit the order",
                                "Then customer should able to verify order confirmation page",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_17_GuestUser_MixedItemAdded_PaymentJCPMasterCard";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session	",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "Then customer selects required product options",
                                "And customer adds product to bag",
                                "Then customer selects 'continue' from added to shopping bag popup	",
                                "When customer searches for '${Products.mattress.items1.webId}'",
                                "Then customer selects required product options",
                                "When customer selects 'AVAILABLE ADDITIONAL' protection plan on pdp",
                                "And customer adds product to bag",
                                "Then customer selects 'checkout' from added to shopping bag popup",
                                "And customer click on checkout button in shopping bag page	",
                                "Then customer opts to checkout as 'guest'",
                                "Then customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "Then customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.jcpmastercard' as 'guest' user",
                                "Then customer provides 'shipping address' as billing address for 'create' block as 'guest' user",
                                "Then customer continues to review section of the checkout page",
                                "Then customer declines the offer on promo finance modal",
                                "Then customer should be able to review the order",
                                "When customer submit the order",
                                "And customer should able to verify order confirmation page"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_18_Express Checkout PO";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer creates new account with 'random' for checkout",
                                "Then customer adds 'Customers.HomeOfficeUser' as shipping address and 'opts' to make this default for checkout",
                                "Then customer adds 'CreditCards.visa' as payment method with billing address 'saved' and 'opts' to make this default for checkout",
                                "When customer searches for '${Products.REGULAR.items1.webId}'",
                                "Then customer selects required product options    ",
                                "And customer adds product to bag",
                                "And verify items in checkout popup",
                                "When customer selects 'checkout' from added to shopping bag popup        ",
                                "When customer click on checkout button in shopping bag page ",
                                "Then customer continues to payment section of the checkout page     ",
                                "Then customer enters saved credit card in payment section as 'CreditCards.visa' ",
                                "Then customer continues to review section of the checkout page     ",
                                "Then customer should be able to review the order",
                                "Then customer submit the order",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_22_Guest User + Regular Item + Express PayPal";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session	",
                                "Then customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer adds product to bag",
                                "When customer selects 'checkout' from added to shopping bag popup	",
                                "Then customer should see personalized shopping bag	",
                                "Then customer validates 'express' paypal button functionality",
                                "Then customer closes the current session"
                        },
                        orderSteps));

        scenarioName = "CartandCheckout_Smoke_26_Place order by editing STH to STS";
        scenarios.put(
                scenarioName + ".har",
                new BddFileDetails(
                        scenarioName + ".har",
                        prefix + scenarioName,
                        new String[]{
                                "Given customer starts JCPenney session",
                                "Then customer searches for '${Products.REGULAR.items1.webId}'",
                                "When customer selects required product options",
                                "Then customer selects '1' as quantity",
                                "When customer adds product to bag",
                                "When customer wants to 'CHECKOUT' from add to bag popup",
                                "Then customer should see personalized shopping bag",
                                "When customer click on checkout button in shopping bag page",
                                "Then customer opts to checkout as 'guest'",
                                "Then customer enters 'Customers.HomeOfficeUser' in shipping form",
                                "Then customer continues to payment section of the checkout page",
                                "And customer 'create' payment with credit card payment method using detail 'CreditCards.visa' as 'guest' user",
                                "Then customer continues to review section of the checkout page",
                                "Then customer should be able to review the order",
                                "Then customer select link 'edit shipping' on order review page",
                                "Then customer select ship to store '${Customers.HomeOfficeUser.zip}' and enter customer details 'Customers.HomeOfficeUser'",
                                "Then customer continues to review section from checkout page",
                                "Then customer should be able to review the order",
                                "When customer submit the order",
                                "And customer should able to verify order confirmation page"
                        },
                        orderSteps));


        return scenarios;
    }
}
