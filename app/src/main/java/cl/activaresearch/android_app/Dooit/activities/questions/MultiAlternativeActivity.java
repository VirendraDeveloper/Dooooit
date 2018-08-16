package cl.activaresearch.android_app.Dooit.activities.questions;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.BaseActivity;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;
/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jun,2018
 */

public class MultiAlternativeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tvTitle, tvCancel, tvOk, tvName;
    private SurveyBean surveyBean;
    private List<SurveyBean.AlternativesBean> alternative;
    private ListView lvAlternatives;
    private AlternativeAdapter mAdapter;
    private String strTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_alternative);
        surveyBean = (SurveyBean) getIntent().getSerializableExtra(Constants.SURVEY);
        alternative = surveyBean.getAlternatives();
        initUI();
        mAdapter = new AlternativeAdapter(this);
        lvAlternatives.setAdapter(mAdapter);
        lvAlternatives.setOnItemClickListener(this);
    }

    private void initUI() {
        lvAlternatives = (ListView) findViewById(R.id.lv_alternatives);
        tvName = (TextView) findViewById(R.id.tv_question_name);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        if (surveyBean != null) {
            tvTitle.setText(strTitle);
            tvName.setText(surveyBean.getQuestion());
            if (surveyBean.getAnswer() != null) {
                SurveyBean.AnswerBean bean = surveyBean.getAnswer();
                List<Integer> bean1 = bean.getDataBean();
                for (int i = 0; i < alternative.size(); i++) {
                    SurveyBean.AlternativesBean alternativesBean = alternative.get(i);
                    for (int j = 0; j < bean1.size(); j++) {
                        Integer integer = bean1.get(j);
                        if (alternativesBean.getId() == integer) {
                            alternativesBean.setAnswered(true);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                String token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
                JsonObject body = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                List<SurveyBean.AlternativesBean> alternatives = surveyBean.getAlternatives();
                for (int i = 0; i < alternatives.size(); i++) {
                    SurveyBean.AlternativesBean bean = alternatives.get(i);
                    JsonObject answer = new JsonObject();
                    if (bean.isAnswered()) {
                        answer.addProperty("alternative", bean.getId() + "");
                        jsonArray.add(answer);
                    }
                }
                body.add("answer", jsonArray);
                showProgress();
                ApiHelper.getInstance().ansSurveyQuestion(token, surveyBean.getTaskId(), surveyBean.getId(), body, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                        dismissProgress();
                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SurveyBean.AlternativesBean alternativesBean = alternative.get(position);
        if (alternativesBean.isAnswered()) {
            alternativesBean.setAnswered(false);
        } else {
            alternativesBean.setAnswered(true);
        }
        List<SurveyBean.AlternativesBean> beans = surveyBean.getAlternatives();
        for (int i = 0; i < beans.size(); i++) {
            SurveyBean.AlternativesBean bean = beans.get(i);
            if (bean.isAnswered()) {
                tvOk.setEnabled(true);
                tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    class AlternativeAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private Activity mContext;

        public AlternativeAdapter(Activity mContext) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return alternative.size();
        }

        @Override
        public Object getItem(int i) {
            return alternative.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.multi_alternative_row, parent, false);
                mViewHolder = new ViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            final SurveyBean.AlternativesBean alternativesBean = alternative.get(i);
            mViewHolder.tvName.setText(alternativesBean.getText());
            if (alternativesBean.isAnswered()) {
                mViewHolder.ivCheck.setImageResource(R.mipmap.ic_chk_on);
            } else {
                mViewHolder.ivCheck.setImageResource(R.mipmap.ic_chk_off);
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView tvName;
            private ImageView ivCheck;

            public ViewHolder(View item) {
                tvName = (TextView) item.findViewById(R.id.tv_answer_name);
                ivCheck = (ImageView) item.findViewById(R.id.iv_alter);
            }
        }
    }
}
