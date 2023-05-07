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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.sempebolt.DAO.DaoImpl;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private ArrayList<Item> itemList;
    private ItemAdapter itemAdapter;

    private FirebaseFirestore firestore;
    private CollectionReference items;

    private int gridNumber = 1;
    private boolean viewRow = false;

    private FrameLayout redCircle;
    private TextView contentTextView;

    private TextView animatedText;

    private int cartItemCount = 0;

    private static int shownItems = 3;

    DaoImpl dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: elindult");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList = new ArrayList<>();

        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);


        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("Items");

        dao = DaoImpl.getInstance();

        queryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }

    static boolean firstQuery = true;
    private void queryData() {
        itemList.clear();

        //először a nagyon tűz itemek mutatása
        if (firstQuery){
            firstQuery = false;
            dao.listBetterItem(3, 4).addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);
                }

                if (itemList.size() == 0) {
                    initData();
                    queryData();
                }
                itemAdapter.notifyDataSetChanged();
            });
        } else {
            dao.listItem(shownItems).addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Item item = document.toObject(Item.class);
                    itemList.add(item);
                }

                if (itemList.size() == 0) {
                    initData();
                    queryData();
                }

                itemAdapter.notifyDataSetChanged();
            });
        }
    }

    private void initData() {
        String[] itemsList = getResources().getStringArray(R.array.item_names);
        String[] itemsInfo = getResources().getStringArray(R.array.item_descriptions);
        String[] itemsPrice = getResources().getStringArray(R.array.item_prices);
        String[] itemsImages = getResources().getStringArray(R.array.item_images);
        TypedArray itemsRatings = getResources().obtainTypedArray(R.array.item_ratings);


        for (int i = 0; i < itemsList.length; i++) {
            Item item = new Item(itemsList[i], itemsInfo[i], itemsPrice[i], itemsRatings.getFloat(i, 0), itemsImages[i]);
            itemList.add(item);
            try {
                dao.addItem(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        Log.d(TAG, "updateAlertIconCount: " + cartItemCount);
        if (cartItemCount > 0){
            Log.d(TAG, "Trying to increase cart number");
            try {
                contentTextView.setText(String.valueOf(cartItemCount));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            contentTextView.setText("");
        }
    }

    public void showMore(View view) {
        shownItems += 3;
        queryData();
    }
}