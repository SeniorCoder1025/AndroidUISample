package com.example.androidsample.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidsample.R;
import com.example.androidsample.base.BaseAppCompatActivity;
import com.example.androidsample.model.Deliver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class DetailDeliverActivity extends BaseAppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Deliver mDeliver;
    ImageView mImageView;
    TextView mTitleView;
    TextView mAddressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_deliver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Delivery Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDeliver = (Deliver) getIntent().getSerializableExtra("deliver");

        // UI References
        mImageView = (ImageView) findViewById(R.id.image_view);
        mTitleView = (TextView) findViewById(R.id.title_view);
        mAddressView = (TextView) findViewById(R.id.address_view);
        Glide.with(this)
                .load(mDeliver.imageUrl)
                .into(mImageView);
        mTitleView.setText(mDeliver.desc);
        mAddressView.setText(mDeliver.address);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(mDeliver.lat, mDeliver.lng);
        mMap.addMarker(new MarkerOptions().position(location).title(mDeliver.desc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
    }
}
