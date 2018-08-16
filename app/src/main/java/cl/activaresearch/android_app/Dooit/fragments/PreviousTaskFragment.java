package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.adapter.TaskAdapter;
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
 * @since 23 Jun,2018
 */
public class PreviousTaskFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Activity mContext;
    private ListView lvTask;
    private TaskAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_task, container, false);
        // Inflate the layout for this fragment
        lvTask = (ListView) view.findViewById(R.id.lv_task);
        lvTask.setEmptyView(view.findViewById(R.id.tv_empty));
        ((HomeActivity) mContext).showProgress();
        ApiHelper.getInstance().getPreviousTask(SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN), new ApiCallback.TasksListener() {
            @Override
            public void onSuccess(List<TaskBean> taskBeans) {
                mAdapter = new TaskAdapter(mContext, taskBeans);
                lvTask.setAdapter(mAdapter);
                ((HomeActivity) mContext).dismissProgress();
            }

            @Override
            public void onFailure(String error) {
                ((HomeActivity) mContext).dismissProgress();
                ((HomeActivity) mContext).showToast(error);
            }
        });
        lvTask.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskBean taskBean = (TaskBean) parent.getAdapter().getItem(position);
        Fragment fragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TASK, taskBean);
        fragment.setArguments(bundle);
        HomeActivity.FROM_WHERE = 3;
        HomeActivity.TAB_NO = 1;
        ((HomeActivity) mContext).loadFragment(fragment);
    }
}
