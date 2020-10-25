package com.example.fpy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivityCredit extends AppCompatActivity {

    private static String paymentIntentClientSecret;
    private Stripe stripe;
    private TextView mAmount;
    private Button payButton;
    private double textamount = 0;
    private static int amount;
    private static PaymentIntent paymentIntent;
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    private Task<String> creditPayment(String json) {
        return mFunctions
                .getHttpsCallable("Credit")
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_credit);

        Intent dataintent = getIntent();

        if (dataintent != null) {
            textamount = dataintent.getDoubleExtra("amount", 0);
        }

        mAmount = findViewById(R.id.amountText);
        payButton = findViewById(R.id.payButton);
        payButton.setText("Pay now");
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51HYjjzF4IJ8BHvcZASjHh7DzctvdHJn2u9kQma9CnPvTbLPoqKm2LeonLfIaoZ7crChlTVsqtADSXslC60JkH9i100nXYkuYni") //Your publishable key
        );
        startCheckout();
    }

    private void startCheckout() {
        final ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        amount = (int) (textamount * 100);
        mAmount.setText("RM " + String.format(Locale.ENGLISH, "%.2f", textamount));
        Map<String, String> payMap = new HashMap<>();
        payMap.put("currency", "myr");
        payMap.put("amount", String.valueOf(amount));
        final String json = new Gson().toJson(payMap);
        Log.i("TAG", "startCheckout: " + json);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if (connected) {
                    creditPayment(json)
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
                                        Log.v("details", task.getResult());
                                        CardInputWidget cardInputWidget = CheckoutActivityCredit.this.findViewById(R.id.cardInputWidget);
                                        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                                        if (params != null) {
                                            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                                            stripe.confirmPayment(CheckoutActivityCredit.this, confirmParams);
                                        } else
                                            Toast.makeText(CheckoutActivityCredit.this, "Please enter your credit/debit card information", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else
                    Toast.makeText(CheckoutActivityCredit.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        if (message != null)
            Log.v("Result", message);
        if (title.equals("Payment completed")) {
            Intent intent = new Intent(CheckoutActivityCredit.this, Payment_successful.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show();
        }
    }

    //Once payment is start, It will call onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivityCredit> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityCredit activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityCredit activity = activityRef.get();
            if (activity == null) {
                return;
            } else {
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
                    Log.i("TAG", "onError:Payment " + gson.toJson(paymentIntent));
                }
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityCredit activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            else {
                savepayment();
                activity.displayAlert("Error", e.toString());
                Log.i("TAG", "onError:Payment " + e.toString());
            }
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
        if (paymentIntent.getStatus() == PaymentIntent.Status.Succeeded)
            paymentdata.put("status", "Successful");
        else
            paymentdata.put("status", "Failed");
        paymentdata.put("amount", amount);
        paymentdata.put("time", new Date().getTime());
        paymentdata.put("payment_method", "Card");
        paymentdata.put("bank", null);
        ref.set(paymentdata);
    }
}