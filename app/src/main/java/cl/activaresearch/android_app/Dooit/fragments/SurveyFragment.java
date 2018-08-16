package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.LocationActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.MultiAlternativeActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.MultiLineActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.PhotoActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.SimpleAlternativeActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.SingleLineActivity;
import cl.activaresearch.android_app.Dooit.activities.questions.VideoActivity;
import cl.activaresearch.android_app.Dooit.adapter.SurveyAdapter;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
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
public class SurveyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, HomeActivity.BackPressed {
    private Activity mContext;
    private ImageView ivBack;
    private TextView tvTitle, tvFinish;
    private ListView listView;
    private SurveyAdapter mAdapter;
    private String strToken;
    private TaskBean taskBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            taskBean = (TaskBean) bundle.getSerializable(Constants.TASK);
        }
        initUI(view);
        HomeActivity.setOnBackPressed(this);
        return view;
    }

    private void initUI(View view) {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        listView = (ListView) view.findViewById(R.id.lv_survey);
        // FooterView when the list is smaller than the screen
        final View footerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        tvFinish = (TextView) footerView.findViewById(R.id.tv_finish); // How you reference the textview in the footer
        if(taskBean.getCod_tarea()==0){
            tvFinish.setText(getResources().getString(R.string.finish_survey));
        }else {
            tvFinish.setText(getResources().getString(R.string.confirm_survey));
        }
        listView.addFooterView(footerView);

        tvTitle.setText(taskBean.getNombre());
        ivBack.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        mAdapter = new SurveyAdapter(mContext);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        strToken = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mContext).showProgress();
        if (taskBean.getCod_tarea() == 0) {
            ApiHelper.getInstance().getTaskShopperSurvey(strToken, new ApiCallback.SurveyListener() {
                @Override
                public void onSuccess(List<SurveyBean> surveyBeans) {
                    mAdapter.addAllTask(surveyBeans);
                    boolean isComplete = true;
                    ((HomeActivity) mContext).dismissProgress();
                    for (int i = 0; i < surveyBeans.size(); i++) {
                        SurveyBean surveyBean = surveyBeans.get(i);
                        if (!surveyBean.isAnswered()) {
                            isComplete = false;
                        }
                    }
                    if (isComplete) {
                        tvFinish.setEnabled(true);
                        tvFinish.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                    }
                }

                @Override
                public void onFailure(String error) {
                    ((HomeActivity) mContext).dismissProgress();
                    ((HomeActivity) mContext).showToast(error);
                }
            });
        } else {
            ApiHelper.getInstance().getTaskSurvey(strToken, taskBean.getCod_tarea() + "", new ApiCallback.SurveyListener() {
                @Override
                public void onSuccess(List<SurveyBean> surveyBeans) {
                    mAdapter.addAllTask(surveyBeans);
                    boolean isComplete = true;
                    ((HomeActivity) mContext).dismissProgress();
                    for (int i = 0; i < surveyBeans.size(); i++) {
                        SurveyBean surveyBean = surveyBeans.get(i);
                        if (!surveyBean.isAnswered()) {
                            isComplete = false;
                        }
                    }
                    if (isComplete) {
                        tvFinish.setEnabled(true);
                        tvFinish.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                    }
                }

                @Override
                public void onFailure(String error) {
                    ((HomeActivity) mContext).dismissProgress();
                    ((HomeActivity) mContext).showToast(error);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Fragment fragment = new TaskDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TASK, taskBean);
                fragment.setArguments(bundle);
                ((HomeActivity) mContext).loadFragment(fragment);
                break;
            case R.id.tv_finish:
                completeTaskDialog();
                break;
        }

    }

    private void completeTaskDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        //alertDialog.setTitle(getString(R.string.sure_finish));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.sure_finish));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.mipmap.ic_pref_logout);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getString(R.string.finish_survey), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
                ApiHelper.getInstance().completeTask(token, taskBean.getCod_tarea() + "", new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("", "");
                        ((HomeActivity) mContext).setMessageDescription(getString(R.string.about_finished));
                        ((HomeActivity) mContext).setMessageTitle(getString(R.string.finished));
                        ((HomeActivity) mContext).showMessageDialog();
                        ((HomeActivity) mContext).setOnAcceptClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment fragment = new TaskDetailFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.TASK, taskBean);
                                fragment.setArguments(bundle);
                                ((HomeActivity) mContext).loadFragment(fragment);
                                ((HomeActivity) mContext).hideMessageDialog();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        ((HomeActivity) mContext).showToast(error);
                        Log.d("", "");
                    }
                });
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SurveyBean surveyBean = (SurveyBean) mAdapter.getItem(position);
        String title = (position + 1) + " / " + mAdapter.getCount();
        Intent intent;
        switch (surveyBean.getType()) {
            case SingleLineText:
                intent = new Intent(mContext, SingleLineActivity.class);
                intent.putExtra(Constants.SURVEY, (Serializable) surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case MultiLineText:
                intent = new Intent(mContext, MultiLineActivity.class);
                intent.putExtra(Constants.SURVEY, surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case SimpleAlternative:
                intent = new Intent(mContext, SimpleAlternativeActivity.class);
                intent.putExtra(Constants.SURVEY, surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case MultiAlternative:
                intent = new Intent(mContext, MultiAlternativeActivity.class);
                intent.putExtra(Constants.SURVEY, surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case Image:
                intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra(Constants.SURVEY, surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case Video:
                intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra(Constants.SURVEY, surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case Location:
                intent = new Intent(mContext, LocationActivity.class);
                intent.putExtra(Constants.SURVEY, surveyBean);
                intent.putExtra(Constants.TITLE, title);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
        }

    }

    @Override
    public void onBackPress() {
        Fragment fragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TASK, taskBean);
        fragment.setArguments(bundle);
        ((HomeActivity) mContext).loadFragment(fragment);
    }
}
