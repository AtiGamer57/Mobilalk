package com.example.sempebolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private ArrayList<Item> itemList;
    private ItemAdapter itemAdapter;

    private int gridNumber = 1;
    private boolean viewRow = false;

    private FrameLayout redCircle;
    private TextView contentTextView;

    private int cartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList = new ArrayList<>();

        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        initData();
    }

    private void initData() {
        String[] itemsList = getResources().getStringArray(R.array.item_names);
        String[] itemsInfo = getResources().getStringArray(R.array.item_descriptions);
        String[] itemsPrice = getResources().getStringArray(R.array.item_prices);
        TypedArray imagesImgRes = getResources().obtainTypedArray(R.array.item_images);
        TypedArray itemsRatings = getResources().obtainTypedArray(R.array.item_ratings);

        itemList.clear();

        for (int i = 0; i < itemsList.length; i++) {
            itemList.add(new Item(itemsList[i], itemsInfo[i], itemsPrice[i], itemsRatings.getFloat(i, 0), imagesImgRes.getResourceId(i, 0)));
        }

        imagesImgRes.recycle();

        itemAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, s);
                itemAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                Log.d(TAG, "Log out clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.setting_button:
                Log.d(TAG, "Settings clicked!");
                return true;
            case R.id.cart:
                Log.d(TAG, "Cart clicked!");
                return true;
            case R.id.view_selector:
                Log.d(TAG, "View selector clicked!");
                if (viewRow) {
                    changeSpanCount(item, R.drawable.logo_view_big, 1);
                } else {
                    changeSpanCount(item, R.drawable.logo_view_module, 2);
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void changeSpanCount(MenuItem item, int drawableId, int i) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(i);
     }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIconCount(){
        cartItemCount = cartItemCount + 1;
        if (cartItemCount > 0){
            contentTextView.setText(String.valueOf(cartItemCount));
        } else {
            contentTextView.setText("");
        }
    }

}