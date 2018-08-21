package com.planner.family.therapist.reflections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Stressor;

public class StressorsAdapter extends ArrayAdapter<Stressor> {

    private static class ViewHolder {
        TextView stressor;
    }

    private Context mContext;

    private List<Stressor> mStressorsList = new ArrayList<>();

    public StressorsAdapter(@NonNull Context context, int resource, List<Stressor> stressorsList) {
        super(context, resource, stressorsList);
        mContext = context;
        mStressorsList = stressorsList;
    }

    public void setData(List<Stressor> stressorsData) {
        mStressorsList.clear();
        mStressorsList.addAll(stressorsData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mStressorsList != null ? mStressorsList.size() : 0;
    }

    public Stressor getItem(int position) {
        return mStressorsList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Stressor stressor = mStressorsList.get(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reflections_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.stressor = convertView.findViewById(R.id.reflecion_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.stressor.setText(stressor.getStressor());
        return convertView;
    }
}