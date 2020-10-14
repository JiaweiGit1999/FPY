package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.Stripe;
import com.stripe.android.model.Address;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.view.AddPaymentMethodActivityStarter;
import com.stripe.android.view.ShippingInfoWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CheckoutActivityOnline extends AppCompatActivity {

    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    private PaymentSession paymentSession;
    private PaymentSession.PaymentSessionListener paymentSessionListener;
    //Api myBackendApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_online);

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51HYjjzF4IJ8BHvcZASjHh7DzctvdHJn2u9kQma9CnPvTbLPoqKm2LeonLfIaoZ7crChlTVsqtADSXslC60JkH9i100nXYkuYni"
        );
        AddPaymentMethodActivityStarter addPaymentMethodActivityStarter = new AddPaymentMethodActivityStarter(this);
        addPaymentMethodActivityStarter.startForResult(new AddPaymentMethodActivityStarter.Args.Builder()
                .setPaymentMethodType(PaymentMethod.Type.Fpx)
                .build()
        );
        //Log.v("Payment Type:", );


        //createPaymentSessionConfig();
        /*paymentSession = new PaymentSession(
                this,
                new PaymentSessionConfig.Builder()
                        .setPaymentMethodTypes(Arrays.asList(
                                PaymentMethod.Type.Fpx
                        ))
                        .setShippingMethodsRequired(false)
                        .build()
        );
        paymentSession.init(paymentSessionListener);*/

        /*addMessage("word")
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            Log.v("error",e.getMessage());
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            // ...
                        }
                        else
                            Log.v("details",task.getResult());
                        // ...
                    }
                });
    }

    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        Log.v("result",result);
                        return result;
                    }
                });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final AddPaymentMethodActivityStarter.Result result =
                AddPaymentMethodActivityStarter.Result.fromIntent(data);
        if (result != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.getJSONObject(result.toString());
                PaymentMethod paymentMethod = PaymentMethod.fromJson(jsonObject);
                Toast.makeText(CheckoutActivityOnline.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.v("method", result.toString());
                /*stripe.onPaymentResult(requestCode, data,
                        new ApiResultCallback<PaymentIntentResult>() {
                            @Override
                            public void onSuccess(@NonNull PaymentIntentResult result) {
                                // If authentication succeeded, the PaymentIntent will have
                                // user actions resolved; otherwise, handle the PaymentIntent
                                // status as appropriate (e.g. the customer may need to choose
                                // a new payment method)

                                final PaymentIntent.Status status = result.getIntent().getStatus();
                                if (PaymentIntent.Status.RequiresPaymentMethod == status) {
                                    // attempt authentication again or ask for a new Payment Method
                                } else if (PaymentIntent.Status.RequiresConfirmation == status) {
                                    // handle confirming the PaymentIntent again on the server
                                } else if (PaymentIntent.Status.Succeeded == status) {
                                    // capture succeeded
                                }
                            }

                            @Override
                            public void onError(@NonNull Exception e) {
                                // handle error
                            }
                        });*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private PaymentSessionConfig createPaymentSessionConfig() {
        return new PaymentSessionConfig.Builder()

                // collect shipping information
                .setShippingInfoRequired(false)

                // collect shipping method
                .setShippingMethodsRequired(false)

                // specify the payment method types that the customer can use;
                // defaults to PaymentMethod.Type.Card
                .setPaymentMethodTypes(
                        Arrays.asList(PaymentMethod.Type.Fpx)
                )

                // if `true`, will show "Google Pay" as an option on the
                // Payment Methods selection screen
                .setShouldShowGooglePay(false)

                .build();
    }


    /*private void createPaymentIntent() {
        myBackendApiClient.createPaymentIntent(params,
                new ApiResultCallback<String>() {
                    @Override
                    public void onSuccess(@NonNull String clientSecret) {
                        // Hold onto clientSecret for Step 4
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        // handle error
                    }
                });
    }*/
}