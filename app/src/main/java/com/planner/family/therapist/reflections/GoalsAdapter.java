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
import com.planner.family.therapist.data.Goal;

public class GoalsAdapter extends ArrayAdapter<Goal> {

    private static class ViewHolder {
        TextView goal;
    }

    private Context mContext;

    private List<Goal> mGoalsList = new ArrayList<>();

    public GoalsAdapter(@NonNull Context context, int resource, List<Goal> goalsList) {
        super(context, resource, goalsList);
        mContext = context;
        mGoalsList = goalsList;
    }

    public void setData(List<Goal> goalsList) {
        mGoalsList.clear();
        mGoalsList.addAll(goalsList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mGoalsList != null ? mGoalsList.size() : 0;
    }

    public Goal getItem(int position) {
        return mGoalsList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Goal goal = mGoalsList.get(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reflections_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.goal = convertView.findViewById(R.id.reflecion_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.goal.setText(goal.getGoal());
        return convertView;
    }
}