package com.example.mycodebook.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mycodebook.R;
import com.example.mycodebook.pojo.DataItem;

import java.util.LinkedList;


public class MainPageListViewAdapter<T> extends BaseAdapter {

    private Context mContext;
    private LinkedList<T> mData;
    public MainPageListViewAdapter( Context mContext,LinkedList<T> mData) {
        if(mData==null){
            this.mData=new LinkedList<>();
        }else{
            this.mData = mData;

        }

        this.mContext = mContext;
    }

    public void updateData(LinkedList<T> mData){
        if(mData==null){
            this.mData=new LinkedList<>();
        }else{
            this.mData = mData;

        }
        notifyDataSetChanged();
    }

    public void notifyAdd(T item){
        if(mData ==null){
            mData=new LinkedList<>();
        }
        mData.add( item);

        notifyDataSetChanged();
    }

    public void notifyUpdate(T oldItem,T item){
        mData.remove(oldItem);
        mData.add(item);

        notifyDataSetChanged();
    }

    public void notifyDelete(T item){
        mData.remove(item);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mainpage_listview_item,parent,false);
            holder = new ViewHolder();
            holder.txt_num=convertView.findViewById(R.id.item_num);
            holder.txt_info=convertView.findViewById(R.id.item_info);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_num.setText(((DataItem)mData.get(position)).getNum());
        holder.txt_info.setText(((DataItem)mData.get(position)).getInfo());
        return convertView;
    }

    private class ViewHolder{
        TextView txt_num;
        TextView txt_info;

    }



}



