package com.culala.tradedynamicsexam.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.culala.tradedynamicsexam.ProductUpdate;
import com.culala.tradedynamicsexam.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.culala.tradedynamicsexam.database.DatabaseHelper;
import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class ProductMaintenanceAdapter extends RecyclerView.Adapter<ProductMaintenanceAdapter.ProductMaintenanceViewHolder> {

    private Context mContext;
    private Cursor mCursor, mCursorSold;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;


    public ProductMaintenanceAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;


    }


    public class ProductMaintenanceViewHolder extends RecyclerView.ViewHolder {


        public TextView txtProductName;
        public TextView txtProductUnit;
        public TextView txtProductPrice;
        public TextView txtProductDateofExpiry;
        public TextView txtProductAvailableInventry;
        public TextView txtProductRemainingCost;
        public TextView txtProductCode;

        public ImageView update;
        public ImageView create;

        private Button add_stocks;

        private ImageView productImage, menu;


        public LinearLayout type_select_layout;


        public ProductMaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);


            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductUnit = itemView.findViewById(R.id.txtproductunit);
            txtProductPrice = itemView.findViewById(R.id.txtproductprice);
            txtProductDateofExpiry = itemView.findViewById(R.id.txtproductexpirydate);
            txtProductAvailableInventry = itemView.findViewById(R.id.txtProductQUANTITY);
            productImage = itemView.findViewById(R.id.productImage1);
            txtProductRemainingCost = itemView.findViewById(R.id.txtremainingCost);
            txtProductCode = itemView.findViewById(R.id.txtProductCode);
            //  add_stocks = itemView.findViewById(R.id.button_add_stocks);

            type_select_layout = itemView.findViewById(R.id.select_layout_5);

            // update = itemView.findViewById(R.id.circularEditP);
            menu = itemView.findViewById(R.id.btnMenu);


        }
    }

    @NonNull
    @Override
    public ProductMaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.product_maintenance_item, parent, false);
        return new ProductMaintenanceAdapter.ProductMaintenanceViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ProductMaintenanceViewHolder holder, final int position) {


        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();


        if (!mCursor.moveToPosition(position)) {
            return;
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));


        final String productName = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_NAME));
        final String productUnit = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_UNIT));
        final double productPrice = mCursor.getDouble(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_PRICE));
        final String productDateofExpiry = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY));
        final int productAvailableInventory = mCursor.getInt(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_AVAILABLEINVENTORY));

        Double remainingStockCost = productAvailableInventory * productPrice;

        String inventory = String.valueOf(productAvailableInventory);

      /*  int soldSales, currentQuantity, totalRemainingStocks;
        String stringTotalRemainingStock, count3;

        count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
        mcursor3.moveToFirst();
        soldSales = mcursor3.getInt(0);

        currentQuantity = Integer.parseInt(productQUANTITY);

        totalRemainingStocks = currentQuantity - soldSales;
        stringTotalRemainingStock = String.valueOf(totalRemainingStocks);
        */
        //END OF STOCK CHECKER


        final long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));

        String longID = String.valueOf(id);

        //TODO change blob they wanted a PATH!!!
       /* byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
        final Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
 */
        holder.txtProductName.setText(productName);
        holder.txtProductUnit.setText(productUnit);
        holder.txtProductPrice.setText(format.format(productPrice));
        holder.txtProductDateofExpiry.setText(productDateofExpiry);
        holder.txtProductAvailableInventry.setText(inventory);
        holder.txtProductRemainingCost.setText(format.format(remainingStockCost));
        holder.txtProductCode.setText(longID);

        byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
        final Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

        if (bmp != null) {
            holder.productImage.setImageBitmap(bmp);
        }
        else {
            holder.productImage.setImageResource(R.drawable.previewimage);
        }
        holder.itemView.setTag(id);

        final String idString = String.valueOf(id);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectMenu(mContext,productName,productUnit,productPrice,productDateofExpiry,productAvailableInventory,id,idString);
            }
        });

        holder.type_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.create.setVisibility(View.VISIBLE);
                // holder.update.setVisibility(View.VISIBLE);


            }
        });


    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }

    private void selectMenu(Context mContext, String productName, String productUnit, Double productPrice, String productDateofExpiry, Integer productAvailableInventory, long id, String idString) {


        final CharSequence[] options = { "Update", "Delete","Cancel" };

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Update or Delete this Product?");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {



                if (options[item].equals("Delete")) {

                    //Delete Function


                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(false);
                        builder.setTitle("DELETE");
                        builder.setMessage("Are you sure you want to delete this Product?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long)id);
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog2 = builder.create();
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.show();


                } else if (options[item].equals("Update")) {

                    Intent intent = new Intent(mContext, ProductUpdate.class);
                    String priceDouble = String.valueOf(productPrice);
                    String inventoryInteger = String.valueOf(productAvailableInventory);

                    intent.putExtra("productname", productName);
                    intent.putExtra("productunit", productUnit);
                    intent.putExtra("productprice", priceDouble);
                    intent.putExtra("dateofexpiry", productDateofExpiry);
                    intent.putExtra("inventory", inventoryInteger);
                    intent.putExtra("ID", idString);

                    mContext.startActivity(intent);

                }  else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProductMaintenanceContract.ProductMaintenanceEntry._ID + " ASC"
        );

    }

    private void removeItem(long id) {
        mDatabase.delete(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME,
                ProductMaintenanceContract.ProductMaintenanceEntry._ID + "=" + id, null);
        swapCursor(getAllItems());

    }

}
