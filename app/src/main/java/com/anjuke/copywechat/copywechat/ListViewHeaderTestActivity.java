package com.anjuke.copywechat.copywechat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anjuke.copywechat.view.BannerAdAutoScrollView;

import java.util.ArrayList;
import java.util.List;

public class ListViewHeaderTestActivity extends Activity {
    private BannerAdAutoScrollView headlines_view_vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_header_test);

        ListView showBannerView = (ListView) findViewById(R.id.banner_show_header_lv);
        headlines_view_vp = new BannerAdAutoScrollView(this);

        showBannerView.addHeaderView(headlines_view_vp);

        List<String> imgs = new ArrayList<>();
                /*imgs.add("http://10.249.13.7/imgs/pic1_for_viewpager_banner1.png");
                imgs.add("http://10.249.13.7/imgs/pic1_for_viewpager_banner2.png");
                imgs.add("http://10.249.13.7/imgs/pic1_for_viewpager_banner3.png");
                imgs.add("http://10.249.13.7/imgs/pic1_for_viewpager_banner4.png");*/
        imgs.add("http://pic1.ajkimg.com/m/d65ac8c7bca1799dc7d08d32d8ac755e/720x146x95.jpg");
        imgs.add("http://pic1.ajkimg.com/m/9b7f11559b10db0715554ce066c07be0/720x146x95.jpg");
        imgs.add("http://pic1.ajkimg.com/m/d65ac8c7bca1799dc7d08d32d8ac755e/720x146x95.jpg");
        imgs.add("http://pic1.ajkimg.com/m/9b7f11559b10db0715554ce066c07be0/720x146x95.jpg");

        headlines_view_vp.setImagePathList(imgs);

        headlines_view_vp.show();

        List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");

        showBannerView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,data));

        //ListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
