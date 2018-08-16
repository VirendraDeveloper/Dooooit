package cl.activaresearch.android_app.Dooit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cl.activaresearch.android_app.Dooit.R;


/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ACCESS_FINE_LOCATION = 1001, CAMERA = 1002, READ_EXTERNAL_STORAGE = 1003, RECORD_AUDIO = 1004;
    private RelativeLayout rlNotification, rlPhoto, rlCamera, rlLocation, rlMicrophone;
    private TextView tvFinalizer;
    private ImageView ivCamera, ivLocation, ivNotification, ivMice, ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        initUI();
        isAllPermissionAvailable();
    }

    private void initUI() {
        rlCamera = (RelativeLayout) findViewById(R.id.rl_camera);
        rlLocation = (RelativeLayout) findViewById(R.id.rl_location);
        rlMicrophone = (RelativeLayout) findViewById(R.id.rl_microphone);
        rlNotification = (RelativeLayout) findViewById(R.id.rl_notification);
        rlPhoto = (RelativeLayout) findViewById(R.id.rl_photo);
        tvFinalizer = (TextView) findViewById(R.id.tv_finalizar);
        ivCamera = (ImageView) findViewById(R.id.iv_camera_check);
        ivLocation = (ImageView) findViewById(R.id.iv_location_check);
        ivMice = (ImageView) findViewById(R.id.iv_mice_check);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo_check);
        ivNotification = (ImageView) findViewById(R.id.iv_notification_check);
        rlPhoto.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlMicrophone.setOnClickListener(this);
        rlNotification.setOnClickListener(this);
        rlCamera.setOnClickListener(this);
        tvFinalizer.setOnClickListener(this);
        if (isAllPermissionAvailable()) {
            tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
            tvFinalizer.setEnabled(true);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
            tvFinalizer.setEnabled(false);
        }
    }

    /**
     * Verify all required permissions
     *
     * @return
     */
    private boolean isAllPermissionAvailable() {
        if (!isReadCameraAllowed()) {
            return false;
        } else if (!isReadStorageAllowed()) {
            return false;
        } else if (!isReadLocationAllowed()) {
            return false;
        } else if (!isReadMiceAllowed()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * check camera permission granted or not
     *
     * @return
     */
    private boolean isReadCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CAMERA);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            ivCamera.setImageResource(R.mipmap.ic_white_check);
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    /**
     * request to camera permission
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA);
    }

    /**
     * request to camera permission
     */
    private void requestMicePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.RECORD_AUDIO)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
    }

    /**
     * check location permission granted or not
     *
     * @return
     */
    private boolean isReadLocationAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            ivLocation.setImageResource(R.mipmap.ic_white_check);
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    /**
     * check location permission granted or not
     *
     * @return
     */
    private boolean isReadMiceAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.RECORD_AUDIO);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            ivLocation.setImageResource(R.mipmap.ic_white_check);
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    /**
     * request to location permission
     */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
    }

    /**
     * check storage permission granted or not
     *
     * @return
     */
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            ivPhoto.setImageResource(R.mipmap.ic_white_check);
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    /**
     * request to storage permission
     */
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
    }

    /**
     * Result of runtime permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean is = isAllPermissionAvailable();
        if (is) {
            tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
            tvFinalizer.setEnabled(true);
        } else {
            tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
            tvFinalizer.setEnabled(false);
        }
        switch (requestCode) {
            case ACCESS_FINE_LOCATION:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ivLocation.setImageResource(R.mipmap.ic_white_check);
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        ivLocation.setImageResource(R.mipmap.ic_white_uncheck);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CAMERA:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ivCamera.setImageResource(R.mipmap.ic_white_check);
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        ivCamera.setImageResource(R.mipmap.ic_white_uncheck);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case READ_EXTERNAL_STORAGE:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ivPhoto.setImageResource(R.mipmap.ic_white_check);
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        ivPhoto.setImageResource(R.mipmap.ic_white_uncheck);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RECORD_AUDIO:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ivMice.setImageResource(R.mipmap.ic_white_check);
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        ivMice.setImageResource(R.mipmap.ic_white_uncheck);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_camera:
                if (isReadCameraAllowed()) {
                    if (isAllPermissionAvailable()) {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvFinalizer.setEnabled(true);
                    } else {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
                        tvFinalizer.setEnabled(false);
                    }

                } else {
                    requestCameraPermission();
                }
                break;
            case R.id.rl_location:
                if (isReadLocationAllowed()) {
                    if (isAllPermissionAvailable()) {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvFinalizer.setEnabled(true);
                    } else {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
                        tvFinalizer.setEnabled(false);
                    }

                } else {
                    requestLocationPermission();
                }
                break;
            case R.id.rl_microphone:
                if (isReadMiceAllowed()) {
                    if (isAllPermissionAvailable()) {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvFinalizer.setEnabled(true);
                    } else {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
                        tvFinalizer.setEnabled(false);
                    }
                } else {
                    requestMicePermission();
                }
                break;
            case R.id.rl_notification:
                if (isReadCameraAllowed()) {
                    if (isAllPermissionAvailable()) {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvFinalizer.setEnabled(true);
                    } else {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
                        tvFinalizer.setEnabled(false);
                    }

                } else {

                }
                break;
            case R.id.rl_photo:
                if (isReadStorageAllowed()) {
                    if (isAllPermissionAvailable()) {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvFinalizer.setEnabled(true);
                    } else {
                        tvFinalizer.setTextColor(getResources().getColor(R.color.colorLight));
                        tvFinalizer.setEnabled(false);
                    }
                } else {
                    requestStoragePermission();
                }
                break;
            case R.id.tv_finalizar:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
