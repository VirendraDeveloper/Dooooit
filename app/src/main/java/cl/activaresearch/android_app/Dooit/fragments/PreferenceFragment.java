package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.activities.PaymentConfigurationActivity;
import cl.activaresearch.android_app.Dooit.activities.SlideShowActivity;
import cl.activaresearch.android_app.Dooit.activities.WebActivity;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 16 Jun,2018
 */
public class PreferenceFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Activity mContext;
    private RelativeLayout rlAbout, rlSignOff, rlConfiguration, rlShare, rlCommunity, rlFaq;
    private ToggleButton tbNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        // Inflate the layout for this fragment
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        rlSignOff = (RelativeLayout) view.findViewById(R.id.rl_sign_off);
        rlConfiguration = (RelativeLayout) view.findViewById(R.id.rl_configuration);
        rlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);
        rlFaq = (RelativeLayout) view.findViewById(R.id.rl_faq);
        rlCommunity = (RelativeLayout) view.findViewById(R.id.rl_community);
        rlShare = (RelativeLayout) view.findViewById(R.id.rl_share);
        tbNotification = (ToggleButton) view.findViewById(R.id.tgb_notification);
        boolean isChecked = SharedPreferenceUtility.getInstance(mContext).getBoolean(Constants.NOTIFICATION);
        tbNotification.setChecked(isChecked);
        rlAbout.setOnClickListener(this);
        rlSignOff.setOnClickListener(this);
        rlConfiguration.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlCommunity.setOnClickListener(this);
        rlFaq.setOnClickListener(this);
        tbNotification.setOnCheckedChangeListener(this);
    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_configuration:
                intent = new Intent(mContext, PaymentConfigurationActivity.class);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.rl_sign_off:
                signOutAlertDialog();
                break;
            case R.id.rl_about:
                ((HomeActivity) mContext).loadFragment(new AboutFragment());
                break;
            case R.id.rl_community:
                intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"soporte@dooit-app.com"});
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Soporte App Dooit");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi Dooit,");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Send email"));
                break;
            case R.id.rl_share:
                String playStore = "https://play.google.com/store/apps/details?id=cl.activaresearch.android_app.Dooit";
                intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dooit (Open it in Google Play Store to Download the Application)");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, playStore);
                startActivity(Intent.createChooser(intent, "Share via"));
                break;
            case R.id.rl_faq:
                intent = new Intent(mContext, WebActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void signOutAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.sign_off));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.sign_off_msg));

        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_pref_logout);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferenceUtility.getInstance(mContext).putBoolean(Constants.IS_LOGIN, false);
                Intent intent = new Intent(mContext, SlideShowActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mContext.finish();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferenceUtility.getInstance(mContext).putBoolean(Constants.NOTIFICATION, isChecked);
        String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
        String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("identifier", android_id);
        if (isChecked) {
            jsonObject.addProperty("enabled", "1");
        } else {
            jsonObject.addProperty("enabled", "0");
        }
        ApiHelper.getInstance().toggleNotifications(token, jsonObject, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                Log.d("", "");
            }

            @Override
            public void onFailure(String error) {
                Log.d("", "");
            }
        });
    }
}
