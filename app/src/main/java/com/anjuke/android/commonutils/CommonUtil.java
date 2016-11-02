
package com.anjuke.android.commonutils;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anjuke.copywechat.copywechat.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CommonUtil {

    private static Context mContext;
    private static CommonUtil _instance = null;

    public synchronized static CommonUtil getInstance(Context context) {

        if (_instance == null) {
            _instance = new CommonUtil(context);
        }
        return _instance;
    }

    private CommonUtil(Context context) {
        mContext = context;
    }

    /**
     * 为程序创建桌面快捷方式
     */
    public void addShortcut(int iconDrawableId) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        //快捷方式的名称  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); //不允许重复创建  

        /****************************此方法已失效*************************/
        //ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());  
        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));  　　
        /******************************** end *******************************/

        Intent shortcurIntent = new Intent(Intent.ACTION_MAIN);
        shortcurIntent.setClassName(mContext, mContext.getClass().getName());
        shortcurIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcurIntent);

        //快捷方式的图标  
        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext,
                iconDrawableId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        mContext.sendBroadcast(shortcut);
    }

    /**
     * 删除程序的快捷方式
     *
     * @param appClass
     *            要删除的快捷方式对应程序启动页面的class地址
     */
    public void delShortcut(String appClass) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getApplicationInfo().name);
        // 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        // 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
        ComponentName comp = new ComponentName(mContext.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        mContext.sendBroadcast(shortcut);
    }

    /**
     * 程序是否已经创建快捷方式
     *
     * @return
     */
    public boolean hasShortcut() {
        boolean isInstallShortcut = false;
        final ContentResolver cr = mContext.getContentResolver();
        // 2.2系统是”com.android.launcher2.settings”
        // 其他的为"com.android.launcher.settings"
        // final String AUTHORITY = "com.android.launcher.settings";
        final String AUTHORITY = "com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[] {
                "title"
        }, "title=?", new String[] {
                mContext.getApplicationInfo().name
        }, null);

        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }

    /**
     * 打开软键盘
     * @param view
     */
    public void openSoftInput(final View view) {
        // 打开软键盘
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
            }

        }, 500);
    }

    /**
     * 关闭软键盘
     */
    public void closeSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    /**
     * 效果：view组件上飘淡出文字<br>
     * 注意：view的父类需要是RelativeLayout 未留给控件过多展示空间的情况下效果不好或无法使用（如listview中的item）
     * @param view
     * @param text
     * @param textColorId
     * @param sp
     */
    public void startFlutter(final View view, CharSequence text , int textColorId, float sp){
        final TextView tv = new TextView(mContext);
        tv.setSingleLine(true);
        tv.setText(text);
        tv.setTextColor(mContext.getResources().getColor(textColorId));
        tv.setTextSize(sp);
        tv.setGravity(Gravity.CENTER);
        
        
        //初始化 Translate动画
        Animation translateAnimation = new TranslateAnimation(0,0,0, UIUtils.dip2Px(-75));
        translateAnimation.setDuration(1000);
        
        //初始化 Alpha动画
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1000);
        
        //动画集
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);
        set.addAnimation(alphaAnimation);
        set.setFillAfter(true);
        
        tv.startAnimation(set);
        
        MarginLayoutParams margin= new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(view.getLeft(), view.getTop(), view.getLeft()+view.getWidth(), view.getTop()+view.getHeight());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        tv.setLayoutParams(layoutParams); 
        tv.setHeight(view.getHeight());
        tv.setWidth(view.getWidth());
        ((ViewGroup)view.getParent()).addView(tv);
    }

    public static String listToStr(List<String> list){
        if(list == null || list.size() == 0)
            return "";
        StringBuilder str = new StringBuilder();
        for(int i = 0;i<list.size();i++){
            str.append(list.get(i));
            if(i != list.size() -1)
                str.append(",");
        }
        return str.toString();

    }
}
