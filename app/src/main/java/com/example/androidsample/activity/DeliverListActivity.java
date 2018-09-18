package com.example.androidsample.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.androidsample.R;
import com.example.androidsample.adapter.DeliverAdapter;
import com.example.androidsample.adapter.RecyclerItemClickListener;
import com.example.androidsample.base.BaseAppCompatActivity;
import com.example.androidsample.model.Deliver;
import com.example.androidsample.network.HttpClient;
import com.example.androidsample.network.HttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class DeliverListActivity extends BaseAppCompatActivity {

    // UI References
    RecyclerView mDeliverRecyclerView;
    DeliverAdapter mDeliverAdapter;

    // Data Array
    ArrayList<Deliver> mDataArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Things to Deliver");

        // Configuration RecyclerView
        mDeliverRecyclerView = (RecyclerView)findViewById(R.id.deliver_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDeliverRecyclerView.setLayoutManager(layoutManager);
        mDeliverRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        mDeliverRecyclerView.addItemDecoration(dividerItemDecoration);

        mDeliverAdapter = new DeliverAdapter(this, mDataArray);
        mDeliverRecyclerView.setAdapter(mDeliverAdapter);
        mDeliverRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mDeliverRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Deliver deliver = mDataArray.get(position);
                        Intent intent = new Intent(DeliverListActivity.this, DetailDeliverActivity.class);
                        intent.putExtra("deliver", deliver);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }){
        });

        loadData();
    }

    private void loadData() {
        showProgress("Loading Data....");
        HttpClient.getHttpClient(this)
                .getDelivers(new HttpResponseHandler<Deliver>() {
                    @Override
                    public void onSuccessList(List<Deliver> response) {
                        hideProgress();
                        mDataArray.clear();
                        mDataArray.addAll(response);
                        mDeliverAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(Deliver response) {
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int responseCode, Exception e) {
                        hideProgress();
                    }
                });
    }
}
