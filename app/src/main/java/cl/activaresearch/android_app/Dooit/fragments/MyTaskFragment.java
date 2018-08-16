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
public class MyTaskFragment extends Fragment implements View.OnClickListener {
    private Activity mContext;
    private TextView tvActiveTab, tvPreviousTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_task, container, false);
        // Inflate the layout for this fragment
        initUI(view);
        tvActiveTab.setOnClickListener(this);
        tvPreviousTab.setOnClickListener(this);
        return view;
    }

    private void initUI(View view) {
        tvPreviousTab = (TextView) view.findViewById(R.id.tv_previous_tab);
        tvActiveTab = (TextView) view.findViewById(R.id.tv_active_tab);
        if (HomeActivity.TAB_NO == 0) {
            tvActiveTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_selected));
            tvPreviousTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_un_selected));
            tvActiveTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            tvPreviousTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            loadFragment(new CurrentTaskFragment());
        } else {
            tvActiveTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_un_selected));
            tvPreviousTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_selected));
            tvPreviousTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            tvActiveTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            loadFragment(new PreviousTaskFragment());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_previous_tab:
                tvActiveTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_un_selected));
                tvPreviousTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_selected));
                tvPreviousTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                tvActiveTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                loadFragment(new PreviousTaskFragment());
                break;
            case R.id.tv_active_tab:
                tvActiveTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.left_selected));
                tvPreviousTab.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.right_un_selected));
                tvActiveTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                tvPreviousTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                loadFragment(new CurrentTaskFragment());
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
        fragmentTransaction.replace(R.id.task_container, fragment);
        fragmentTransaction.commit();
    }
}
