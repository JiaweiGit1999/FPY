package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCode extends AppCompatActivity {
    EditText unit;
    EditText number;
    EditText ic;
    EditText name;
    ImageView code;
    Button generatecode;
    String text1, text2, text3, text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        unit = findViewById(R.id.unit);
        number = findViewById(R.id.ownernumber);
        ic = findViewById(R.id.ic);
        name = findViewById(R.id.visitorname);
        code = findViewById(R.id.qrcode);
        generatecode = findViewById(R.id.generateButton);
        generatecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1 = unit.getText().toString().trim();
                text2 = number.getText().toString().trim();
                text3 = ic.getText().toString().trim();
                text4 = name.getText().toString().trim();

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode("Unit: " + text1 + "\nOwner Number: " + text2 + "\nVisitor IC: " + text3 + "\nVisitor Name: " + text4, BarcodeFormat.QR_CODE, 200, 200);
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