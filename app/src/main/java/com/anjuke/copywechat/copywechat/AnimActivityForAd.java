package com.anjuke.copywechat.copywechat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.anjuke.copywechat.view.BannerAutoScrollView;
import com.anjuke.copywechat.view.ImagePathInterfaceForBanner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnimActivityForAd extends Activity {

    private Animation anim_left_in;
    private Animation anim_right_out;
    private TextView titleTv;
    private boolean in_left=true;

    private List<ImageView> imageViewsList;
    private List<ImageView> dotViewsList;
    private ViewPager viewPager;
    private boolean isAutoPlay = false;
    private LinearLayout activity_anim_activity_for_ad_dotly;
    private boolean changeDirection=false;

    private ScheduledExecutorService scheduledExecutorService;

    //Handler
    private Handler handler2 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    private int currentItem  = 0;

    private String[] imageUrls = new String[]{
        "http://image.zcool.com.cn/56/35/1303967876491.jpg",
                "http://image.zcool.com.cn/59/54/m_1303967870670.jpg",
                "http://image.zcool.com.cn/47/19/1280115949992.jpg",
                "http://image.zcool.com.cn/59/11/m_1303967844788.jpg"
    };

    private BannerAutoScrollView headlines_view_vp;

    private PagerAdapter MyAdapterLong = new PagerAdapter() {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            Log.i(ConstantCls.LOG_DEBUG_TAG, "viewpager destroyItem");
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i(ConstantCls.LOG_DEBUG_TAG, "viewpager instantiateItem position is :"+position);
            ImageView imageView = imageViewsList.get(position);

            ViewGroup vg = (ViewGroup)imageView.getParent();
            /*if(vg==null) {
                container.addView(imageView);
            }*/
            container.addView(imageView);
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_activity_for_ad);

        anim_left_in = AnimationUtils.loadAnimation(this,R.anim.left_in);
        anim_right_out = AnimationUtils.loadAnimation(this,R.anim.right_out);
        titleTv = (TextView) findViewById(R.id.viewpager_for_ad_title_tv);

        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();

        ImageView viewPic3 =  new ImageView(this);
        viewPic3.setTag(imageUrls[1]);
        viewPic3.setBackgroundResource(R.drawable.pic1_for_viewpager_banner3);
        viewPic3.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewsList.add(viewPic3);
        ImageView viewPic1 =  new ImageView(this);
        viewPic1.setTag(imageUrls[0]);
        viewPic1.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
        viewPic1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewsList.add(viewPic1);
        ImageView viewPic2 =  new ImageView(this);
        viewPic2.setTag(imageUrls[1]);
        viewPic2.setBackgroundResource(R.drawable.pic1_for_viewpager_banner2);
        viewPic2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewsList.add(viewPic2);

        ImageView viewPic3_2 =  new ImageView(this);
        viewPic3_2.setTag(imageUrls[1]);
        viewPic3_2.setBackgroundResource(R.drawable.pic1_for_viewpager_banner3);
        viewPic3_2.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageViewsList.add(viewPic3);
        imageViewsList.add(viewPic3_2);

        ImageView viewPic1_1 =  new ImageView(this);
        viewPic1_1.setTag(imageUrls[0]);
        viewPic1_1.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
        viewPic1_1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewsList.add(viewPic1_1);
        ImageView viewPic1_2 =  new ImageView(this);
        viewPic1_2.setTag(imageUrls[0]);
        viewPic1_2.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
        viewPic1_2.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageViewsList.add(viewPic1_2);

        activity_anim_activity_for_ad_dotly = (LinearLayout)findViewById(R.id.activity_anim_activity_for_ad_dotly);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        ImageView dotViewPic =  new ImageView(this);
        dotViewPic.setTag(imageUrls[2]);
        dotViewPic.setBackgroundResource(R.drawable.focus_dot);
        dotViewPic.setScaleType(ImageView.ScaleType.FIT_XY);
        dotViewsList.add(dotViewPic);
        activity_anim_activity_for_ad_dotly.addView(dotViewPic,layoutParams);

        ImageView dotViewPic2 =  new ImageView(this);
        dotViewPic2.setTag(imageUrls[3]);
        dotViewPic2.setBackgroundResource(R.drawable.no_focus_dot);
        dotViewPic2.setScaleType(ImageView.ScaleType.FIT_XY);
        dotViewsList.add(dotViewPic2);
        activity_anim_activity_for_ad_dotly.addView(dotViewPic2,layoutParams);
        ImageView dotViewPic3 =  new ImageView(this);
        dotViewPic3.setTag(imageUrls[3]);
        dotViewPic3.setBackgroundResource(R.drawable.no_focus_dot);
        dotViewPic3.setScaleType(ImageView.ScaleType.FIT_XY);
        dotViewsList.add(dotViewPic3);
        activity_anim_activity_for_ad_dotly.addView(dotViewPic3,layoutParams);

        viewPager = (ViewPager)findViewById(R.id.viewpager_for_ad);
        viewPager.setFocusable(true);
        viewPager.setAdapter(MyAdapterLong);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        scheduledExecutorService.shutdown();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*if(position==(dotViewsList.size()+1)) {
                    viewPager.setCurrentItem(1);
                    currentItem = 1;
                }*/

                Log.i(ConstantCls.LOG_DEBUG_TAG, "onPageSelected position is :"+position+" imageViewsList.size():"+imageViewsList.size());
                /*
                if(position<1) {
                     position = imageViewsList.size()-2;
                     viewPager.setCurrentItem(position, false);
                }else if(position>(imageViewsList.size()-2)){
                    Log.i(ConstantCls.LOG_DEBUG_TAG,"执行到这里!!!!");
                    position = 1;
                    viewPager.setCurrentItem(position, false);
                }*/
                currentItem = position;

                int dpos = position;
                if(position<1) {
                    dpos = imageViewsList.size()-2;
                }else if(position>(imageViewsList.size()-2)){
                    dpos = 1;
                }
                for(ImageView iv:dotViewsList){
                    iv.setBackgroundResource(R.drawable.no_focus_dot);
                }
                dotViewsList.get((dpos-1)%dotViewsList.size()).setBackgroundResource(R.drawable.focus_dot);

                /*
                for(ImageView iv:dotViewsList){
                    iv.setBackgroundResource(R.drawable.no_focus_dot);
                }
                dotViewsList.get((dotPosition-1)%dotViewsList.size()).setBackgroundResource(R.drawable.focus_dot);
                int dotPosition=(position<1)?imageViewsList.size()-2:position;
                dotPosition = (dotPosition>imageViewsList.size()-2)?1:dotPosition;*/
                //dotViewsList.get((position-1)%dotViewsList.size()).setBackgroundResource(R.drawable.focus_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:// 手势滑动，空闲中
                        isAutoPlay = false;
                        break;
                    case 2:// 界面切换中
                        isAutoPlay = true;
                        break;
                    case 0:
                        int position = currentItem;
                        if(position<1) {
                            position = imageViewsList.size()-2;
                            viewPager.setCurrentItem(position, false);
                        }else if(position>(imageViewsList.size()-2)){
                            Log.i(ConstantCls.LOG_DEBUG_TAG,"执行到这里!!!!");
                            position = 1;
                            viewPager.setCurrentItem(position, false);
                        }
                        /*if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                            viewPager.setCurrentItem(1, false);
                        }else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                            viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 2, false);
                        }*/
                }
            }
        });
        //handler.postDelayed(runnable, 1000);
        //new SchedulerTask().execute(100);


        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);

        headlines_view_vp = (BannerAutoScrollView)findViewById(R.id.headlines_view_vp);
        headlines_view_vp.setImagePathInterfaceForBanner(new ImagePathInterfaceForBanner() {
            @Override
            public List<String> getImgPath() {
                List<String> imgs = new ArrayList<>();
                imgs.add("http://10.249.13.32/imgs/pic1_for_viewpager_banner1.png");
                imgs.add("http://10.249.13.32/imgs/pic1_for_viewpager_banner2.png");
                imgs.add("http://10.249.13.32/imgs/pic1_for_viewpager_banner3.png");
                imgs.add("http://10.249.13.32/imgs/pic1_for_viewpager_banner4.png");
                return imgs;
            }
        });
        headlines_view_vp.show();
    }

    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler2.obtainMessage().sendToTarget();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        headlines_view_vp.startScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        headlines_view_vp.stopScroll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        headlines_view_vp.stopScroll();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        headlines_view_vp.startScroll();
    }
}
