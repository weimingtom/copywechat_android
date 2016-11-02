package com.anjuke.copywechat.copywechat;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anjuke.copywechat.copywechat.fragements.FourFragment;
import com.anjuke.copywechat.copywechat.fragements.OneFragment;
import com.anjuke.copywechat.copywechat.fragements.ThreeFragment;
import com.anjuke.copywechat.copywechat.fragements.TwoFragment;
import com.anjuke.copywechat.copywechat.fragements.interfaces.OneFragmentInterface;
import com.anjuke.copywechat.copywechat.imageviews.RoundImageView;
import com.anjuke.copywechat.copywechat.util.ConstantCls;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KeyEvent.Callback{

    private ViewPager pager = null;
    private PagerTabStrip pagerTabStrip = null;

    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;

    private ImageView imageViewOverTab;

    private RoundImageView roundImageView;

    private LinearLayout layout_menuforprofile;
    private RelativeLayout rlayout_maincontent;

    int screenWidth;

    ArrayList<View> viewContainer = new ArrayList<View>();
    ArrayList<String> titleContainer = new ArrayList<String>();

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();


    //当前选中的项
    int currenttab=-1;


    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;
    private ImageView ivRunningMan;
    private AnimationDrawable mAnimationDrawable;

    private int pageoffsetStartforViewpager=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.activity_main_custome_title);


        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);

       // mAnimationDrawable = (AnimationDrawable) rlayout_maincontent.getBackground();
        //mAnimationDrawable.start();
        toolbar.setTitle("仿微信应用");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //mAnimationDrawable.stop();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //mAnimationDrawable.start();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        roundImageView = (RoundImageView)findViewById(R.id.roundImage_network);
        roundImageView.setOnClickListener(this);

        layout_menuforprofile = (LinearLayout)findViewById(R.id.layout_menuforprofile);
        rlayout_maincontent = (RelativeLayout) findViewById(R.id.rlayout_maincontent);

        btnOne = (Button)findViewById(R.id.btn_one);
        btnTwo = (Button)findViewById(R.id.btn_two);
        btnThree = (Button)findViewById(R.id.btn_three);
        btnFour = (Button)findViewById(R.id.btn_four);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);


        pager = (ViewPager) this.findViewById(R.id.id_main_viewpager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.i(ConstantCls.LOG_DEBUG_TAG, "positionOffsetPixels is :"+positionOffsetPixels);
                if(position==1) {
                    if (pageoffsetStartforViewpager>positionOffsetPixels) { //检查滑动方向
                        //TODO

                    } else if (pageoffsetStartforViewpager<positionOffsetPixels) {
                        //TODO

                    }
                }
                    pageoffsetStartforViewpager = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        OneFragment oneFragment = new OneFragment();
        TwoFragment twoFragment = new TwoFragment();
        ThreeFragment threeFragment = new ThreeFragment();
        FourFragment fourFragment = new FourFragment();
        fragmentArrayList.add(oneFragment);
        fragmentArrayList.add(twoFragment);
        fragmentArrayList.add(threeFragment);
        fragmentArrayList.add(fourFragment);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        btnTwo.measure(0, 0);

        imageViewOverTab = (ImageView)findViewById(R.id.imgv_overtab);

        DrawerLayout.LayoutParams rlayout_maincontentParams=new DrawerLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        rlayout_maincontent.setLayoutParams(rlayout_maincontentParams);

        RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(screenWidth/4, btnTwo.getMeasuredHeight());
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageViewOverTab.setLayoutParams(imageParams);

        pager.setAdapter(new MyFragementStatePagerAdapter(getSupportFragmentManager()));

        checkService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        // getMenuInflater().inflate(R.menu.activity_main_actions, menu);
        return true;
    }


    public void imageMove(int moveToTab){
        int startPos=0;
        int endPos=0;

        startPos = currenttab*(screenWidth/4);
        endPos = moveToTab*(screenWidth/4);


        //平移动画
        TranslateAnimation translateAnimation=new TranslateAnimation(startPos,endPos, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        imageViewOverTab.startAnimation(translateAnimation);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_one:
                changeView(0);
                break;
            case R.id.btn_two:
                changeView(1);
                break;
            case R.id.btn_three:
                changeView(2);
                break;
            case R.id.btn_four:
                changeView(3);
                break;
            case R.id.roundImage_network:
                Toast.makeText(this,"您点击了RoundImageView",Toast.LENGTH_LONG).show();
            default:
                break;
        }
    }

    public void changeView(int desTab){
        pager.setCurrentItem(desTab,true);
    }

    class MyFragementStatePagerAdapter extends FragmentStatePagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        public MyFragementStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void finishUpdate(ViewGroup container) {

            Log.e(ConstantCls.LOG_DEBUG_TAG+"s","调用finishupdate");
            super.finishUpdate(container);
            int currentItem = pager.getCurrentItem();
            if(currentItem==currenttab){
                return;
            }

            imageMove(pager.getCurrentItem());
            currenttab = pager.getCurrentItem();
        }
    }


    private long lastClickBackTime = 0; //记录最后一次点击back键的时间
    private Toast exitToast;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitapp();
                break;
        }
        return true;
    }

    // 程序退出时调用
    protected void exitapp() {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickBackTime < 2000) {
            finish();
        } else {
            if (exitToast == null) {
                exitToast = Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT);
            }
            exitToast.show();
        }
        lastClickBackTime = currentClickTime;
    }

    public void checkService(){
        ActivityManager myAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            Intent intent1 = new Intent(getApplicationContext(),GetServerMsgService.class);
            startService(intent1);
            return ;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals("GetServerMsgService")) {
                return;
            }
        }
        Intent intent1 = new Intent(getApplicationContext(),GetServerMsgService.class);
        startService(intent1);
    }
}
