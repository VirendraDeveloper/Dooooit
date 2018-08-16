package cl.activaresearch.android_app.Dooit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.adapter.SlideAdapter;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class SlideShowActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private Integer[] images = {R.layout.slide1, R.layout.slide2, R.layout.slide3, R.layout.slide4};
    private ImageView ivDot1, ivDot2, ivDot3, ivDot4;
    private TextView tvJoin, tvLogin;
    private SlideAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        initUI();
        viewPagerAdapter = new SlideAdapter(this, images);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ivDot1 = (ImageView) findViewById(R.id.tv_dot1);
        ivDot2 = (ImageView) findViewById(R.id.tv_dot2);
        ivDot3 = (ImageView) findViewById(R.id.tv_dot3);
        ivDot4 = (ImageView) findViewById(R.id.tv_dot4);
        tvJoin = (TextView) findViewById(R.id.tv_join);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvJoin.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    private void unSelectDot() {
        ivDot1.setImageResource(R.drawable.ic_white);
        ivDot2.setImageResource(R.drawable.ic_white);
        ivDot3.setImageResource(R.drawable.ic_white);
        ivDot4.setImageResource(R.drawable.ic_white);
    }

    private void selectDot(int dot) {
        switch (dot) {
            case 0:
                ivDot1.setImageResource(R.drawable.ic_blue);
                break;
            case 1:
                ivDot2.setImageResource(R.drawable.ic_blue);
                break;
            case 2:
                ivDot3.setImageResource(R.drawable.ic_blue);
                break;
            case 3:
                ivDot4.setImageResource(R.drawable.ic_blue);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_join:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        unSelectDot();
        selectDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
