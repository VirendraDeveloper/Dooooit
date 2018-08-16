package cl.activaresearch.android_app.Dooit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 03 Jul,2018
 */
public class SurveyAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Activity mContext;
    private List<SurveyBean> surveyBeans;

    public SurveyAdapter(Activity mContext) {
        surveyBeans = new ArrayList<>();
        this.mContext = mContext;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void addAllTask(List<SurveyBean> tasks1) {
        surveyBeans.clear();
        surveyBeans.addAll(tasks1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return surveyBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return surveyBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.survey_row, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final SurveyBean surveyBean = surveyBeans.get(i);
        mViewHolder.tvName.setText(surveyBean.getQuestion());
        if (surveyBean.isAnswered()) {
            mViewHolder.ivCheck.setImageResource(R.mipmap.ic_check_all);
        } else {
            mViewHolder.ivCheck.setImageResource(R.drawable.white_circle);
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView tvName;
        private ImageView ivCheck;
        private View line2;

        public ViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.tv_title);
            ivCheck = (ImageView) item.findViewById(R.id.iv_check);
            line2 = (View) item.findViewById(R.id.line2);
        }
    }
}
