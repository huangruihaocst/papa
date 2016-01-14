package com.Fragments.papa.experiment_result;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by huang on 15-11-20.
 */
public class ImageGridAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Media> mediaArrayList;

    @Override
    public int getCount() {
        return mediaArrayList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(4, 4, 4, 4);
        }else{
            imageView = (ImageView)convertView;
        }
        imageView.setImageBitmap(mediaArrayList.get(position).bitmap);
        return imageView;
    }

    public ImageGridAdapter(Context context, ArrayList<Media> mediaArrayList){
        this.context = context;
        this.mediaArrayList = mediaArrayList;
    }
}