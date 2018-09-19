package com.example.androidsample.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.androidsample.R;
import com.example.androidsample.adapter.DeliverAdapter;
import com.example.androidsample.adapter.RecyclerItemClickListener;
import com.example.androidsample.base.BaseAppCompatActivity;
import com.example.androidsample.model.Deliver;
import com.example.androidsample.network.HttpClient;
import com.example.androidsample.network.HttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class DeliverListActivity extends BaseAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // UI References
    RecyclerView mDeliverRecyclerView;
    SwipeRefreshLayout mRefreshLayout;
    DeliverAdapter mDeliverAdapter;
    ProgressBar mLoadMoreIndicate;

    // Data Array
    ArrayList<Deliver> mDataArray = new ArrayList<>();


    final int Load_None = 0;
    final int Load_More = 1;
    final int Load_New = 2;
    int loadState = Load_None;
    final int Page_Size = 20;
    boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Things to Deliver");

        // Configuration RecyclerView
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        mRefreshLayout.setOnRefreshListener(this);
        mDeliverRecyclerView = (RecyclerView)findViewById(R.id.deliver_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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

        // setup load more
        mDeliverRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (loadState == Load_None &&
                        lastVisibleItem > totalCount - 2) {
                    loadMore();
                }
            }
        });

        mLoadMoreIndicate = (ProgressBar)findViewById(R.id.load_more_indicate);
        mLoadMoreIndicate.setVisibility(View.GONE);

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
    }

    private void loadMore() {
        if (loadState != Load_None || hasMore == false)
            return;
        loadState = Load_More;
        mLoadMoreIndicate.setVisibility(View.VISIBLE);
        HttpClient.getHttpClient(this)
                .getDelivers(mDataArray.size(), Page_Size, new HttpResponseHandler<Deliver>() {
                    @Override
                    public void onSuccessList(List<Deliver> response) {
                        mRefreshLayout.setRefreshing(false);
                        mDataArray.addAll(response);
                        mDeliverAdapter.notifyDataSetChanged();
                        loadState = Load_None;
                        mLoadMoreIndicate.setVisibility(View.GONE);
                        if (response.size() < Page_Size) {
                            hasMore = false;
                        }
                    }
                });
    }

    private void loadData() {
        if (loadState != Load_None) {
            if (loadState == Load_More)
                mRefreshLayout.setRefreshing(false);
            return;
        }
        loadState = Load_New;
        hasMore = true;
        mRefreshLayout.setRefreshing(true);
        HttpClient.getHttpClient(this)
                .getDelivers(0, Page_Size, new HttpResponseHandler<Deliver>() {
                    @Override
                    public void onSuccessList(List<Deliver> response) {
                        mRefreshLayout.setRefreshing(false);
                        mDataArray.clear();
                        mDataArray.addAll(response);
                        mDeliverAdapter.notifyDataSetChanged();
                        loadState = Load_None;
                        if (response.size() < Page_Size) {
                            hasMore = false;
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
