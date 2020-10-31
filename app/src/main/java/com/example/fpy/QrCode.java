package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QrCode extends AppCompatActivity {

    ImageView code;
    Button generatecode,sharebutton;
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

                //get current time
                Calendar cal = Calendar.getInstance();
                //add 2 hours
                cal.add(Calendar.HOUR_OF_DAY, 2);
                // convert to string
                expire = (String) DateFormat.format("hh:mm:ss", cal.getTime());

                Log.d("qrcode:",expire);
                String units = "";
                //convert list to string
                for (String s : unit) {
                   units = units +","+ s;
                }
                //generate qrcode
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
}