package com.example.sempebolt.DAO;

import com.example.sempebolt.Item;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DaoImpl implements Dao{
    private static DaoImpl dao;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference items = firestore.collection("Items");


    public static DaoImpl getInstance(){
        if (dao == null) {
            dao = new DaoImpl();
        }
        return dao;
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public void deleteItem(Item item) {
        DocumentReference docRef = firestore.collection("items").document(item.getName());
        docRef.delete();
    }

    @Override
    public Task<QuerySnapshot> listItem(int count) {
        Task<QuerySnapshot> list = items.orderBy("name").limit(count).get();
        return list;
    }

    @Override
    public Task<QuerySnapshot> listBetterItem(int count, float rating) {
        Task<QuerySnapshot> list = items.whereGreaterThanOrEqualTo("rating",rating).orderBy("rating", Query.Direction.DESCENDING).orderBy("name").limit(count).get();
        return list;
    }

    @Override
    public void updateItem(Item old, Item neu) {
        deleteItem(old);
        addItem(neu);
    }
}
