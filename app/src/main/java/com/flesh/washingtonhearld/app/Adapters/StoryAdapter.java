package com.flesh.washingtonhearld.app.Adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flesh.washingtonhearld.app.MyDateUtils;
import com.flesh.washingtonhearld.app.Objects.DtoStory;
import com.flesh.washingtonhearld.app.R;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aaronfleshner on 5/17/14.
 */
public class StoryAdapter extends ArrayAdapter<DtoStory> {

    private ArrayList<DtoStory> data;
    private LayoutInflater vi;
    private DtoStory story;
    private Context mContext;

    public StoryAdapter(Context context, ArrayList<DtoStory> data) {
        super(context, R.layout.grid_story_cell, data);
        this.data = data;
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        story = data.get(position);
        if (row == null) {
            row = vi.inflate(R.layout.grid_story_cell, null);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        try {
            holder.tvTitle.setText(story.getTitle());
            holder.tvAuthor.setText(MyDateUtils.TimeFromTodayAccuracyToTheMinute(story.getDatePublished()));
            Picasso.with(mContext).load(story.getImageUrl()).resize(275,275).centerInside().into(holder.img);
        }catch (NullPointerException e){

        }

        return row;
    }



    // somewhere else in your class definition
    static class ViewHolder {
        @InjectView(R.id.imgStoryCell)
        ImageView img;
        @InjectView(R.id.tvTitle)
        TextView tvTitle;
        @InjectView(R.id.tvAuthor)
        TextView tvAuthor;

        ViewHolder(View row) {
            ButterKnife.inject(this, row);
        }
    }
}
