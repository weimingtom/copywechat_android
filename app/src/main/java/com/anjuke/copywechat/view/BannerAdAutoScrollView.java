package com.anjuke.copywechat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/20
 */
public class BannerAdAutoScrollView extends FrameLayout{
    private List<ImageView> imageViewsList;
    private List<ImageView> dotViewsList;
    private ViewPager viewPager;
    private LinearLayout activity_anim_activity_for_ad_dotly;
    private int currentItem  = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private List<String> imagePathList;
    private boolean isAutoPlay = false;
    private Context context;
    private ImagePathInterfaceForBanner imagePathInterfaceForBanner;
    private View mainLayout;

    private int height;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    //Handler
    private Handler handler2 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public BannerAdAutoScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public void show(){
        if(imagePathInterfaceForBanner!=null)
            imagePathList = imagePathInterfaceForBanner.getImgPath();
        init(context);
    }

    public BannerAdAutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BannerAdAutoScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void init(Context context){

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);
        mainLayout = inflater.inflate(R.layout.view_ajk_conversation_banner_ad, this);
        activity_anim_activity_for_ad_dotly = (LinearLayout)findViewById(R.id.activity_anim_activity_for_ad_dotly);

        ImageView bannerCloseIv = (ImageView)findViewById(R.id.banner_close_btn);
        bannerCloseIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerAdAutoScrollView.this.setVisibility(GONE);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.viewpager_for_ad_vp);


        initArrays();


        viewPager.setFocusable(true);
        viewPager.setAdapter(MyAdapterLong);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1);

        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        startScroll();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        stopScroll();
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
                Log.i("BannerAutoScrollView", "onPageSelected position is :"+position+" imageViewsList.size():"+imageViewsList.size());

                currentItem = position;

                int dpos = position;
                if(position<1) {
                    dpos = imageViewsList.size()-2;
                }else if(position>(imageViewsList.size()-2)){
                    dpos = 1;
                }
                for(ImageView iv:dotViewsList){
                    iv.setBackgroundResource(R.drawable.anjuke61_icon10_slt);
                }
                dotViewsList.get((dpos-1)%dotViewsList.size()).setBackgroundResource(R.drawable.anjuke61_icon10);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:// 手势滑动，空闲中
                        break;
                    case 2:// 界面切换中
                        break;
                    case 0:
                        int position = currentItem;
                        if(position<1) {
                            position = imageViewsList.size()-2;
                            viewPager.setCurrentItem(position, false);
                        }else if(position>(imageViewsList.size()-2)){
                            position = 1;
                            viewPager.setCurrentItem(position, false);
                        }
                }
            }
        });
        startScroll();
    }


    public void initArrays(){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsvg = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        //init arrays
        if(imagePathList==null){    //加载默认图片
            ImageView viewPic3 =  new ImageView(context);
            viewPic3.setTag("");
            viewPic3.setBackgroundResource(R.drawable.pic1_for_viewpager_banner3);
            viewPic3.setScaleType(ImageView.ScaleType.FIT_XY);
            viewPic3.setLayoutParams(layoutParamsvg);
            imageViewsList.add(viewPic3);
            ImageView viewPic1 =  new ImageView(context);
            viewPic1.setTag("");
            viewPic1.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
            viewPic1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(viewPic1);
            ImageView viewPic2 =  new ImageView(context);
            viewPic2.setTag("");
            viewPic2.setBackgroundResource(R.drawable.pic1_for_viewpager_banner2);
            viewPic2.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(viewPic2);

            ImageView viewPic3_2 =  new ImageView(context);
            viewPic3_2.setTag("");
            viewPic3_2.setBackgroundResource(R.drawable.pic1_for_viewpager_banner3);
            viewPic3_2.setScaleType(ImageView.ScaleType.FIT_XY);
            //imageViewsList.add(viewPic3);
            imageViewsList.add(viewPic3_2);

            ImageView viewPic1_1 =  new ImageView(context);
            viewPic1_1.setTag("");
            viewPic1_1.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
            viewPic1_1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(viewPic1_1);
            ImageView viewPic1_2 =  new ImageView(context);
            viewPic1_2.setTag("");
            viewPic1_2.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
            viewPic1_2.setScaleType(ImageView.ScaleType.FIT_XY);

            ImageView dotViewPic =  new ImageView(context);
            dotViewPic.setTag("");
            dotViewPic.setBackgroundResource(R.drawable.focus_dot);
            dotViewPic.setScaleType(ImageView.ScaleType.FIT_XY);
            dotViewsList.add(dotViewPic);
            activity_anim_activity_for_ad_dotly.addView(dotViewPic,layoutParams);

            ImageView dotViewPic2 =  new ImageView(context);
            dotViewPic2.setTag("");
            dotViewPic2.setBackgroundResource(R.drawable.no_focus_dot);
            dotViewPic2.setScaleType(ImageView.ScaleType.FIT_XY);
            dotViewsList.add(dotViewPic2);
            activity_anim_activity_for_ad_dotly.addView(dotViewPic2,layoutParams);
            ImageView dotViewPic3 =  new ImageView(context);
            dotViewPic3.setTag("");
            dotViewPic3.setBackgroundResource(R.drawable.no_focus_dot);
            dotViewPic3.setScaleType(ImageView.ScaleType.FIT_XY);
            dotViewsList.add(dotViewPic3);
            activity_anim_activity_for_ad_dotly.addView(dotViewPic3,layoutParams);
        }else {


            for(int i=0; i<imagePathList.size(); i++){
                if(i==0){
                    ImageView viewPic_l =  new ImageView(context);
                    viewPic_l.setAdjustViewBounds(true);
                    viewPic_l.setTag("");
                    //viewPic_l.setBackgroundResource(R.drawable.pic1_for_viewpager_banner3);
                    viewPic_l.setLayoutParams(layoutParamsvg);
                    imageLoader.displayImage(imagePathList.get(imagePathList.size()-1), viewPic_l);
                    viewPic_l.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageViewsList.add(viewPic_l);
                }
                ImageView viewPic_ll =  new ImageView(context);
                viewPic_ll.setAdjustViewBounds(true);
                viewPic_ll.setTag("");
                viewPic_ll.setLayoutParams(layoutParamsvg);
                //viewPic_ll.setBackgroundResource(R.drawable.pic1_for_viewpager_banner3);
                imageLoader.displayImage(imagePathList.get(i), viewPic_ll);
                viewPic_ll.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViewsList.add(viewPic_ll);

                ImageView dotViewPic_ll =  new ImageView(context);
                dotViewPic_ll.setTag("");
                dotViewPic_ll.setBackgroundResource(R.drawable.anjuke61_icon10_slt);
                dotViewPic_ll.setScaleType(ImageView.ScaleType.FIT_XY);
                dotViewsList.add(dotViewPic_ll);
                activity_anim_activity_for_ad_dotly.addView(dotViewPic_ll,layoutParams);
                if(i==0)
                    dotViewPic_ll.setBackgroundResource(R.drawable.anjuke61_icon10);

                if(i==imagePathList.size()-1){
                    ImageView viewPic_l =  new ImageView(context);
                    viewPic_l.setAdjustViewBounds(true);
                    viewPic_l.setTag("");
                    viewPic_l.setLayoutParams(layoutParamsvg);
                    //viewPic_l.setBackgroundResource(R.drawable.pic1_for_viewpager_banner1);
                    imageLoader.displayImage(imagePathList.get(0), viewPic_l);
                    viewPic_l.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageViewsList.add(viewPic_l);
                }
            }
        }
    }

    public  void stopScroll(){
        if(scheduledExecutorService!=null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService=null;
        }
    }

    public void startScroll(){
        stopScroll();

        if(scheduledExecutorService==null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
        }
    }

    private PagerAdapter MyAdapterLong = new PagerAdapter() {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewsList.get(position);

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

    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler2.obtainMessage().sendToTarget();
            }
        }
    }

    public ImagePathInterfaceForBanner getImagePathInterfaceForBanner() {
        return imagePathInterfaceForBanner;
    }

    public void setImagePathInterfaceForBanner(ImagePathInterfaceForBanner imagePathInterfaceForBanner) {
        this.imagePathInterfaceForBanner = imagePathInterfaceForBanner;
    }

    public List<String> getImagePathList() {
        return imagePathList;
    }

    public void setImagePathList(List<String> imagePathList) {
        this.imagePathList = imagePathList;
    }
}
