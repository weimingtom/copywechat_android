package com.anjuke.copywechat.copywechat.fragements;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.util.ConstantCls;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/5
 */
public class ThreeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_main_mtab3, null);

        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onCreateView");
        return  view;
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onPause");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onResume");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onDestroy");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onDestroyView");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onStop");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++threefragement---- onDetach");
    }
}
