package cl.activaresearch.android_app.Dooit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cl.activaresearch.android_app.Dooit.R;
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
public class InstructionsActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout sliderDotspanel;
    private TextView tvAccept, tvTitle, tvCancel;
    private ViewPager mViewPager;
    private String[] instructions = new String[3];
    private TaskBean taskBean;
    private String strToken;
    private Intent intent;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        initUI();
        InstructionAdapter mCustomPagerAdapter = new InstructionAdapter(instructions, this);
        mViewPager.setAdapter(mCustomPagerAdapter);
        dotscount = mCustomPagerAdapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            if (i == 0) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purple_circle));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gray_circle));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purple_circle));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == (instructions.length - 1)) {
                    tvAccept.setEnabled(true);
                    tvAccept.setTextColor(Color.WHITE);
                }
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gray_circle));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purple_circle));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initUI() {
        tvAccept = (TextView) findViewById(R.id.tv_accept);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.slider_dots);
        taskBean = (TaskBean) getIntent().getSerializableExtra(Constants.TASK);
        tvTitle.setText(taskBean.getNombre());
        try {
            if (taskBean.getStatus() > 1) {
                tvAccept.setVisibility(View.GONE);
            } else {
                tvAccept.setVisibility(View.VISIBLE);
            }
            if (taskBean.getPaso1() != null & !taskBean.getPaso1().equalsIgnoreCase("")) {
                instructions[0] = taskBean.getPaso1();
                if (taskBean.getPaso2() != null & !taskBean.getPaso2().equalsIgnoreCase("")) {
                    instructions[1] = taskBean.getPaso2();
                    if (taskBean.getPaso3() != null & !taskBean.getPaso3().equalsIgnoreCase("")) {
                        instructions[2] = taskBean.getPaso3();
                    }
                } else {
                    if (taskBean.getPaso3() != null & !taskBean.getPaso3().equalsIgnoreCase("")) {
                        instructions[1] = taskBean.getPaso3();
                    }
                }

            } else {
                if (taskBean.getPaso2() != null & !taskBean.getPaso2().equalsIgnoreCase("")) {
                    instructions[0] = taskBean.getPaso2();
                    if (taskBean.getPaso3() != null & !taskBean.getPaso3().equalsIgnoreCase("")) {
                        instructions[1] = taskBean.getPaso3();
                    }
                } else {
                    if (taskBean.getPaso3() != null & !taskBean.getPaso3().equalsIgnoreCase("")) {
                        instructions[0] = taskBean.getPaso3();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCancel.setOnClickListener(this);
        tvAccept.setOnClickListener(this);
        strToken = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_accept:
                showProgress();
                ApiHelper.getInstance().claimForTask(strToken, taskBean.getCod_tarea() + "", new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("", "");
                        finish();
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d("", "");
                        dismissProgress();
                        showToast(error);
                    }
                });
                break;
        }

    }

    class InstructionAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private String[] mResources;

        public InstructionAdapter(String[] mResources, Context context) {
            mContext = context;
            this.mResources = mResources;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.instruction_item, container, false);
            TextView tvInstruction = (TextView) itemView.findViewById(R.id.tv_instruction);
            tvInstruction.setText(mResources[position]);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

}
