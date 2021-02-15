package com.culala.tradedynamicsexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.culala.tradedynamicsexam.adapter.ProductMaintenanceAdapter;
import com.culala.tradedynamicsexam.database.DatabaseHelper;
import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract;

public class ProductList extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private ProductMaintenanceAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DatabaseHelper myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        final TextView txtaddFirst;

        final RecyclerView recyclerview = findViewById(R.id.recyclerviewProduct);
        txtaddFirst = findViewById(R.id.addProduct);
        mAdapter = new ProductMaintenanceAdapter(this, getAllItems());

                    //has items
                    recyclerview.setVisibility(View.VISIBLE);
                    //addFirst.setVisibility(View.GONE);
                    txtaddFirst.setVisibility(View.GONE);
                    recyclerview.setLayoutManager(new LinearLayoutManager(ProductList.this));
                    recyclerview.setAdapter(mAdapter);




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

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.swapCursor(getAllItems());
    }
}