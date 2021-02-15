package com.culala.tradedynamicsexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.culala.tradedynamicsexam.adapter.Pos_Product_Adapter;
import com.culala.tradedynamicsexam.database.DatabaseHelper;
import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;

public class MainActivity extends AppCompatActivity {

    private HelpLiveo mHelpLiveo;
    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    private FloatingActionButton fabAdd;
    private Pos_Product_Adapter mAdapter;

    TextView emptyRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        myDb = new DatabaseHelper(MainActivity.this);
        mDatabase = myDb.getWritableDatabase();
        emptyRec = findViewById(R.id.txttoAddproduct);

        final RecyclerView recyclerview = findViewById(R.id.rvMain);
        mAdapter = new Pos_Product_Adapter(this, getAllItems());

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                if (getProfilesCount() > 0) {
                    //has items
                    recyclerview.setVisibility(View.VISIBLE);
                    emptyRec.setVisibility(View.GONE);
                    recyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                    recyclerview.setAdapter(mAdapter);

                } else {
                    recyclerview.setVisibility(View.GONE);
                    emptyRec.setVisibility(View.VISIBLE);

                }

                handler.postDelayed(this, 500);


            }
        };

        handler.postDelayed(runnable, 500);

    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM products";
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void addProduct(View view) {

        fabAdd = findViewById(R.id.fabAddProducts);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddProduct.class);
                startActivity(intent);

            }
        });
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
    protected void onResume() {
        super.onResume();

        mAdapter.swapCursor(getAllItems());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_products) {

            if (getProfilesCount() > 0) {

                Intent intent = new Intent(MainActivity.this, ProductList.class);
                startActivity(intent);
            } else {
                Toast.makeText(this,"Add products first.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_viewproducts, menu);
        return super.onPrepareOptionsMenu(menu);
    }
}