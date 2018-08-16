package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;


/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 24 Jun,2018
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    public static final int FILTER_CODE = 1022;
    private Activity mContext;
    private ImageView ivBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((HomeActivity) mContext).onClickPreference(null);
    }
}
