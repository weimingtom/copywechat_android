package com.anjuke.copywechat.copywechat.fragements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anjuke.copywechat.copywechat.ChatActivityTestFri;
import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.adapters.RecentChatAdapter;
import com.anjuke.copywechat.copywechat.fragements.interfaces.OneFragmentInterface;
import com.anjuke.copywechat.copywechat.model.RecentChatItem;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.anjuke.copywechat.view.BannerAdAutoScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:第一个Fragment，用于显示最近聊天
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/5
 */
public class OneFragment extends Fragment implements OneFragmentInterface {
    Button button;
    private Context mContext;
    private ListView mRecentChatLv;
    private RecentChatAdapter mRecentChatAdater;
    private List<RecentChatItem> mRecentChatList;
    private BannerAdAutoScrollView headlines_view_vp;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        JsonObjectRequest req = new JsonObjectRequest("http://"+ConstantCls.PUSH_SERVER_FOR_CHAT_IP+":"+ConstantCls.PUSH_SERVER_FOR_RESTFUL_PORT+"/spring4/hello2/mkyong.com",
                new Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = null;
                        try{
                            jsonObject = new JSONObject(response.toString());
                            Toast.makeText(getContext(), "获取到数据！！！！！！！！"+jsonObject.get("msg"), Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FragmentActivity activity = getActivity();
                        Toast.makeText(mContext, "无数据！！！！！！！！", Toast.LENGTH_SHORT).show();
                        VolleyLog.e("Error: ", error.getMessage());
                    }
        });

// add the request object to the queue to be executed
        Volley.newRequestQueue(getContext()).add(req);
        View view = inflater.inflate(R.layout.activity_main_mtab1, null);

        mRecentChatLv = (ListView)view.findViewById(R.id.recent_chat_lv);
        mRecentChatList = new ArrayList<>();
        for(int i=0; i<10; i++){

            RecentChatItem recentChatItem = new RecentChatItem();
            recentChatItem.setRecentMsg("聊天内容"+i);
            recentChatItem.setUserName("用户"+i);
            recentChatItem.setUserIcon("空");
            recentChatItem.setRecentChatTime("12:2");
            mRecentChatList.add(recentChatItem);
        }
        headlines_view_vp = (BannerAdAutoScrollView)view.findViewById(R.id.fragment_tab1_banner_bannerv);
        //mRecentChatLv.addHeaderView(headlines_view_vp);

        List<String> imgs = new ArrayList<>();
        imgs.add("http://pic1.ajkimg.com/m/d65ac8c7bca1799dc7d08d32d8ac755e/720x146x95.jpg");
        imgs.add("http://pic1.ajkimg.com/m/9b7f11559b10db0715554ce066c07be0/720x146x95.jpg");
        imgs.add("http://pic1.ajkimg.com/m/d65ac8c7bca1799dc7d08d32d8ac755e/720x146x95.jpg");
        imgs.add("http://pic1.ajkimg.com/m/9b7f11559b10db0715554ce066c07be0/720x146x95.jpg");

        headlines_view_vp.setImagePathList(imgs);

        headlines_view_vp.show();
        mRecentChatAdater = new RecentChatAdapter(mContext,mRecentChatList);
        mRecentChatLv.setAdapter(mRecentChatAdater);

        return  view;
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

    @Override
    public void getAImage() {
        button.setText("changed name");
    }
}
