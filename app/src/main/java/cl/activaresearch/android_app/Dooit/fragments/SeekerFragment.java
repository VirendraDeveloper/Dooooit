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


/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 13 Jun,2018
 */
public class SeekerFragment extends Fragment implements View.OnClickListener {
    private Activity mContext;
    private TextView tvListTab, tvMapTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seeker, container, false);
        // Inflate the layout for this fragment
        initUI(view);
        tvMapTab.setOnClickListener(this);
        tvListTab.setOnClickListener(this);
        return view;
    }

    private void initUI(View view) {
        tvListTab = (TextView) view.findViewById(R.id.tv_list_tab);
        tvMapTab = (TextView) view.findViewById(R.id.tv_map_tab);
        loadFragment(new ListaFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_map_tab:
                tvListTab.setBackgroundDrawable(mContext.getDrawable(R.drawable.left_un_selected));
                tvMapTab.setBackgroundDrawable(mContext.getDrawable(R.drawable.right_selected));
                tvMapTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                tvListTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                loadFragment(new MapaFragment());
                break;
            case R.id.tv_list_tab:
                tvListTab.setBackgroundDrawable(mContext.getDrawable(R.drawable.left_selected));
                tvMapTab.setBackgroundDrawable(mContext.getDrawable(R.drawable.right_un_selected));
                tvListTab.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                tvMapTab.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                loadFragment(new ListaFragment());
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
        fragmentTransaction.replace(R.id.seeker_container, fragment);
        fragmentTransaction.commit();
    }
}

