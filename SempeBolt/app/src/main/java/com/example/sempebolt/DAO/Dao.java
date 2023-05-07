package com.example.sempebolt.DAO;

import com.example.sempebolt.Item;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public interface Dao {
    public void addItem(Item item);
    public void deleteItem(Item item);
    public Task<QuerySnapshot> listItem(int count);
    public Task<QuerySnapshot> listBetterItem(int count, float rating);
    public void updateItem(Item old, Item neu);
}
