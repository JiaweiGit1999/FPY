package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.datatype.Duration;

public class QrCode extends AppCompatActivity {

    TextView timer;
    ImageView code;
    Button generatecode, sharebutton;
    String username, ic, contact, expire;
    List<String> unit;
    User user = User.getInstance();
    QrCodeList qrCodeList = QrCodeList.getInstance();
    Calendar cal = Calendar.getInstance();
    Calendar currentTime = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    CountDownTimer countDownTimer;
    boolean timerRunning = false;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        timer = findViewById(R.id.timer);
        code = findViewById(R.id.qrcode);
        generatecode = findViewById(R.id.generateButton);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (qrCodeList.getUsername() != null && qrCodeList.getExpire() != null) {
            if (currentTime.after(qrCodeList.getExpire()))
                getUserData();
            else
                getQrCodeList();
            generateQrCode();
        } else {
            getUserData();
            generateQrCode();
        }

        generatecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData();
                generateQrCode();
            }
        });
        sharebutton = findViewById(R.id.sharebutton);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = code.getDrawable();
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

                try {
                    File file = new File (getApplicationContext().getExternalCacheDir(), File.separator + "qrcode.png");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    file.setReadable(true,false);
                    final Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID + ".provider",file);
                    intent.putExtra(Intent.EXTRA_STREAM,photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent,"Share Image via"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getQrCodeList() {
        username = qrCodeList.getUsername();
        ic = qrCodeList.getIc();
        contact = qrCodeList.getContact();
        unit = qrCodeList.getUnit();
        cal = qrCodeList.getExpire();
    }

    private void getUserData() {
        username = user.getUsername();
        ic = user.getIc();
        contact = user.getContact();
        unit = user.getUnit();
        cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 2);
    }

    private void generateQrCode() {
        if (timerRunning)
            countDownTimer.cancel();
        expire = String.valueOf(cal.getTime().getTime());
        qrCodeList.setExpire(cal);
        qrCodeList.setIc(ic);
        qrCodeList.setUnit(unit);
        qrCodeList.setUsername(username);
        qrCodeList.setContact(contact);
        Log.d("qrcode:", expire);
        String units = "";
        Set<String> stringSet = new HashSet<>();
        i = 0;
        for (String s : unit) {
            if (i == 0)
                units += s;
            else
                units += "," + s;
            stringSet.add(s);
            i++;
        }
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode("Unit: " + units +
                            "\nOwner Contact: " + contact +
                            "\nOwner IC: " + ic +
                            "\nOwner Name: " + username +
                            "\nExpire time: " + expire,
                    BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        countDownTimer = new CountDownTimer(cal.getTimeInMillis() - currentTime.getTimeInMillis(), 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("time_left", String.valueOf(millisUntilFinished));
                int second = Math.toIntExact(millisUntilFinished / 1000);
                int minute = second / 60;
                int hour = minute / 60;
                minute = minute % 60;
                second = second % 60;


                timer.setText("Time Left: " + String.format(Locale.ENGLISH, "%02d:%02d:%02d", hour, minute, second));
            }

            public void onFinish() {
                timer.setText("done!");
                /*getUserData();
                generateQrCode();*/
            }
        }.start();

        timerRunning = true;
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("contact", contact);
        editor.putString("ic", ic);
        editor.putStringSet("unit", stringSet);
        editor.putLong("time", cal.getTimeInMillis());
        editor.apply();
    }
}