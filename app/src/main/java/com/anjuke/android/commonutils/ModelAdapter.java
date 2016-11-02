/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * ModelAdapter.java
 *
 */
package com.anjuke.android.commonutils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 方法一：内部实现
 *        adapter = new ModelAdapter<User>(getActivity(), R.layout.listitem_uesr) {
 *           @Override
 *           protected IViewHolder<User> createViewHolder() {
 *               return new ViewHolder<User>() {
 *                   @Override
 *                   public void update(User user, View convertView) {
 *                       ((TextView)convertView.findViewById(R.id.name)).setText(user.getName);
 *                       ((TextView)convertView.findViewById(R.id.addr)).setText(user.getAddr);
 *                   }
 *               };
 *           }
 *       };
 * 
 * 方法二：外部实现
 *         public static class UserHolder extends ViewHolder<User> {
 *            @Override
 *            public void update(User item, View convertView) {
 *                 ((TextView)convertView.findViewById(R.id.name)).setText(user.getName);
 *            }
 *
 *        }
 */

/**
 * T 必须是个class ，使用本方法要实现ViewHolder 进行控件的绑定和逻辑的处理
 * 
 * @author willchun (chunwang@anjuke.com)
 * @date 2013-5-17
 */
public class ModelAdapter<T> extends BaseAdapter {

    private List<T> objects;

    private final int resId;
    protected Activity mActivity;
    private final Class<? extends IViewHolder<T>> itemClass;

    public ModelAdapter(Activity activity, int resId, List<T> objects,
                        Class<? extends IViewHolder<T>> itemClass) {
        this.resId = resId;
        this.mActivity = activity;
        this.itemClass = itemClass;
        if (objects == null) {
            this.objects = new ArrayList<T>();
        } else {
            this.objects = objects;
        }
    }
    
    public ModelAdapter(Activity activity, int resId, T[] objects,
                        Class<? extends IViewHolder<T>> itemClass) {
        this(activity, resId, Arrays.asList(objects), itemClass);
    }

    public ModelAdapter(Activity activity, int resId, Class<? extends IViewHolder<T>> itemClass) {
        this(activity, resId, new ArrayList<T>(), itemClass);
    }

    public ModelAdapter(Activity activity, int resId) {
        this(activity, resId, new ArrayList<T>(), null);
    }

    protected View createItemView() {
        return LayoutInflater.from(mActivity).inflate(resId, null);
    }

    protected IViewHolder<T> createViewHolder() {
        try {
            return itemClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(itemClass.getName() + " 初始化失败！请确认类"
                    + itemClass.getSimpleName() + "含有一个无参的构造函数");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(itemClass.getName() + " 初始化失败！请确认类"
                    + itemClass.getSimpleName()
                    + "是一个public static class");
        } catch (NullPointerException e) {
            throw new RuntimeException(" 初始化失败！itemClass 不能为空");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        IViewHolder<T> holder = null;
        if (convertView == null) {
            convertView = createItemView();
            holder = createViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (IViewHolder<T>) convertView.getTag();
        }
        holder.update(getItem(position), convertView, this, position, parent);
        return convertView;
    }

    public static interface IViewHolder<T> {
        void update(T item, View convertView, ModelAdapter<T> adapter, int position,
                    ViewGroup parent);
    }

    public abstract static class ViewHolder<T> implements IViewHolder<T> {

        @Override
        public void update(T item, View convertView, ModelAdapter<T> adapter, int position,
                           ViewGroup parent) {
            // TODO Auto-generated method stub
            update(item, convertView);
        }
        public abstract void update(T item, View convertView);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects == null ? 0 : objects.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return objects == null ? null : objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void addItem(T object) {
        if (object != null) {
            objects.add(object);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> items) {
        if (items != null) {
            objects.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItems(T... items) {
        addItems(Arrays.asList(items));
    }

    public void setItems(List<T> items) {
        objects.clear();
        addItems(items);
    }

    public void setItems(T... items) {
        objects.clear();
        addItems(items);
    }

    public List<T> getItems() {
        return objects;
    }

    public void deleteItem(int position) {
        objects.remove(position);
        notifyDataSetChanged();
    }

    public void deleteItems(List<T> items) {
        objects.removeAll(items);
        notifyDataSetChanged();
    }


}
