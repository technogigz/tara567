package com.myTara567.app.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.myTara567.app.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<slider_model> list;

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    ImageAdapter(Context context, ArrayList<slider_model> images) {
        this.mContext = context;
        this.list = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (ImageView) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.img_card, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        // setting the image in the imageView
        //imageView.setImageResource(images[position]);
        slider_model model = list.get(position);
        Glide.with(mContext).asBitmap()
                .load(model.getSlider_image())
                .centerCrop()
                .into(imageView);

        //9887752685

        // Adding the View
        container.addView(itemView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}