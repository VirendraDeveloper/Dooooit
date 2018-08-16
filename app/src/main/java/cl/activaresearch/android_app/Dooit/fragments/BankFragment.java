package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;


/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 13 Jun,2018
 */
public class BankFragment extends Fragment implements View.OnClickListener {
    private Activity mContext;
    private TextView tvBalanceTab, tvHistoryTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank, container, false);
        // Inflate the layout for this fragment
        initUI(view);
        tvBalanceTab.setOnClickListener(this);
        tvHistoryTab.setOnClickListener(this);
        return view;
    }

    private void initUI(View view) {
        tvBalanceTab = (TextView) view.findViewById(R.id.tv_balance_tab);
        tvHistoryTab = (TextView) view.findViewById(R.id.tv_history_tab);
        if (HomeActivity.TAB_NO == 0) {
            tvBalanceTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_selected));
            tvHistoryTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_un_selected));
            tvBalanceTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            tvHistoryTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            loadFragment(new CurrentBalanceFragment());
        } else {
            tvBalanceTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_un_selected));
            tvHistoryTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_selected));
            tvHistoryTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            tvBalanceTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            loadFragment(new PaymentHistoryFragment());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_history_tab:
                tvBalanceTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_un_selected));
                tvHistoryTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_selected));
                tvHistoryTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                tvBalanceTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                loadFragment(new PaymentHistoryFragment());
                break;
            case R.id.tv_balance_tab:
                tvBalanceTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_selected));
                tvHistoryTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_un_selected));
                tvBalanceTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                tvHistoryTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                loadFragment(new CurrentBalanceFragment());
                break;
        }
    }

    /**
     * Loading fragment
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bank_container, fragment);
        fragmentTransaction.commit();
    }
}