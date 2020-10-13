package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QrCode extends AppCompatActivity {

    ImageView code;
    Button generatecode;
    String username, ic, contact,expire;
    List<String> unit;
    User user = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        code = findViewById(R.id.qrcode);
        generatecode = findViewById(R.id.generateButton);
        generatecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from user class
                username = user.getUsername();
                ic = user.getIc();
                contact = user.getContact();
                unit = user.getUnit();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, 2);
                expire = (String) DateFormat.format("hh:mm:ss", cal.getTime());
                Log.d("qrcode:",expire);
                String units = "";
                //convert list to string
                for (String s : unit) {
                   units = units +","+ s;
                }

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode("Unit: " + units +
                            "\nOwner Contact: " + contact +
                            "\nVisitor IC: " + ic +
                            "\nVisitor Name: " + username+
                            "\nexpire time: " + expire,
                            BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    code.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}