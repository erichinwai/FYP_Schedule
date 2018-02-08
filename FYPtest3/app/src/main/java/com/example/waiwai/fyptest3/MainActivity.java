package com.example.waiwai.fyptest3;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<View> listViews;
    private ImageView cursorIv;
    private TextView tab01, tab02, tab03;
    private TextView[] titles;
    private ViewPager viewPager;

    private int offset = 0;

    private int lineWidth;

    private int current_index = 0;

    private static final int TAB_COUNT = 3;

    private static final int TAB_0 = 0;

    private static final int TAB_1 = 1;

    private static final int TAB_2 = 2;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navigationView.setCheckedItem(R.id.your_schedule);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        initUI();
        initImageView();
        initVPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Toast.makeText(this, "clicked search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "clicked setting", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.vPager);
        cursorIv = (ImageView) findViewById(R.id.iv_tab_bottom_img);
        tab01 = (TextView) findViewById(R.id.tv01);
        tab02 = (TextView) findViewById(R.id.tv02);
        tab03 = (TextView) findViewById(R.id.tv03);

        tab01.setOnClickListener(this);
        tab02.setOnClickListener(this);
        tab03.setOnClickListener(this);
    }

    private void initImageView() {
        lineWidth = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name).getWidth();
        // Android提供的DisplayMetrics可以很方便的获取屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels; // 获取分辨率宽度
        offset = (screenW / TAB_COUNT - lineWidth) / 2;  // 计算偏移值
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        // 设置下划线初始位置
        cursorIv.setImageMatrix(matrix);
    }

    private void initVPager() {
        listViews = new ArrayList<>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.recom_layout, null));
        listViews.add(mInflater.inflate(R.layout.current_layout, null));
        listViews.add(mInflater.inflate(R.layout.create_layout, null));
        viewPager.setAdapter(new MyPagerAdapter(listViews));
        viewPager.setCurrentItem(0);
        titles = new TextView[]{tab01, tab02, tab03};
        viewPager.setOffscreenPageLimit(titles.length);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int one = offset * 2 + lineWidth;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 下划线开始移动前的位置
                float fromX = one * current_index;
                // 下划线移动完毕后的位置
                float toX = one * position;
                Animation animation = new TranslateAnimation(fromX, toX, 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                // 给图片添加动画
                cursorIv.startAnimation(animation);
                // 当前Tab的字体变成红色
                titles[position].setTextColor(Color.RED);
                titles[current_index].setTextColor(Color.BLACK);
                current_index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public class MyPagerAdapter extends PagerAdapter {

        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv01:
                if (viewPager.getCurrentItem() != TAB_0) {
                    viewPager.setCurrentItem(TAB_0);
                }
                break;
            case R.id.tv02:
                if (viewPager.getCurrentItem() != TAB_1) {
                    viewPager.setCurrentItem(TAB_1);
                }
                break;
            case R.id.tv03:
                if (viewPager.getCurrentItem() != TAB_2) {
                    viewPager.setCurrentItem(TAB_2);
                }
                break;
        }
    }
}