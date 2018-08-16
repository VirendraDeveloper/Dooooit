package cl.activaresearch.android_app.Dooit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.activities.InstructionsActivity;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiClient;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.FilePathManager;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 23 Jun,2018
 */
public class TaskDetailFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    private Activity mContext;
    private ImageView ivBack, ivTask;
    private TextView tvStart, tvDetails, tvInstruction, tvMoney, tvDistance, tvTimer,
            tvCategory, tvTaskName, tvPoll, tvValidation, tvCobra;
    private TaskBean taskBean;
    private LinearLayout llTaskDetails;
    private MapView mMapView;
    private GoogleMap googleMap;
    private ProgressBar pbTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        // Inflate the layout for this fragment
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            taskBean = (TaskBean) bundle.getSerializable(Constants.TASK);
        }
        return view;
    }

    private void initUI(View view) {
        mMapView = (MapView) view.findViewById(R.id.mapView);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivTask = (ImageView) view.findViewById(R.id.iv_task);
        tvStart = (TextView) view.findViewById(R.id.tv_start);
        tvPoll = (TextView) view.findViewById(R.id.tv_poll);
        tvValidation = (TextView) view.findViewById(R.id.tv_validation);
        tvCobra = (TextView) view.findViewById(R.id.tv_cobra);
        tvTaskName = (TextView) view.findViewById(R.id.tv_task_name);
        tvCategory = (TextView) view.findViewById(R.id.tv_category);
        tvCategory = (TextView) view.findViewById(R.id.tv_category);
        tvTimer = (TextView) view.findViewById(R.id.tv_timer);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        tvMoney = (TextView) view.findViewById(R.id.tv_money);
        pbTask = (ProgressBar) view.findViewById(R.id.pb_task);
        tvInstruction = (TextView) view.findViewById(R.id.tv_instruction);
        tvDetails = (TextView) view.findViewById(R.id.tv_details);
        llTaskDetails = (LinearLayout) view.findViewById(R.id.rl_task_details);
        ivBack.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvPoll.setOnClickListener(this);
        tvInstruction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ((HomeActivity) mContext).selectBackFragment();
                break;
            case R.id.tv_instruction:
                Intent intent = new Intent(mContext, InstructionsActivity.class);
                intent.putExtra(Constants.TASK, taskBean);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                ((HomeActivity) mContext).hideAlertDialog();
                break;
            case R.id.tv_start:
                String aboutDesc = getString(R.string.about_start_task1) +" " + taskBean.getHoras() + "h" + taskBean.getMins() + "m " + getString(R.string.about_start_task2);
                ((HomeActivity) mContext).setAlertDescription(aboutDesc);
                ((HomeActivity) mContext).setAlertTitle(getString(R.string.start_task));
                ((HomeActivity) mContext).showAlertDialog();
                ((HomeActivity) mContext).setOnAcceptClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, InstructionsActivity.class);
                        intent.putExtra(Constants.TASK, taskBean);
                        startActivity(intent);
                        mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        ((HomeActivity) mContext).hideAlertDialog();
                    }
                });
                ((HomeActivity) mContext).setOnCancelClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) mContext).hideAlertDialog();
                    }
                });
                break;
            case R.id.tv_poll:
                Fragment fragment = new SurveyFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TASK, taskBean);
                fragment.setArguments(bundle);
                ((HomeActivity) mContext).loadFragment(fragment);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        ((HomeActivity) mContext).showProgress();
        ((HomeActivity) mContext).hideKeyboard(mContext);
        ApiHelper.getInstance().getTaskDetails(SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN), taskBean.getCod_tarea() + "", new ApiCallback.TaskListener() {

            @Override
            public void onSuccess(TaskBean taskBeanDetail) {
                Log.d("", "");
                taskBean = taskBeanDetail;
                ((HomeActivity) mContext).dismissProgress();
                tvCategory.setText(taskBean.getCategoria());
                tvTaskName.setText(taskBean.getNombre());
                tvCategory.setTextColor(Color.parseColor(taskBean.getColor()));
                tvDetails.setText(taskBean.getDescripcion());
                tvMoney.setText(taskBean.getPago());
                double roundOff = Math.round(taskBean.getDistancia() * 100.0) / 100.0;
                tvDistance.setText(roundOff + "km");
                tvTimer.setText(taskBean.getHoras() + ":" + taskBean.getMins());
                int pg = (int) (taskBean.getProgress() * 10);
                pbTask.setProgress(pg);
                // pbTask.getProgressDrawable().setColorFilter(Color.parseColor(taskBeanDetail.getColor()), PorterDuff.Mode.SRC_IN);
                if (taskBean.getFoto() != null) {
                    Glide.with(mContext)
                            .load(ApiClient.BASE_URL + taskBean.getFoto())
                            .into(ivTask);
                }
                switch (taskBeanDetail.getStatus()) {
                    case 1:
                        llTaskDetails.setVisibility(View.GONE);
                        tvCobra.setVisibility(View.GONE);
                        tvValidation.setVisibility(View.GONE);
                        tvStart.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        tvCobra.setVisibility(View.GONE);
                        tvValidation.setVisibility(View.GONE);
                        llTaskDetails.setVisibility(View.VISIBLE);
                        tvStart.setVisibility(View.GONE);
                        break;
                    case 3:
                        tvCobra.setVisibility(View.GONE);
                        tvValidation.setVisibility(View.GONE);
                        llTaskDetails.setVisibility(View.VISIBLE);
                        tvStart.setVisibility(View.GONE);
                        break;
                    case 4:
                        tvCobra.setVisibility(View.GONE);
                        tvValidation.setVisibility(View.VISIBLE);
                        llTaskDetails.setVisibility(View.GONE);
                        tvStart.setVisibility(View.GONE);
                        break;
                    case 5:
                        tvCobra.setVisibility(View.VISIBLE);
                        tvValidation.setVisibility(View.GONE);
                        llTaskDetails.setVisibility(View.GONE);
                        tvStart.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onFailure(String error) {
                ((HomeActivity) mContext).dismissProgress();
                ((HomeActivity) mContext).showToast(error);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (taskBean != null) {
            new LoadMarker().execute();

            LatLng latLng = new LatLng(Double.parseDouble(taskBean.getLat()), Double.parseDouble(taskBean.getLon()));
            // create marker
            /*MarkerOptions marker = new MarkerOptions().position(latLng).title(taskBean.getNombre());
            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()));
            // adding marker
            googleMap.addMarker(marker);*/
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private class LoadMarker extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            // Get bitmap from server
            Bitmap overlay;
            try {
                URL url = new URL(ApiClient.BASE_URL + taskBean.getIcono());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                overlay = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return overlay;
        }

        @SuppressLint("ResourceType")
        protected void onPostExecute(Bitmap bitmap) {
            LatLng latLng = new LatLng(Double.parseDouble(taskBean.getLat()), Double.parseDouble(taskBean.getLon()));
            // If received bitmap successfully, draw it on our drawable
            if (bitmap != null) {
                View custom_layout = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                ImageView ivMarkerBg = (ImageView) custom_layout.findViewById(R.id.iv_marker_bg);
                ImageView iv_category_logo = (ImageView) custom_layout.findViewById(R.id.iv_marker);
                ivMarkerBg.setColorFilter(Color.parseColor(taskBean.getColor()));
                //frameLayout.setBackgroundTintList(getResources().getColorStateList(Color.parseColor(taskBean.getColor())));
                Bitmap pinbit = Bitmap.createScaledBitmap(bitmap, 40, 60, false);
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
