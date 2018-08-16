package cl.activaresearch.android_app.Dooit.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.fragments.AboutFragment;
import cl.activaresearch.android_app.Dooit.fragments.BankFragment;
import cl.activaresearch.android_app.Dooit.fragments.MyTaskFragment;
import cl.activaresearch.android_app.Dooit.fragments.PaymentDetailFragment;
import cl.activaresearch.android_app.Dooit.fragments.PreferenceFragment;
import cl.activaresearch.android_app.Dooit.fragments.ProfileFragment;
import cl.activaresearch.android_app.Dooit.fragments.SeekerFragment;
import cl.activaresearch.android_app.Dooit.fragments.SurveyFragment;
import cl.activaresearch.android_app.Dooit.fragments.TaskDetailFragment;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;


/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class HomeActivity extends BaseActivity {
    public static Fragment fragment;
    public static int FROM_WHERE = 0, TAB_NO = 0;
    private static BackPressed backPressed;
    private TextView tvBank, tvTask, tvSeeker, tvPref, tvProf;
    private ImageView ivBank, ivTask, ivSeeker, ivPref, ivProf;
    private final String TAG = HomeActivity.class.getName();

    public static void setOnBackPressed(BackPressed backPressed1) {
        backPressed = backPressed1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        tvProf.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivProf.setImageResource(R.mipmap.ic_profile);

        loadFragment(new ProfileFragment());
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        final String token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
        if (!refreshedToken.equalsIgnoreCase("")) {
            final String android_id = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("token", refreshedToken);
            jsonObject.addProperty("identifier", android_id);
            jsonObject.addProperty("os", "android");
            ApiHelper.getInstance().registerDevice(token, jsonObject, new ApiCallback.Listener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, result);
                    SharedPreferenceUtility.getInstance(HomeActivity.this).putBoolean(Constants.NOTIFICATION, true);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("identifier", android_id);
                    jsonObject.addProperty("enabled", "1");
                    ApiHelper.getInstance().toggleNotifications(token, jsonObject, new ApiCallback.Listener() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d("Enable result: ", result);
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.d("onFailure:", error + "");
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    Log.d(TAG, error);
                }
            });
        }
    }

    private void initUI() {
        tvBank = (TextView) findViewById(R.id.tv_tab_bank);
        tvTask = (TextView) findViewById(R.id.tv_tab_task);
        tvSeeker = (TextView) findViewById(R.id.tv_tab_seeker);
        tvPref = (TextView) findViewById(R.id.tv_tab_plus);
        tvProf = (TextView) findViewById(R.id.tv_tab_profile);
        ivBank = (ImageView) findViewById(R.id.iv_tab_bank);
        ivTask = (ImageView) findViewById(R.id.iv_tab_task);
        ivSeeker = (ImageView) findViewById(R.id.iv_tab_seeker);
        ivPref = (ImageView) findViewById(R.id.iv_tab_plus);
        ivProf = (ImageView) findViewById(R.id.iv_tab_profile);
    }

    /**
     * Loading fragment
     *
     * @param fragment1
     */
    public void loadFragment(Fragment fragment1) {
        fragment = fragment1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.commit();
    }

    public void onClickPreference(View v) {
        setDeactivatedTab();
        tvPref.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivPref.setImageResource(R.mipmap.ic_pref);
        loadFragment(new PreferenceFragment());
    }

    public void onClickBank(View v) {
        setDeactivatedTab();
        tvBank.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivBank.setImageResource(R.mipmap.ic_bank);
        TAB_NO = 0;
        loadFragment(new BankFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setDeactivatedTab() {
        tvBank.setTextColor(getResources().getColor(R.color.colorDarkGray));
        ivBank.setImageResource(R.mipmap.ic_bank_deactivate);
        tvProf.setTextColor(getResources().getColor(R.color.colorDarkGray));
        ivProf.setImageResource(R.mipmap.ic_profile_deactivate);
        tvSeeker.setTextColor(getResources().getColor(R.color.colorDarkGray));
        ivSeeker.setImageResource(R.mipmap.ic_seeker_deactivate);
        tvTask.setTextColor(getResources().getColor(R.color.colorDarkGray));
        ivTask.setImageResource(R.mipmap.ic_task_deactivate);
        tvPref.setTextColor(getResources().getColor(R.color.colorDarkGray));
        ivPref.setImageResource(R.mipmap.ic_pref_deactivate);
    }

    public void onClickProfile(View v) {
        setDeactivatedTab();
        tvProf.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivProf.setImageResource(R.mipmap.ic_profile);
        loadFragment(new ProfileFragment());
    }

    public void onClickSeeker(View v) {
        setDeactivatedTab();
        tvSeeker.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivSeeker.setImageResource(R.mipmap.ic_seeker);
        TAB_NO = 0;
        loadFragment(new SeekerFragment());
    }

    public void selectBackFragment() {
        switch (FROM_WHERE) {
            case 1:
                onClickSeeker(null);
                break;
            case 2:
                onClickMyTask(null);
                break;
            case 3:
                onClickMyTask(null);
                break;
        }
    }

    public void onClickMyTask(View v) {
        setDeactivatedTab();
        tvTask.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivTask.setImageResource(R.mipmap.ic_task);
        loadFragment(new MyTaskFragment());
    }

    public void onClickSurvey() {
        setDeactivatedTab();
        tvTask.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ivTask.setImageResource(R.mipmap.ic_task);
        loadFragment(new SurveyFragment());
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof ProfileFragment) {
            super.onBackPressed();
            finish();
        } else if (fragment instanceof TaskDetailFragment) {
            selectBackFragment();
        } else if (fragment instanceof AboutFragment) {
            onClickPreference(null);
        } else if (fragment instanceof SurveyFragment) {
            if (backPressed != null) {
                backPressed.onBackPress();
            }
        } else if (fragment instanceof PaymentDetailFragment) {
            if (backPressed != null) {
                backPressed.onBackPress();
            }
        } else {
            setDeactivatedTab();
            fragment = new ProfileFragment();
            tvProf.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            ivProf.setImageResource(R.mipmap.ic_profile);
            loadFragment(fragment);
        }
    }

    public interface BackPressed {
        void onBackPress();
    }
}
