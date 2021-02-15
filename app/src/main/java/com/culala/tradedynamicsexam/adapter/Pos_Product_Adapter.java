package com.culala.tradedynamicsexam.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.culala.tradedynamicsexam.ProductList;
import com.culala.tradedynamicsexam.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import com.culala.tradedynamicsexam.R;
import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract;

public class Pos_Product_Adapter extends RecyclerView.Adapter<Pos_Product_Adapter.PosProductViewHolder> {


    private Context mContext;
    private Cursor mCursor;


    private SQLiteDatabase mDatabase;
    private ByteArrayOutputStream p_thumbnail = new ByteArrayOutputStream();


    DatabaseHelper myDb;

    Animation animation, animation1;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;


    public Pos_Product_Adapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

    }

    public class PosProductViewHolder extends RecyclerView.ViewHolder {


        public TextView txtProductCode;
        public TextView txtProductName;
        public TextView txtProductSellPrice;


        public CardView select_layout;

        public ImageView product_thumb;


        public PosProductViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txtposproductName);
            txtProductSellPrice = itemView.findViewById(R.id.txtposproductSell);
            product_thumb = itemView.findViewById(R.id.product_thumbnail);

            select_layout = itemView.findViewById(R.id.selectCard);
        }
    }

    @NonNull
    @Override
    public PosProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_product, parent, false);
        return new Pos_Product_Adapter.PosProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PosProductViewHolder holder, int position) {

        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();





            //if you have a saved currency in settings

                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productName = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_NAME));
                final double productPrice = mCursor.getDouble(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_PRICE));

                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                final Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                if (bmp != null){
                    holder.product_thumb.setImageBitmap(bmp);
                }else {
                    holder.product_thumb.setImageResource(R.drawable.previewimage);
                }


        holder.txtProductName.setText(productName);
                holder.txtProductSellPrice.setText(format.format(productPrice));

                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //go to productlist


                    }
                });







    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
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
}
