package cl.activaresearch.android_app.Dooit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.FilterActivity;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.models.CategoryBean;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.recievers.LocationTrack;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiClient;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.FilePathManager;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

import static android.app.Activity.RESULT_OK;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 18 Jun,2018
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private final int FILTER_CODE = 1022;
    private Activity mContext;
    private MapView mMapView;
    private GoogleMap googleMap;
    private ImageView ivFilter, ivGps;
    private Location myLocation;
    private int distance;
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        initUI(view);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        mMapView.onSaveInstanceState(savedInstanceState);
        ivFilter.setOnClickListener(this);
        ivGps.setOnClickListener(this);
        getCategory();
        return view;
    }

    private void getCategory() {
        String cat = "[";
        for (CategoryBean categoryBean : FilterActivity.categories) {
            if (categoryBean.isSelect()) {
                cat = cat + categoryBean.getId() + ",";
            }
        }
        if (!cat.equalsIgnoreCase("[")) {
            cat = cat.substring(0, cat.length() - 1);
        }
        cat = cat + "]";
        category = cat;
        distance = FilterActivity.distance;
    }

    private void initUI(View view) {
        mMapView = (MapView) view.findViewById(R.id.mapView);
        ivFilter = (ImageView) view.findViewById(R.id.iv_filter);
        ivGps = (ImageView) view.findViewById(R.id.iv_gps);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        myLocation = new LocationTrack(getActivity()).getLocation();
        // latitude and longitude
        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            // create marker
            MarkerOptions marker = new MarkerOptions().position(latLng).title("Hello Maps");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dooit_marker));

            // adding marker
            googleMap.addMarker(marker);
            zoomLocation(latLng);
        }

    }

    private void zoomLocation(LatLng latLng) {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                latLng).zoom(11).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
        Location location = new LocationTrack(getActivity()).getLocation();
        if (location != null) {
            ((HomeActivity) mContext).showProgress();
            ApiHelper.getInstance().getAllTask(token, location.getLatitude() + "", location.getLongitude() + "", distance + "", category, new ApiCallback.TasksListener() {
                @Override
                public void onSuccess(List<TaskBean> taskBeans) {
                    for (TaskBean taskBean : taskBeans) {
                        if (googleMap != null) {
                            new LoadMarker().execute(taskBean);
                            addTaskMarker(taskBean);
                        }
                    }
                    ((HomeActivity) mContext).dismissProgress();
                }

                @Override
                public void onFailure(String error) {
                    ((HomeActivity) mContext).dismissProgress();
                    // ((HomeActivity) mContext).showToast(error);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_filter:
                Intent intent = new Intent(mContext, FilterActivity.class);
                startActivityForResult(intent, FILTER_CODE);
                mContext.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                break;
            case R.id.iv_gps:
                Location location = new LocationTrack(getActivity()).getLocation();
                // latitude and longitude
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    zoomLocation(latLng);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                distance = data.getIntExtra(Constants.DISTANCE, 0);
                category = data.getStringExtra(Constants.CATEGORIES);
                Log.d("", "");
            }
        }
    }

    private void addTaskMarker(TaskBean taskBean) {
        LatLng latLng = new LatLng(Double.parseDouble(taskBean.getLat()), Double.parseDouble(taskBean.getLon()));
        // create marker
        MarkerOptions marker = new MarkerOptions().position(latLng).title(taskBean.getNombre()).snippet(taskBean.getCategoria());
        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dooit_marker));
        // adding marker
        googleMap.addMarker(marker);


    }

    private class LoadMarker extends AsyncTask<TaskBean, Void, TaskBean> {
        @Override
        protected TaskBean doInBackground(TaskBean... params) {
            // Get bitmap from server
            TaskBean taskBean;
            Bitmap overlay;
            try {
                taskBean = params[0];
                URL url = new URL(ApiClient.BASE_URL + taskBean.getIcono());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                overlay = BitmapFactory.decodeStream(input);
                taskBean.setBitmap(overlay);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return taskBean;
        }

        @SuppressLint("ResourceType")
        protected void onPostExecute(TaskBean taskBean) {
            LatLng latLng = new LatLng(Double.parseDouble(taskBean.getLat()), Double.parseDouble(taskBean.getLon()));
            // If received bitmap successfully, draw it on our drawable
            if (taskBean.getBitmap() != null) {
                View custom_layout = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                ImageView ivMarkerBg = (ImageView) custom_layout.findViewById(R.id.iv_marker_bg);
                ImageView iv_category_logo = (ImageView) custom_layout.findViewById(R.id.iv_marker);
                ivMarkerBg.setColorFilter(Color.parseColor(taskBean.getColor()));
                //frameLayout.setBackgroundTintList(getResources().getColorStateList(Color.parseColor(taskBean.getColor())));
                Bitmap pinbit = Bitmap.createScaledBitmap(taskBean.getBitmap(), 40, 60, false);
                iv_category_logo.setImageBitmap(pinbit);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(FilePathManager.getMarkerBitmapFromView(custom_layout));
                // Add the new marker to the map
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("")
                        .snippet("")
                        .icon(bitmapDescriptor));
            } else {
                // Add the new marker to the map
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
            }
        }
    }

}
