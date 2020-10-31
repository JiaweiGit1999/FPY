package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.Stripe;
import com.stripe.android.databinding.FpxPaymentMethodBinding;
import com.stripe.android.model.Address;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.StripeIntent;
import com.stripe.android.view.AddPaymentMethodActivityStarter;
import com.stripe.android.view.FpxBank;
import com.stripe.android.view.ShippingInfoWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import okhttp3.Response;

public class CheckoutActivityOnline extends AppCompatActivity {

    private static String paymentIntentClientSecret;
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    private Stripe stripe;
    private static int amount;
    private static String detail;
    private Map<String, String> payMap = new HashMap<>();
    private static PaymentIntent paymentIntent;

    private Task<String> onlinePayment(String json) {
        return mFunctions
                .getHttpsCallable("Online")
                .call(json)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        Log.v("result", result);
                        return result;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_online);
        Intent dataintent = getIntent();
        if (dataintent != null) {
            amount = (int) dataintent.getDoubleExtra("amount", 0);
            detail = dataintent.getStringExtra("detail");
        }

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51HYjjzF4IJ8BHvcZASjHh7DzctvdHJn2u9kQma9CnPvTbLPoqKm2LeonLfIaoZ7crChlTVsqtADSXslC60JkH9i100nXYkuYni"
        );
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51HYjjzF4IJ8BHvcZASjHh7DzctvdHJn2u9kQma9CnPvTbLPoqKm2LeonLfIaoZ7crChlTVsqtADSXslC60JkH9i100nXYkuYni") //Your publishable key
        );
        AddPaymentMethodActivityStarter addPaymentMethodActivityStarter = new AddPaymentMethodActivityStarter(this);
        addPaymentMethodActivityStarter.startForResult(new AddPaymentMethodActivityStarter.Args.Builder()
                .setPaymentMethodType(PaymentMethod.Type.Fpx)
                .build()
        );

        payMap.put("amount", String.valueOf(amount * 100));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("data", String.valueOf(requestCode));
        Log.v("data", String.valueOf(resultCode));
        final AddPaymentMethodActivityStarter.Result result =
                AddPaymentMethodActivityStarter.Result.fromIntent(data);
        if (result instanceof AddPaymentMethodActivityStarter.Result.Success) {
            final AddPaymentMethodActivityStarter.Result.Success successResult =
                    (AddPaymentMethodActivityStarter.Result.Success) result;
            onPaymentMethodResult(successResult.getPaymentMethod());
        } else {
            Log.v("data", "stripe");
            stripe.onPaymentResult(requestCode, data, new CheckoutActivityOnline.PaymentResultCallback(this));
        }
    }

    private void onPaymentMethodResult(@NonNull final PaymentMethod paymentMethod) {
        if (paymentMethod.fpx != null) {
            final String fpxBankCode = Objects.requireNonNull(paymentMethod.fpx).bank;
            final FpxBank fpxBank = FpxBank.get(fpxBankCode);
            if (fpxBank != null) {
                Log.v("Payment Type Code:", fpxBank.getCode());
                Log.v("Payment Type Name:", fpxBank.getDisplayName());
                Log.v("Payment Type Id:", fpxBank.getId());
                final String json = new Gson().toJson(payMap);
                Log.i("TAG", "startCheckout: " + json);

                if (paymentMethod.id != null) {
                    onlinePayment(json)
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Exception e = task.getException();
                                        Log.v("error", e.getMessage());
                                        if (e instanceof FirebaseFunctionsException) {
                                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                            FirebaseFunctionsException.Code code = ffe.getCode();
                                            Object details = ffe.getDetails();
                                        }
                                    } else {
                                        paymentIntentClientSecret = task.getResult();
                                        ConfirmPaymentIntentParams confirmParams =
                                                ConfirmPaymentIntentParams.createWithPaymentMethodId(paymentMethod.id, paymentIntentClientSecret, "https://www.google.com/");
                                        stripe.confirmPayment(CheckoutActivityOnline.this, confirmParams);
                                        Log.v("Payment", "confirming");
                                    }
                                }
                            });
                } else
                    Log.v("Payment", "failed");
            }
        }
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {

        if (message != null)
            Log.v("Result", message);
        if (title.equals("Payment completed")) {
            Intent intent = new Intent(CheckoutActivityOnline.this, Payment_successful.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    //Once payment is start, It will call onActivityResult
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new CheckoutActivityOnline.PaymentResultCallback(this));
    }*/

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivityOnline> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityOnline activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityOnline activity = activityRef.get();
            if (activity == null) {
                return;
            }
            paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //If It is successful You will get detail in log and UI
                Log.i("TAG", "onSuccess:Payment " + gson.toJson(paymentIntent));
                savepayment();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                savepayment();
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Log.i("TAG", "onAuthentication:Payment " + gson.toJson(paymentIntent));
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityOnline activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            savepayment();
            activity.displayAlert("Error", e.toString());
        }
    }

    private static void savepayment() {
        Map<String, Object> paymentdata = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref;
        try {
            ref = db.collection("payment").document(paymentIntent.getId());
            paymentdata.put("payment_id", paymentIntent.getId());
        } catch (Exception e) {
            ref = db.collection("payment").document(paymentIntentClientSecret);
            paymentdata.put("payment_id", paymentIntentClientSecret);
        }
        paymentdata.put("user_id", User.getInstance().getUid());
        try {
            if (paymentIntent.getStatus() == PaymentIntent.Status.Succeeded)
                paymentdata.put("status", "Successful");
            else
                paymentdata.put("status", "Failed");
        } catch (Exception e) {
            paymentdata.put("status", "Failed");
        }
        paymentdata.put("amount", amount);
        paymentdata.put("time", new Date().getTime());
        paymentdata.put("payment_method", "FPX");
        paymentdata.put("detail", detail);
        try {
            paymentdata.put("bank", paymentIntent.getPaymentMethod().fpx.bank);
        } catch (Exception e) {
            paymentdata.put("bank", null);
        }
        ref.set(paymentdata);
    }
}
