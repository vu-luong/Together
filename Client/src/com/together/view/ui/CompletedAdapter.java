package com.together.view.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.provider.MediaStore.Video;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.together.R;
import com.together.pojo.ItemCompleted;

public class CompletedAdapter
       extends BaseAdapter {
	
    private final Context mContext;
    
    private List<ItemCompleted> itemList =
        new ArrayList<>();

    public CompletedAdapter(Context context) {
        super();
        mContext = context;
    }

    
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        ItemCompleted item = itemList.get(position);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =
                mInflater.inflate(R.layout.item_completed,null);
        }

        return convertView;
    }

  
    public void add(ItemCompleted item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

   
    public void remove(Video video) {
        itemList.remove(video);
        notifyDataSetChanged();
    }

   
    public List<ItemCompleted> getVideos() {
        return itemList;
    }

 
    public void setVideos(List<ItemCompleted> list) {
        this.itemList = list;
        notifyDataSetChanged();
    }

   
    public int getCount() {
        return itemList.size();
    }

   
    public ItemCompleted getItem(int position) {
        return itemList.get(position);
    }

  
    public long getItemId(int position) {
        return position;
    }
}
