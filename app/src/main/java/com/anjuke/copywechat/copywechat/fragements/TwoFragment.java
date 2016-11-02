package com.anjuke.copywechat.copywechat.fragements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjuke.copywechat.copywechat.ChatActivity;
import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.adapters.MyContactListForStickyListHeadersAdapter;
import com.anjuke.copywechat.copywechat.model.ContactMember;
import com.anjuke.copywechat.copywechat.util.ConstantCls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/5
 */
public class TwoFragment extends Fragment implements View.OnClickListener{

    private ExpandableListView expandableListView;
    private List<String> parent = null;
    private Map<String, List<String>> map = null;


    private MyContactListForStickyListHeadersAdapter myStickyListHeadersAdapter;
    private ArrayList<ContactMember> contactMemberList;
    private ExpandableStickyListHeadersListView expandableStickyList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.activity_main_mtab2, container, false);

        contactMemberList = new ArrayList<>();
        //同学
        ContactMember contactMembertx1 = new ContactMember();
        contactMembertx1.setContactCat("我的同学");
        contactMembertx1.setContactCatID(1);
        contactMembertx1.setContactName("同学A");
        contactMembertx1.setContactID("同学A");
        contactMemberList.add(contactMembertx1);
        ContactMember contactMembertx2 = new ContactMember();
        contactMembertx2.setContactCat("我的同学");
        contactMembertx2.setContactCatID(1);
        contactMembertx2.setContactName("同学B");
        contactMembertx2.setContactID("同学B");
        contactMemberList.add(contactMembertx2);
        ContactMember contactMembertx3 = new ContactMember();
        contactMembertx3.setContactCat("我的同学");
        contactMembertx3.setContactCatID(1);
        contactMembertx3.setContactName("同学C");
        contactMembertx3.setContactID("同学C");
        contactMemberList.add(contactMembertx3);
        //好友
        ContactMember contactMemberhy1 = new ContactMember();
        contactMemberhy1.setContactCat("我的好友");
        contactMemberhy1.setContactCatID(2);
        contactMemberhy1.setContactName("好友A");
        contactMemberhy1.setContactID("好友A");
        contactMemberList.add(contactMemberhy1);
        ContactMember contactMemberhy2 = new ContactMember();
        contactMemberhy2.setContactCat("我的好友");
        contactMemberhy2.setContactCatID(2);
        contactMemberhy2.setContactName("好友B");
        contactMemberhy2.setContactID("好友B");
        contactMemberList.add(contactMemberhy2);
        ContactMember contactMemberhy3 = new ContactMember();
        contactMemberhy3.setContactCat("我的好友");
        contactMemberhy3.setContactCatID(2);
        contactMemberhy3.setContactName("好友C");
        contactMemberhy3.setContactID("好友C");
        contactMemberList.add(contactMemberhy3);
        //陌生人
        ContactMember contactMemberms1 = new ContactMember();
        contactMemberms1.setContactCat("陌生人");
        contactMemberms1.setContactCatID(3);
        contactMemberms1.setContactName("陌生人A");
        contactMemberms1.setContactID("陌生人A");
        contactMemberList.add(contactMemberms1);
        ContactMember contactMemberms2 = new ContactMember();
        contactMemberms2.setContactCat("陌生人");
        contactMemberms2.setContactCatID(3);
        contactMemberms2.setContactName("陌生人B");
        contactMemberms2.setContactID("陌生人B");
        contactMemberList.add(contactMemberms2);

        expandableStickyList = (ExpandableStickyListHeadersListView) view.findViewById(R.id.expandablestickylist_list);
        myStickyListHeadersAdapter = new MyContactListForStickyListHeadersAdapter(getContext(), contactMemberList);
        expandableStickyList.setAdapter(myStickyListHeadersAdapter);
        expandableStickyList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(expandableStickyList.isHeaderCollapsed(headerId)){
                    expandableStickyList.expand(headerId);
                }else {
                    expandableStickyList.collapse(headerId);
                }
            }
        });

        expandableStickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(ConstantCls.USERNAME_FOR_CHATACTIVITY,contactMemberList.get(i).getContactName());
                startActivity(intent);
            }
        });
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
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }

    class MyAdapter extends BaseExpandableListAdapter{
        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public int getChildrenCount(int i) {
            String key = parent.get(i);
            int size=map.get(key).size();
            return size;
        }

        @Override
        public Object getGroup(int i) {
            return parent.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            String key = parent.get(i);
            return (map.get(key).get(i1));
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.contact_cat_level1_layout, null);
            }
            TextView tv = (TextView) view
                    .findViewById(R.id.contact_cat_level1_textview);
            tv.setText(TwoFragment.this.parent.get(i));
            return tv;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            String key = TwoFragment.this.parent.get(i);
            String info =  map.get(key).get(i1);
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.contact_cat_level2_layout, null);
            }
            TextView tv = (TextView) view
                    .findViewById(R.id.contact_cat_level2_textview);
            tv.setText(info);
            return tv;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++twofragement---- onDetach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(ConstantCls.FRAGMENT_LOG_DEBUG_TAG,"+++++++twofragement---- onDetach");
    }
}
