package cl.activaresearch.android_app.Dooit.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.CategoryBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiClient;
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
public class FilterActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private TextView tvCancel, tvOk, tvDistance;
    private CategoryAdapter mAdapter;
    private ListView lvCategory;
    private SeekBar skbDistance;
    public static int distance = 200;
    public static List<CategoryBean> categories = new ArrayList<>();
    private ImageView ivAllSelect;
    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initUI();
        mAdapter = new CategoryAdapter(FilterActivity.this);
        lvCategory.setAdapter(mAdapter);
        if (categories.size() < 1) {
            showProgress();
            ApiHelper.getInstance().getAllCategory(SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN), new ApiCallback.CategoryListener() {

                @Override
                public void onSuccess(List<CategoryBean> categories1) {
                    distance = 200;
                    categories = categories1;
                    for (CategoryBean bean : categories) {
                        bean.setSelect(true);
                    }
                    isAllSelected = true;
                    mAdapter.notifyDataSetChanged();
                    dismissProgress();
                }

                @Override
                public void onFailure(String error) {
                    showToast(error);
                }
            });
        }
    }

    private void initUI() {
        lvCategory = (ListView) findViewById(R.id.lv_categories);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        ivAllSelect = (ImageView) findViewById(R.id.iv_all_category);
        skbDistance = (SeekBar) findViewById(R.id.skb_distance);
        skbDistance.setOnSeekBarChangeListener(this);
        tvOk.setOnClickListener(this);
        ivAllSelect.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        skbDistance.setProgress(distance);
        checkAllSelected();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        distance = progress;
        tvDistance.setText(progress + "km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.iv_all_category:
                if (!isAllSelected) {
                    isAllSelected = true;
                    ivAllSelect.setImageResource(R.mipmap.ic_check_all);
                } else {
                    isAllSelected = false;
                    ivAllSelect.setImageResource(R.drawable.white_circle);
                }
                for (CategoryBean categoryBean : categories) {
                    categoryBean.setSelect(isAllSelected);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_ok:
                String cat = "[";
                for (CategoryBean categoryBean : categories) {
                    if (categoryBean.isSelect()) {
                        cat = cat + categoryBean.getId() + ",";
                    }
                }
                if (!cat.equalsIgnoreCase("[")) {
                    cat = cat.substring(0, cat.length() - 1);
                }
                cat = cat + "]";
                Intent data = new Intent();
                data.putExtra(Constants.DISTANCE, distance);
                data.putExtra(Constants.CATEGORIES, cat);
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }

    public class CategoryAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private Activity mContext;

        public CategoryAdapter(Activity mContext) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int i) {
            return categories.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            final ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.category_row, parent, false);
                mViewHolder = new ViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            final CategoryBean categoryBean = categories.get(i);
            mViewHolder.tvCategory.setText(categoryBean.getName());
            if (!categoryBean.getIconPath().equalsIgnoreCase("")) {
                Glide.with(mContext)
                        .load(ApiClient.BASE_URL + categoryBean.getIconPath())
                        .into(mViewHolder.ivCategory);
            }

            GradientDrawable bgShape = (GradientDrawable) mViewHolder.rlCategory.getBackground();
            bgShape.setColor(Color.parseColor(categoryBean.getColor()));

            if (categoryBean.isSelect()) {
                mViewHolder.ivSelect.setImageResource(R.mipmap.ic_check_all);
            } else {
                mViewHolder.ivSelect.setImageResource(R.drawable.white_circle);
            }

            mViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!categoryBean.isSelect()) {
                        categoryBean.setSelect(true);
                        mViewHolder.ivSelect.setImageResource(R.mipmap.ic_check_all);
                    } else {
                        categoryBean.setSelect(false);
                        mViewHolder.ivSelect.setImageResource(R.drawable.white_circle);
                    }
                    checkAllSelected();
                }
            });
            return convertView;
        }


        private class ViewHolder {
            private TextView tvCategory;
            private ImageView ivCategory, ivSelect;
            private RelativeLayout rlCategory;

            public ViewHolder(View item) {
                tvCategory = (TextView) item.findViewById(R.id.tv_category);
                ivCategory = (ImageView) item.findViewById(R.id.iv_category);
                ivSelect = (ImageView) item.findViewById(R.id.iv_check_category);
                rlCategory = (RelativeLayout) item.findViewById(R.id.rl_category);
            }
        }
    }

    private void checkAllSelected() {
        isAllSelected = true;
        for (CategoryBean ca : categories) {
            if (!ca.isSelect()) {
                isAllSelected = false;
            }
        }
        if (isAllSelected) {
            ivAllSelect.setImageResource(R.mipmap.ic_check_all);
        } else {
            ivAllSelect.setImageResource(R.drawable.white_circle);
        }
    }

}