package com.anjuke.copywechat.copywechat.fragements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.anjuke.copywechat.copywechat.ActionBarActivity;
import com.anjuke.copywechat.copywechat.ListViewHeaderTestActivity;
import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.ScrollTouchActivity;
import com.anjuke.copywechat.copywechat.WebViewActivity;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/5
 */
public class FourFragment extends Fragment {
    private View view_main;
    private boolean showdPopupWindow=false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view_main =  inflater.inflate(R.layout.activity_main_mtab4, null);

        final Button showpopupWindow = (Button) view_main.findViewById(R.id.tab4_show_popup_window_btn);

        View layoutview = getLayoutInflater(savedInstanceState).inflate(R.layout.tab4_popup_window_layout,null);
        Button btn1 = (Button) layoutview.findViewById(R.id.tab4_popup_window_btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        final PopupWindow popWin = new PopupWindow(layoutview, 280, 360);
        showpopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showdPopupWindow==false) {
                    popWin.showAsDropDown(view_main.findViewById(R.id.tab4_show_popup_window_btn), 20, 20);
                    popWin.showAtLocation(view_main, Gravity.CENTER, 20, 20);
                    showdPopupWindow=true;
                }else{
                    showdPopupWindow=false;
                    popWin.dismiss();
                }
            }
        });

        view_main.findViewById(R.id.tab4_show_a_webivew_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("weburl","http://www.baidu.com");
                startActivity(intent);
            }
        });

        view_main.findViewById(R.id.tab4_show_action_bar_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActionBarActivity.class);
                startActivity(intent);
            }
        });
        view_main.findViewById(R.id.tab4_show_viewpager_ad_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListViewHeaderTestActivity.class);
                startActivity(intent);
            }
        });

        view_main.findViewById(R.id.tab4_show_scroller_touch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScrollTouchActivity.class);
                startActivity(intent);
            }
        });
        return  view_main;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
