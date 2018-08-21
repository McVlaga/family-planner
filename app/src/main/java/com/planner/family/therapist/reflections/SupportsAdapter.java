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
import com.planner.family.therapist.data.Support;

public class SupportsAdapter extends ArrayAdapter<Support> {

    private static class ViewHolder {
        TextView support;
    }

    private Context mContext;

    private List<Support> mSupportsList = new ArrayList<>();

    public SupportsAdapter(@NonNull Context context, int resource, List<Support> supportsList) {
        super(context, resource, supportsList);
        mContext = context;
        mSupportsList = supportsList;
    }

    public void setData(List<Support> supportsData) {
        mSupportsList.clear();
        mSupportsList.addAll(supportsData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mSupportsList != null ? mSupportsList.size() : 0;
    }

    public Support getItem(int position) {
        return mSupportsList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Support support = mSupportsList.get(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reflections_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.support = convertView.findViewById(R.id.reflecion_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.support.setText(support.getSupport());
        return convertView;
    }
}