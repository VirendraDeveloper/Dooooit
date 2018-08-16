package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.activities.PaymentConfigurationActivity;
import cl.activaresearch.android_app.Dooit.activities.ProfileActivity;
import cl.activaresearch.android_app.Dooit.models.UserBean;
import cl.activaresearch.android_app.Dooit.recievers.NetworkRecognizer;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 13 Jun,2018
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private Activity mContext;
    private TextView tvEdit, tvName, tvEmail, tvCity, tvAddress, tvSex, tvRegion, tvDob;
    private CircleImageView ivProfile;
    private LinearLayout llDOB, llSex, llAddress, llCity, llRegion;
    private View view1, view2, view3, view4, view5;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        tvEdit = (TextView) view.findViewById(R.id.tv_editor);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvDob = (TextView) view.findViewById(R.id.tv_dob);
        tvCity = (TextView) view.findViewById(R.id.tv_city);
        tvSex = (TextView) view.findViewById(R.id.tv_sex);
        tvRegion = (TextView) view.findViewById(R.id.tv_region);
        tvEmail = (TextView) view.findViewById(R.id.tv_email);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        llAddress = (LinearLayout) view.findViewById(R.id.ll_address);
        llCity = (LinearLayout) view.findViewById(R.id.ll_city);
        llDOB = (LinearLayout) view.findViewById(R.id.ll_dob);
        llSex = (LinearLayout) view.findViewById(R.id.ll_sex);
        llRegion = (LinearLayout) view.findViewById(R.id.ll_region);
        ivProfile = (CircleImageView) view.findViewById(R.id.iv_profile);
        view1 = (View) view.findViewById(R.id.view1);
        view2 = (View) view.findViewById(R.id.view2);
        view3 = (View) view.findViewById(R.id.view3);
        view4 = (View) view.findViewById(R.id.view4);
        view5 = (View) view.findViewById(R.id.view5);
        tvEdit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkRecognizer.isNetworkAvailable(mContext)) {
            ((HomeActivity) mContext).showProgress();
            ApiHelper.getInstance().getUserProfileDetails(SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN), new ApiCallback.UserListener() {
                @Override
                public void onSuccess(UserBean userBean) {
                    updateDetails(userBean);
                    ((HomeActivity) mContext).dismissProgress();
                }

                @Override
                public void onFailure(String error) {
                    ((HomeActivity) mContext).showToast(error);
                    ((HomeActivity) mContext).dismissProgress();
                }
            });
        } else {
            ((HomeActivity) mContext).showNetwork();
        }
    }

    private void updateDetails(UserBean userBean) {
        if (userBean.getSexo() != null) {
            llSex.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            tvSex.setText(userBean.getSexo());
        } else {
            llSex.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }
        if (!SharedPreferenceUtility.getInstance(mContext).getBoolean(Constants.PROFILE_DLG)) {
            ((HomeActivity) mContext).setMessageTitleInfo("Hola, " + userBean.getNombres() + "@ a Dooit");
            ((HomeActivity) mContext).setMessageDescriptionInfo(getString(R.string.info_desc));
            ((HomeActivity) mContext).showInfo();
            ((HomeActivity) mContext).setOnAcceptInfoClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) mContext).hideInfoDialog();
                    tvEdit.callOnClick();
                    SharedPreferenceUtility.getInstance(mContext).putBoolean(Constants.PROFILE_DLG, true);
                }
            });
        } else {
            if (!SharedPreferenceUtility.getInstance(mContext).getBoolean(Constants.COMPLETE_DLG)) {
                ((HomeActivity) mContext).setMessageTitleInfo(getString(R.string.info_title1));
                ((HomeActivity) mContext).setMessageDescriptionInfo(getString(R.string.info_desc1));
                ((HomeActivity) mContext).showInfo();
                ((HomeActivity) mContext).setOnAcceptInfoClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) mContext).hideInfoDialog();
                        SharedPreferenceUtility.getInstance(mContext).putBoolean(Constants.COMPLETE_DLG, true);
                        intent = new Intent(mContext, PaymentConfigurationActivity.class);
                        startActivity(intent);
                        mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                });
            } else {
                if (!SharedPreferenceUtility.getInstance(mContext).getBoolean(Constants.PAYMENT_DLG)) {
                    ((HomeActivity) mContext).setMessageTitleInfo(getString(R.string.info_title2));
                    ((HomeActivity) mContext).setMessageDescriptionInfo(getString(R.string.info_desc2));
                    ((HomeActivity) mContext).showInfo();
                    ((HomeActivity) mContext).setOnAcceptInfoClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomeActivity) mContext).hideInfoDialog();
                            SharedPreferenceUtility.getInstance(mContext).putBoolean(Constants.PAYMENT_DLG, true);
                            ((HomeActivity) mContext).onClickSeeker(null);
                        }
                    });

                } else {
                }

            }
        }
        ((HomeActivity) mContext).setMessageTitleInfo(getString(R.string.info_title2));
        ((HomeActivity) mContext).setMessageDescriptionInfo(getString(R.string.info_desc2));
        ((HomeActivity) mContext).showInfo();
        ((HomeActivity) mContext).setOnAcceptInfoClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).hideInfoDialog();
                SharedPreferenceUtility.getInstance(mContext).putBoolean(Constants.PAYMENT_DLG, true);
                ((HomeActivity) mContext).onClickSeeker(null);
            }
        });



        if (userBean.getDireccion() != null && !userBean.getDireccion().equalsIgnoreCase("")) {
            llAddress.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            tvAddress.setText(userBean.getDireccion());
        } else {
            llAddress.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
        }
        if (userBean.getFecha_nac() != null) {
            try {
                llDOB.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(userBean.getFecha_nac());
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                tvDob.setText(sdf2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            llDOB.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }
        if (userBean.getCityAsString() != null) {
            view4.setVisibility(View.VISIBLE);
            tvCity.setText(userBean.getCityAsString());
            llCity.setVisibility(View.VISIBLE);
        } else {
            llCity.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
        }
        if (userBean.getRegionAsString() != null) {
            tvRegion.setText(userBean.getRegionAsString());
            view5.setVisibility(View.VISIBLE);
            llRegion.setVisibility(View.VISIBLE);
        } else {
            llRegion.setVisibility(View.GONE);
            view5.setVisibility(View.GONE);
        }
        if (userBean.getFoto_perfil() != null) {
            Glide.with(mContext)
                    .load(userBean.getFoto_perfil())
                    .into(ivProfile);
        } else {
            ivProfile.setImageResource(R.drawable.profile_bg);
        }
        tvName.setText(userBean.getNombres() + " " + userBean.getApellidos());
        tvEmail.setText(userBean.getEmail());
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(mContext, ProfileActivity.class);
        startActivity(intent);
    }
}
