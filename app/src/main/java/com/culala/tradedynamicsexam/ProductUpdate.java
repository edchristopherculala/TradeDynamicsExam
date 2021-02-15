package com.culala.tradedynamicsexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.culala.tradedynamicsexam.database.DatabaseHelper;
import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductUpdate extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    EditText edttxtproductName,edttxtProductUnit,edttxtDateofExpiry,edttxtPrice,edttxtQuantity;
    Button buttonUpdateProducts;
    ImageView previewImage;

    boolean clicked=false;

    private ByteArrayOutputStream a_thumbnail = new ByteArrayOutputStream();

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        edttxtproductName = findViewById(R.id.edttxtPRODUCTNAMEupdate);
        edttxtProductUnit = findViewById(R.id.edttxtProductUnit);
        edttxtDateofExpiry = findViewById(R.id.edttxtDATEOFEXPIRYupdate);
        edttxtPrice = findViewById(R.id.edttxtBUYPRICEupdate);
        edttxtQuantity = findViewById(R.id.edttxtQTYupdate);

        buttonUpdateProducts = findViewById(R.id.buttonUPDATEPRODUCT);
        previewImage = findViewById(R.id.buttonCameraUpdate);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked = true;

                selectImage(ProductUpdate.this);
            }
        });

        Intent intent = getIntent();

        final String ProductNAME = intent.getStringExtra("productname");
        final String ProductUNIT = intent.getStringExtra("productunit");
        final String doublePrice = intent.getStringExtra("productprice");
        String ProductDateOfExpiry = intent.getStringExtra("dateofexpiry");
        String integerInventory = intent.getStringExtra("inventory");
        final String UNIQUEID = intent.getStringExtra("ID");

        Double ProductPrice = Double.parseDouble(doublePrice);
        Integer ProductInventory = Integer.parseInt(integerInventory);

        edttxtproductName.setText(ProductNAME);
        edttxtProductUnit.setText(ProductUNIT);
        edttxtDateofExpiry.setText(ProductDateOfExpiry);
        edttxtPrice.setText(doublePrice);
        edttxtQuantity.setText(integerInventory);

        edttxtDateofExpiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ProductUpdate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        buttonUpdateProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });


    }

    private void updateItem(){

        Intent intent = getIntent();
        final String UNIQUEID = intent.getStringExtra("ID");

        String PRODUCTNAME = edttxtproductName.getText().toString();
        String PRODUCTUNIT = edttxtProductUnit.getText().toString();
        String PRODUCTDATEOFEXPIRY = edttxtDateofExpiry.getText().toString();
        String PRODUCTPRICE = edttxtPrice.getText().toString();
        String PRODUCTQUANTITY = edttxtQuantity.getText().toString();


        if (PRODUCTNAME.isEmpty() && PRODUCTUNIT.isEmpty() && PRODUCTDATEOFEXPIRY.isEmpty() && PRODUCTPRICE.isEmpty()
                && PRODUCTQUANTITY.isEmpty()){

            Toast.makeText(this,"Please input product data.",Toast.LENGTH_LONG).show();

        }else if (PRODUCTNAME.isEmpty() ){

            Toast.makeText(this,"Please add product name.",Toast.LENGTH_LONG).show();

        }else if (PRODUCTUNIT.isEmpty() ){

            Toast.makeText(this,"Please add product unit.",Toast.LENGTH_LONG).show();
        }else if (PRODUCTDATEOFEXPIRY.isEmpty() ){

            Toast.makeText(this,"Please add product expiration.",Toast.LENGTH_LONG).show();
        }else if (PRODUCTPRICE.isEmpty() ){

            Toast.makeText(this,"Please add product price.",Toast.LENGTH_LONG).show();
        }else if (PRODUCTQUANTITY.isEmpty() ){

            Toast.makeText(this,"Please add product quantity",Toast.LENGTH_LONG).show();
        }
        else {

            String queryProductName = "Select * From products where PRODUCTNAME = '" + PRODUCTNAME + "'";
            if (mDatabase.rawQuery(queryProductName, null).getCount() > 0  ) {
                Toast.makeText(this, "" + PRODUCTNAME + "  is already registered in the system!", Toast.LENGTH_SHORT).show();
            } else {


                String where = "_ID" + "=" + UNIQUEID;

                double price = Double.parseDouble(PRODUCTPRICE);
                int inventory = Integer.parseInt(PRODUCTQUANTITY);


                mDatabase.execSQL("UPDATE  " + ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME + " SET PRODUCTNAME ='" + PRODUCTNAME + "'," +
                        " PRODUCTUNIT ='" + PRODUCTUNIT + "'," + " PRODUCTDATEOFEXPIRY = '" + PRODUCTDATEOFEXPIRY + "'," +
                        " PRODUCTPRICE = '" + price + "'," + " PRODUCTAVAILABLEINVENTORY = '" + inventory + "' WHERE _ID='" + UNIQUEID + "'");


                if (clicked) {
                    ContentValues cv = new ContentValues();
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());
                    mDatabase.update("products", cv, where, null);

                }


                Toast.makeText(this, "Product successfully updated.", Toast.LENGTH_LONG).show();

                clicked = false;

                finish();

            }

        }


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttxtDateofExpiry.setText(sdf.format(myCalendar.getTime()));
    }


    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null ) {


                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                        previewImage.setImageBitmap(selectedImage);


                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri uri =  data.getData();


                        try {

                            if (uri != null) {
                                InputStream imageStream = getContentResolver().openInputStream(uri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                                selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                                previewImage.setImageBitmap(selectedImage);

                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                    break;
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_usermaintenance, menu);
        return super.onPrepareOptionsMenu(menu);
    }
}