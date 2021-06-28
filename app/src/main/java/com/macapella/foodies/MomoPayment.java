package com.macapella.foodies;

import com.android.volley.toolbox.HttpResponse;

import org.apache.http.impl.auth.HttpAuthenticator;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.openunirest.http.Unirest;
import unirest.HttpResponse;
import unirest.Unirest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public class MomoPayment {
    static String subscriptionKey = "13180c04b3b34c959d5daca389f58df4";
    static String paymentReferenceGlobal;

    public static void main(String[] args) throws JSONException {
        String userCellNumber = createUuid();
        createProvisioningUser(userCellNumber, subscriptionKey);

        JSONObject jsonObject = new JSONObject(createApiForUser(userCellNumber, subscriptionKey));
        Map<String, Object> userApiMap = jsonObject.toMap();
        String userApiKey = userApiMap.get("apiKey").toString();


        JSONObject jsonObject1 = new JSONObject(createApiToken(userCellNumber, userApiKey, subscriptionKey));
        Map<String, Object> userApiTokenMap = jsonObject1.toMap();
        String userApiToken = userApiTokenMap.get("access_token").toString();

        JSONObject jsonObject2 = new JSONObject(checkIfUserActiveRegistered("78479252", userApiToken, subscriptionKey));
        Map<String, Object> userActivityRegistrationMap = jsonObject2.toMap();
        String userActivityRegistrationStatus = userActivityRegistrationMap.get("result").toString();

        try {
            JSONObject jsonObject3 = new JSONObject(getUserAccountBalance(subscriptionKey, userApiToken));
            Map<String, Object> getUserBalanceMap = jsonObject3.toMap();
            String userBalance = getUserBalanceMap.get("availableBalance").toString();
            String userCurrency = getUserBalanceMap.get("currency").toString();
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }

        String paymentResponse = requestToPay(subscriptionKey, userApiToken, "78479545", "EUR", "500.00", "Paid", "Paid");

        checkRequestToPayStatus(paymentReferenceGlobal, subscriptionKey, userApiToken);
    }

    public static String createUuid() {
        /*
         * This method is used to generate random UUIDs
         */
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        System.out.println("Your UUID reference has been created: " + uuidAsString);
        return uuidAsString;
    }

    public static void createProvisioningUser(String userCellNumber, String subscriptionKey) {
        /*
         * This method is used to generate a temporal registered and activated sandbox user on the MTN mobile money platform,
         * This is a UUID that serves a username on the platform. It is a temporal solution that is replaced by a
         * real registered and activated cellphone number(MSISDN) of the user in live sessions.
         */
        HttpResponse<String> response = Unirest.post("https://sandbox.momodeveloper.mtn.com/v1_0/apiuser")
                .header("X-Reference-Id", userCellNumber)
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .header("Content-Type", "application/json")
                .body("{\n  \"providerCallbackHost\": \"string\"\n}")
                .asString();
        System.out.println("Provisioning user - request status: " + response.getStatusText());

    }

    public static String createApiForUser(String userCellNumber, String subscriptionKey) {
        /*
         * This method is used to create an API key for a user, it serves as the user's password on the MTN platform.
         * This token should preferably be saved to the users profile to avoid generating a new one for each transaction,
         * and should be updated accordingly if a new one is generated.
         */
        HttpResponse<String> response = Unirest.post("https://sandbox.momodeveloper.mtn.com/v1_0/apiuser/" + userCellNumber + "/apikey")
                .header("X-Reference-Id", userCellNumber)
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .body("")
                .asString();
        System.out.println("Provisioning User API - request status: " + response.getStatusText());
        System.out.println("Provisioning User Key: " + response.getBody());
        return response.getBody();
    }

    public static String createApiToken(String userCellNumber, String apiKey, String subscriptionKey) {
        /*
         * This method is used to create a user's API Token. This token is used to authorize a user's transactions.
         * It is also used to authorize requests for the user's balance, the payment statuses of a user's payments,
         * and to check if a user is eligible to use the MTN mobile money service.
         * This token expires in 3600, and a new one should preferably be generated for each transaction.
         * This token is an http basic authenstication generated using base 64 by encoding the users cellphone number (username)
         * and the user's API key (password).
         */
        //Review the encoder's encodeToString method parameters for accuracy in the way they are entered.
        String encoding = Base64.getEncoder().encodeToString((userCellNumber + ":" + apiKey).getBytes(StandardCharsets.UTF_8));
        System.out.println("Base64 Basic authorization is: " + encoding);

        HttpResponse<String> response = Unirest.post("https://sandbox.momodeveloper.mtn.com/collection/token/")
                .header("Ocp-Apim-Subscription-Key",subscriptionKey)
                .header("Authorization", "Basic " + encoding)
                .asString();

        System.out.println("User API token - request status: " + response.getStatusText());
        System.out.println(response.getBody());
        return response.getBody();

    }

    public static String checkIfUserActiveRegistered(String userCellNumber, String userApiToken, String subscriptionKey) {
        /*
         * This method is used to check if a user is active and/or registered to use the MTN mobile money service.
         * This information could be used to determine if the control flow should proceed to check the user's balance and request a payment.
         */
        HttpResponse<String> response = Unirest.get("https://sandbox.momodeveloper.mtn.com/collection/v1_0/accountholder/msisdn/" + userCellNumber + "/active")
                .header("X-Target-Environment", "sandbox")
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .header("Authorization", "Bearer " + userApiToken)
                .asString();

        System.out.println("User activity and registration - request status: " + response.getStatusText());
        System.out.println(response.getBody());
        return response.getBody();

    }

    public static String getUserAccountBalance(String subscriptionKey, String userApiToken) {
        /*
         *This method is used to check the balance of a user.
         * This information may be useful to determine if the control flow should proceed to request a payment or not, depending on the amount of
         * the payment against the user's balance.
         */

        HttpResponse<String> response = Unirest.get("https://sandbox.momodeveloper.mtn.com/collection/v1_0/account/balance")
                .header("X-Target-Environment", "sandbox")
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .header("Authorization", "Bearer " + userApiToken)
                .asString();

        System.out.println("User balance - request status: " + response.getStatusText());
        System.out.println(response.getBody());
        return response.getBody();

    }

    public static String requestToPay(String subscriptionKey, String userApiToken, String userCellNumber, String currency, String paymentAmount, String payerMessage, String payeeNote) {
        /*
         * This method is used to send a request for a payment.
         * The X-reference in this case is a UUID generated and used as a reference for the payment. This reference is also used to check the status of
         * the payment in the checkRequestToPayStatus method.
         * The payment reference is associated to the userApiToken.
         */

        String paymentReference = createUuid();
        paymentReferenceGlobal = paymentReference;
        HttpResponse<String> response = Unirest.post("https://sandbox.momodeveloper.mtn.com/collection/v1_0/requesttopay")
                .header("X-Reference-Id", paymentReference)
                .header("X-Target-Environment", "sandbox")
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .header("Authorization", "Bearer " + userApiToken)
                .header("Content-Type", "text/plain")
                .body("{\n  \"amount\": \"" + paymentAmount + "\",\n  \"currency\": \"" + currency +"\",\n  \"externalId\": \"" + paymentReference + "\",\n  \"payer\": {\n    \"partyIdType\": \"MSISDN\",\n    \"partyId\": \""+ userCellNumber +"\"\n  },\n  \"payerMessage\": \"" + payerMessage + "\",\n  \"payeeNote\": \"" + payeeNote + "\"\n}")
                .asString();

        System.out.println("Request to Pay: " + response.getStatusText());
        return response.getStatusText();

    }

    public static void checkRequestToPayStatus(String paymentReference, String subscriptionKey, String userApiToken) {
        /*
         * This method is used for checking the status of a payment.
         * The payment reference in the request URL is a UUID, the same UUID payment reference used to request the payment for which you are checking the status.
         */

        HttpResponse<String> response = Unirest.get("https://sandbox.momodeveloper.mtn.com/collection/v1_0/requesttopay/" + paymentReference)
                .header("X-Target-Environment", "sandbox")
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .header("Authorization", "Bearer " + userApiToken)
                .asString();

        System.out.println(response.getStatusText());
        System.out.println(response.getBody());

    }


}

