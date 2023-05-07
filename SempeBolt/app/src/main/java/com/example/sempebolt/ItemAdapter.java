package com.example.sempebolt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<Item> itemsDataAll;
    private ArrayList<Item> itemsData;
    private Context context;
    private int lastPosition = -1;

    ItemAdapter(Context context, ArrayList<Item> itemsData){
        this.itemsData = itemsData;
        this.itemsDataAll = itemsData;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item currentItem = itemsData.get(position);
        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Item> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0){
                results.count = itemsDataAll.size();
                results.values = itemsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Item item : itemsDataAll){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView descriptionText;
        private TextView priceText;
        private ImageView itemImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.itemTitle);
            descriptionText = itemView.findViewById(R.id.itemDescription);
            priceText = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.findViewById(R.id.cartButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("shashanga", "shashanga");
                    try {
                        ((ShopActivity) context).updateAlertIconCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("shashanga2", "shashanga2");
                }
            });
        }

        public void bindTo(Item currentItem) {
            titleText.setText(currentItem.getName());
            descriptionText.setText(currentItem.getDescription());
            priceText.setText(currentItem.getPrice());
            ratingBar.setRating(currentItem.getRating());

            Glide.with(context).load(currentItem.getImgRes()).into(itemImage);
        }
    }
}
