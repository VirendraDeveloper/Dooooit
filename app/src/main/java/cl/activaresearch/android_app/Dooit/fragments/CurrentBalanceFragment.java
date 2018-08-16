package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.activities.WithdrawActivity;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;


/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 23 Jun,2018
 */
public class CurrentBalanceFragment extends Fragment implements View.OnClickListener {
    private Activity mContext;
    private TextView tvBalance, tvWithdraw;
    private String TAG = CurrentBalanceFragment.class.getName();
    private String mBalance = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_current_balance, container, false);
        // Inflate the layout for this fragment
        tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        tvWithdraw = (TextView) view.findViewById(R.id.tv_withdraw);
        tvWithdraw.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
        ((HomeActivity) mContext).showProgress();
        ApiHelper.getInstance().getBalance(token, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String balance) {
                ((HomeActivity) mContext).dismissProgress();
                mBalance = balance;
                tvBalance.setText("$" + balance);
                if (balance.equalsIgnoreCase("")|| balance.equalsIgnoreCase("0")) {
                    tvWithdraw.setVisibility(View.GONE);
                } else {
                    tvWithdraw.setVisibility(View.VISIBLE);
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
    public void onClick(View v) {
        Intent intent = new Intent(mContext, WithdrawActivity.class);
        intent.putExtra(Constants.BALANCE, mBalance);
        startActivity(intent);
        HomeActivity.TAB_NO = 0;
        mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
