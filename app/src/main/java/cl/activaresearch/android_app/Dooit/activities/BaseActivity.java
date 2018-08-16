package cl.activaresearch.android_app.Dooit.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.recievers.LocationTrack;
import cl.activaresearch.android_app.Dooit.recievers.NetworkRecognizer;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jun,2018
 */
public class BaseActivity extends AppCompatActivity implements NetworkRecognizer.ConnectivityReceiveListener, LocationTrack.GpsListener {
    private Dialog pDialog, nDialog, gDialog, iDialog, mDialog, aDialog;
    private TextView tvDesc, tvTitle, tvDesc1, tvTitle1, tvDesc2, tvTitle2;
    private View.OnClickListener clickListener, clickListener1, clickListener2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /*initialize progress dialogs */
        pDialog = new Dialog(this);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setCancelable(false);
        pDialog.setContentView(R.layout.progress_dialog);
        ImageView ivSpinner = (ImageView) pDialog.findViewById(R.id.iv_spinner);
        final AnimationDrawable mailAnimation = (AnimationDrawable) ivSpinner.getBackground();
        ivSpinner.post(new Runnable() {
            public void run() {
                if (mailAnimation != null) mailAnimation.start();
            }
        });

         /*initialize network dialogs */
        nDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog);
        nDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nDialog.setContentView(R.layout.network_dilaog);
        nDialog.setCancelable(false);
        TextView tvOk = (TextView) nDialog.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nDialog.dismiss();
            }
        });



           /*initialize network dialogs */
        gDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog);
        gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gDialog.setContentView(R.layout.gps_dialog);
        gDialog.setCancelable(false);
        TextView tvOk1 = (TextView) gDialog.findViewById(R.id.tv_ok);
        tvOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        aDialog = new Dialog(this);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aDialog.setContentView(R.layout.alert_dialog);
        aDialog.setCancelable(false);
        tvTitle = (TextView) aDialog.findViewById(R.id.tv_title);
        tvDesc = (TextView) aDialog.findViewById(R.id.tv_desc);
        TextView tvAccept = (TextView) aDialog.findViewById(R.id.tv_accept);
        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onClick(v);
            }
        });
        TextView tvCancel = (TextView) aDialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener1 != null) clickListener1.onClick(v);
            }
        });


        mDialog = new Dialog(this);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.message_dialog);
        mDialog.setCancelable(false);
        tvTitle1 = (TextView) mDialog.findViewById(R.id.tv_title);
        tvDesc1 = (TextView) mDialog.findViewById(R.id.tv_desc);
        TextView tvAccept1 = (TextView) mDialog.findViewById(R.id.tv_accept);
        tvAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onClick(v);
            }
        });

        iDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        iDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        iDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        iDialog.setContentView(R.layout.info_dialog);
        iDialog.setCancelable(false);
        tvTitle2 = (TextView) iDialog.findViewById(R.id.tv_title);
        tvDesc2 = (TextView) iDialog.findViewById(R.id.tv_desc);
        TextView tvAccept3 = (TextView) iDialog.findViewById(R.id.tv_accept);
        tvAccept3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener2 != null) clickListener2.onClick(v);
            }
        });
        NetworkRecognizer.setConnectivityListener(this);
        startService(new Intent(this, LocationTrack.class));
        LocationTrack.setOnGpsListener(this);
    }

    public void setOnAcceptClick(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnAcceptInfoClick(View.OnClickListener clickListener2) {
        this.clickListener2 = clickListener2;
    }
    public void setOnCancelClick(View.OnClickListener clickListener1) {
        this.clickListener1 = clickListener1;
    }

    public void setAlertDescription(String desc) {
        if (tvDesc != null) tvDesc.setText(desc);
    }

    public void setAlertTitle(String title) {
        if (tvTitle != null) tvTitle.setText(title);
    }

    public void setMessageDescription(String desc) {
        if (tvDesc1 != null) tvDesc1.setText(desc);
    }
    public void setMessageDescriptionInfo(String desc) {
        if (tvDesc2 != null) tvDesc2.setText(desc);
    }

    public void setMessageTitle(String title) {
        if (tvTitle1 != null) tvTitle1.setText(title);
    }

    public void setMessageTitleInfo(String title) {
        if (tvTitle2 != null) tvTitle2.setText(title);
    }

    public void showNetwork() {
        nDialog.show();
    }

    public void showProgress() {
        pDialog.show();
    }

    public void showAlertDialog() {
        aDialog.show();
    }

    public void showMessageDialog() {
        mDialog.show();
    }

    public void hideMessageDialog() {
        mDialog.dismiss();
    }

    public void hideAlertDialog() {
        aDialog.dismiss();
    }

    public void showInfo() {
        iDialog.show();
    }

    public void hideInfoDialog() {
        iDialog.dismiss();
    }

    public void dismissNetwork() {
        nDialog.dismiss();
    }

    public void dismissProgress() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    public boolean isNetworkShowing() {
        if (nDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            nDialog.dismiss();
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onShowGps(boolean isOpen) {
        if (isOpen) {
            if (!gDialog.isShowing()) {
                gDialog.show();
            }
        } else {
            gDialog.dismiss();
        }
    }
}
