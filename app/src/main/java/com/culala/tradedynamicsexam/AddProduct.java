package com.culala.tradedynamicsexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.culala.tradedynamicsexam.database.DatabaseHelper;
import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddProduct extends AppCompatActivity {


    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    EditText edttxProductName,edttxtProductUnit,edittxtDateofExpiry,edittxtPrice,edittxtQuantity;
    ImageView cameraChoose;
    Button buttonAddProducts;
    String s;

    private ByteArrayOutputStream a_thumbnail = new ByteArrayOutputStream();
    final Calendar myCalendar = Calendar.getInstance();

    Uri selectedImageUri = null;
    boolean isPhoto;

    File imgpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        edttxProductName = findViewById(R.id.edittxtproductName);
        edttxtProductUnit = findViewById(R.id.txtviewproductUnit);
        edittxtDateofExpiry = findViewById(R.id.edittxtdateofExpiry);
        edittxtPrice = findViewById(R.id.edittxtProductPrice);
        edittxtQuantity = findViewById(R.id.edittxtQTY);

        cameraChoose = findViewById(R.id.buttonCamera);

        buttonAddProducts = findViewById(R.id.btnAddProducts);


        isPhoto = false;

        cameraChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                a_thumbnail.reset();
                selectImage(AddProduct.this);
            }
        });



        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };


        edittxtDateofExpiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddProduct.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        buttonAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add Item
                addItem();
            }
        });

    }

    private void addItem(){

        String PRODUCTNAME,PRODUCTUNIT,PRODUCTDATEOFEXPIRY,PRODUCTPRICE,PRODUCTQUANTITY;


        PRODUCTNAME = edttxProductName.getText().toString();
        PRODUCTUNIT = edttxtProductUnit.getText().toString();
        PRODUCTDATEOFEXPIRY = edittxtDateofExpiry.getText().toString();
        PRODUCTPRICE = edittxtPrice.getText().toString().toUpperCase();
        PRODUCTQUANTITY = edittxtQuantity.getText().toString();


        //get the id of the supplier

        if (PRODUCTNAME.isEmpty() && PRODUCTUNIT.isEmpty() && PRODUCTDATEOFEXPIRY.isEmpty() && PRODUCTPRICE.isEmpty() && PRODUCTQUANTITY.isEmpty()){

            Toast.makeText(AddProduct.this,"Please Input Product Data!",Toast.LENGTH_LONG).show();

        }else if (PRODUCTNAME.isEmpty() ){

            Toast.makeText(this,"Please Choose Product Category!",Toast.LENGTH_LONG).show();

        }else if (PRODUCTUNIT.isEmpty() ){

            Toast.makeText(this,"Please Choose Supplier Name!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTDATEOFEXPIRY.isEmpty() ){

            Toast.makeText(this,"Please Input Product Description!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTPRICE.isEmpty() ){

            Toast.makeText(this,"Please Input Product Buy Price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTQUANTITY.isEmpty() ){

            Toast.makeText(this,"Please Input Product Sell Price!",Toast.LENGTH_LONG).show();
        }
        else  {

            String queryProductName = "Select * From products where PRODUCTNAME = '" + PRODUCTNAME + "'";
            if (mDatabase.rawQuery(queryProductName, null).getCount() > 0  ) {
                Toast.makeText(this, "" + PRODUCTNAME + "  is already registered in the system!", Toast.LENGTH_SHORT).show();
            } else {

                    if (isPhoto) {

                        ContentValues cv = new ContentValues();
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_NAME, PRODUCTNAME);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_UNIT, PRODUCTUNIT);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_PRICE, PRODUCTPRICE);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY, PRODUCTDATEOFEXPIRY);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_AVAILABLEINVENTORY, PRODUCTQUANTITY);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());

                        mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv);

                        Toast.makeText(this, "" + PRODUCTNAME + " was successfully added!", Toast.LENGTH_SHORT).show();

                        edttxProductName.getText().clear();
                        edttxtProductUnit.getText().clear();
                        edittxtDateofExpiry.getText().clear();
                        edittxtPrice.getText().clear();
                        edittxtQuantity.getText().clear();

                        closeKeyboard();

                    } else {

                        ContentValues cv = new ContentValues();
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_NAME, PRODUCTNAME);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_UNIT, PRODUCTUNIT);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_PRICE, PRODUCTPRICE);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY, PRODUCTDATEOFEXPIRY);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_AVAILABLEINVENTORY, PRODUCTQUANTITY);
                        cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());

                        mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv);

                        Toast.makeText(this, "" + PRODUCTNAME + " was successfully added!", Toast.LENGTH_SHORT).show();

                        edttxProductName.getText().clear();
                        edttxtProductUnit.getText().clear();
                        edittxtDateofExpiry.getText().clear();
                        edittxtPrice.getText().clear();
                        edittxtQuantity.getText().clear();

                        closeKeyboard();

                    }

            }

        }

        //
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateLabel1() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittxtDateofExpiry.setText(sdf.format(myCalendar.getTime()));
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

    private File getFile() {

        File localStorage = this.getApplicationContext().getFilesDir();
        String RootDir = Environment.getExternalStorageDirectory() + File.separator + "ExamApp";
        File folder = new File(RootDir);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String fileName = Calendar.getInstance().getTime() + ".jpg";
        imgpath = new File(folder, File.separator +
                fileName.trim());
        return imgpath;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        int dataSize=0;

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null ) {


                            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                            selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                            selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                            cameraChoose.setImageBitmap(selectedImage);

                            selectedImageUri = data.getData();

                            isPhoto = true;





                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri uri =  data.getData();


                        try {

                            if (uri != null) {
                                InputStream imageStream = this.getContentResolver().openInputStream(uri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                                selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                                cameraChoose.setImageBitmap(selectedImage);

                                selectedImageUri = data.getData();

                                isPhoto = true;

                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                    break;
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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