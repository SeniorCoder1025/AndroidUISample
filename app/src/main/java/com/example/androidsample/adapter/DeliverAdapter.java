package com.example.androidsample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidsample.R;
import com.example.androidsample.model.Deliver;

import java.util.ArrayList;

public class DeliverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "DeliverAdapter";

    ArrayList<Deliver> mDataArray;
    Context mContext;

    public DeliverAdapter(Context context, ArrayList<Deliver> delivers) {
        mDataArray = delivers;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_deliver, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Deliver data = mDataArray.get(position);
        AdapterViewHolder vh = (AdapterViewHolder) viewHolder;
        Glide.with(mContext)
                .load(data.imageUrl)
                .into(vh.imageView);
        vh.titleView.setText(data.desc);
    }

    private static class AdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            titleView = (TextView) itemView.findViewById(R.id.title_view);
        }
    }
}
