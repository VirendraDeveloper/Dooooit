package cl.activaresearch.android_app.Dooit.activities.questions;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonObject;

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

public class SingleLineActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private TextView tvTitle, tvCancel, tvOk, tvQuestion;
    private SurveyBean surveyBean;
    private EditText edtAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_line);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvQuestion = (TextView) findViewById(R.id.tv_question_name);
        edtAnswer = (EditText) findViewById(R.id.edt_answer);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        edtAnswer.addTextChangedListener(this);
        Intent intent = getIntent();
        surveyBean = (SurveyBean) intent.getSerializableExtra(Constants.SURVEY);
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        if (surveyBean != null) {
            tvTitle.setText(intent.getStringExtra(Constants.TITLE));
            tvQuestion.setText(surveyBean.getQuestion());
            if (surveyBean.getAnswer() != null) {
                edtAnswer.setText(surveyBean.getAnswer().getData());
                edtAnswer.setSelection(surveyBean.getAnswer().getData().length());
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
                String ans = edtAnswer.getText().toString().trim();
                JsonObject body = new JsonObject();
                body.addProperty("answer", ans);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String strAns = edtAnswer.getText().toString().trim();
        if (!strAns.equalsIgnoreCase("")) {
            tvOk.setEnabled(true);
            tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            tvOk.setEnabled(false);
            tvOk.setTextColor(getResources().getColor(R.color.colorLight));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
