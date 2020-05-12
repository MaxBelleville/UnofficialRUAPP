package com.letgo.ruapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class TravelActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        GoogleMapOptions options = new GoogleMapOptions();
        options.compassEnabled(false);
        options.rotateGesturesEnabled(false);
        options.tiltGesturesEnabled(false);
        options.mapToolbarEnabled(false);
        final SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
        getSupportFragmentManager().beginTransaction().replace(R.id.mapFrameLayout,mapFragment).commit();
        mapFragment.getMapAsync(this);
        AppBarLayout appBar = (AppBarLayout)findViewById(R.id.appbar);
        if( appBar.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        KmlLayer layer = null;
        try {
            layer = new KmlLayer(mMap, R.raw.layer, getApplicationContext());
            layer.addLayerToMap();
            layer.setOnFeatureClickListener(feature -> {
                Log.d("Special","Feature clicked "+ feature.getProperty("name"));
            });
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.setIndoorEnabled(false);
       mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng rye = new LatLng(43.6576585,-79.3809904);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rye,17));
    }
}
