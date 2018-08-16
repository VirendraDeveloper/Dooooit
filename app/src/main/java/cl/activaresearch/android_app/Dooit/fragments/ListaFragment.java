package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.FilterActivity;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.adapter.TaskAdapter;
import cl.activaresearch.android_app.Dooit.models.CategoryBean;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.recievers.LocationTrack;
import cl.activaresearch.android_app.Dooit.recievers.NetworkRecognizer;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

import static android.app.Activity.RESULT_OK;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 18 Jun,2018
 */
public class ListaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private final int FILTER_CODE = 1022;
    private Activity mContext;
    private ListView lvTask;
    private TaskAdapter taskAdapter;
    private ImageView ivFilter;
    private List<TaskBean> taskBeans;
    private int distance;
    private String category;
    private SearchView searchList;
    private RelativeLayout tvEmpty;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskBeans = new ArrayList<>();
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        // Inflate the layout for this fragment
        lvTask = (ListView) view.findViewById(R.id.lv_task);
        searchList = (SearchView) view.findViewById(R.id.search_list);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        ivFilter = (ImageView) view.findViewById(R.id.iv_filter);
        ivFilter.setOnClickListener(this);
        tvEmpty = (RelativeLayout) view.findViewById(R.id.tv_empty);
        searchList.setOnQueryTextListener(this);
        lvTask.setOnItemClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        getCategory();
        return view;
    }

    private void getCategory() {
        String cat = "[";
        for (CategoryBean categoryBean : FilterActivity.categories) {
            if (categoryBean.isSelect()) {
                cat = cat + categoryBean.getId() + ",";
            }
        }
        if (!cat.equalsIgnoreCase("[")) {
            cat = cat.substring(0, cat.length() - 1);
        }
        cat = cat + "]";
        category = cat;
        distance = FilterActivity.distance;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_filter:
                Intent intent = new Intent(mContext, FilterActivity.class);
                startActivityForResult(intent, FILTER_CODE);
                mContext.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                distance = data.getIntExtra(Constants.DISTANCE, 0);
                category = data.getStringExtra(Constants.CATEGORIES);
                Log.d("", "");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
        Location location = new LocationTrack(getActivity()).getLocation();
        if (NetworkRecognizer.isNetworkAvailable(mContext)) {
            if (location != null) {
                ((HomeActivity) mContext).showProgress();
                ApiHelper.getInstance().getAllTask(token, location.getLatitude() + "", location.getLongitude() + "", distance + "", category, new ApiCallback.TasksListener() {
                    @Override
                    public void onSuccess(List<TaskBean> tasks1) {
                        taskBeans = tasks1;
                        taskAdapter = new TaskAdapter(mContext, taskBeans);
                        lvTask.setAdapter(taskAdapter);
                        lvTask.setEmptyView(tvEmpty);
                        ((HomeActivity) mContext).dismissProgress();
                    }

                    @Override
                    public void onFailure(String error) {
                        taskAdapter = new TaskAdapter(mContext, taskBeans);
                        lvTask.setAdapter(taskAdapter);
                        lvTask.setEmptyView(tvEmpty);
                        ((HomeActivity) mContext).dismissProgress();
                    }
                });
            }
        } else {
            ((HomeActivity) mContext).showNetwork();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskBean taskBean = (TaskBean) parent.getAdapter().getItem(position);
        Fragment fragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TASK, taskBean);
        HomeActivity.FROM_WHERE = 1;
        fragment.setArguments(bundle);
        ((HomeActivity) mContext).loadFragment(fragment);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        taskAdapter.getFilter().filter(newText.toString());
        return false;
    }

    @Override
    public void onRefresh() {
        String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
        Location location = new LocationTrack(getActivity()).getLocation();
        if (NetworkRecognizer.isNetworkAvailable(mContext)) {
            if (location != null) {
                ApiHelper.getInstance().getAllTask(token, location.getLatitude() + "", location.getLongitude() + "", distance + "", category, new ApiCallback.TasksListener() {
                    @Override
                    public void onSuccess(List<TaskBean> tasks1) {
                        taskBeans = tasks1;
                        taskAdapter = new TaskAdapter(mContext, taskBeans);
                        lvTask.setAdapter(taskAdapter);
                        lvTask.setEmptyView(tvEmpty);
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(String error) {
                        taskAdapter = new TaskAdapter(mContext, taskBeans);
                        lvTask.setAdapter(taskAdapter);
                        lvTask.setEmptyView(tvEmpty);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        } else {
            ((HomeActivity) mContext).showNetwork();
            swipeRefresh.setRefreshing(false);
        }
    }
}

