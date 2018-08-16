package cl.activaresearch.android_app.Dooit.activities.questions;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.BaseActivity;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.recievers.LocationTrack;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;
import cl.activaresearch.android_app.Dooit.utils.Utility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jun,2018
 */

public class LocationActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener {
    private TextView tvTitle, tvRemember, tvHowToGet, tvCancel, tvOk, tvQuestion;
    private SurveyBean surveyBean;
    private MapView mMapView;
    private double mLatitude = 0, mLongitude = 0;
    private int PLACE_PICKER_REQUEST = 120;
    private String TAG = LocationActivity.class.getName();
    private LocationRequest mLocationRequest;
    private double currentLatitude, currentLongitude;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                }

                @Override
                public void onConnectionSuspended(int i) {
                }
            };
    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvRemember = (TextView) findViewById(R.id.tv_remember);
        tvHowToGet = (TextView) findViewById(R.id.tv_how_get);
        mMapView = (MapView) findViewById(R.id.mapView);
        tvQuestion = (TextView) findViewById(R.id.tv_question_name);
        surveyBean = (SurveyBean) getIntent().getSerializableExtra(Constants.SURVEY);
        tvQuestion.setText(surveyBean.getQuestion());
        tvTitle.setText(getIntent().getStringExtra(Constants.TITLE));
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        tvRemember.setOnClickListener(this);
        tvHowToGet.setOnClickListener(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        mMapView.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                String token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
                JsonObject body = new JsonObject();
                body.addProperty("lat", mLatitude + "");
                body.addProperty("long", mLongitude + "");
                showProgress();
                ApiHelper.getInstance().ansSurveyQuestion(token, surveyBean.getTaskId(), surveyBean.getId(), body, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                        dismissProgress();
                    }
                });
                break;
            case R.id.tv_remember:
                setMessageTitle(getString(R.string.activated_remember));
                setMessageDescription(getString(R.string.notify_home));
                showMessageDialog();
                setOnAcceptClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Location location = new LocationTrack(LocationActivity.this).getLocation();
                        if (location != null) {
                            mLongitude = location.getLongitude();
                            mLatitude = location.getLatitude();
                            tvOk.setEnabled(true);
                            tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
                        } else {
                            showToast(getString(R.string.not_location));
                        }
                        hideMessageDialog();
                    }
                });
                break;
            case R.id.tv_how_get:
                selectLocation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    mLongitude = latLng.latitude;
                    mLatitude = latLng.longitude;
                    tvOk.setEnabled(true);
                    tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    showToast(getString(R.string.not_location));
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location location = new LocationTrack(this).getLocation();
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(250)
                    .strokeWidth(1)
                    .strokeColor(Color.parseColor("#50B690DF"))
                    .fillColor(Color.parseColor("#50B690DF"));
            googleMap.addCircle(circleOptions);
            zoomLocation(latLng, googleMap);
        }
    }

    /**
     * Selecting image form Gallery or Camera
     */
    private void selectLocation() {
        final CharSequence[] items = {getString(R.string.google_map), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.how_get));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(LocationActivity.this);
                if (items[item].equals(getString(R.string.google_map))) {
                    try {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        startActivityForResult(builder.build(LocationActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void zoomLocation(LatLng latLng, GoogleMap googleMap) {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                latLng).zoom(14).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }
}
