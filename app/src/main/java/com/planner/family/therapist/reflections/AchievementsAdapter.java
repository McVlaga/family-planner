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
import com.planner.family.therapist.data.Achievement;

public class AchievementsAdapter extends ArrayAdapter<Achievement> {

    private static class ViewHolder {
        TextView achievement;
    }

    private Context mContext;

    private List<Achievement> mAchievementsList = new ArrayList<>();

    public AchievementsAdapter(@NonNull Context context, int resource, List<Achievement> achievementsList) {
        super(context, resource, achievementsList);
        mContext = context;
        mAchievementsList = achievementsList;
    }

    public void setData(List<Achievement> achievementsList) {
        mAchievementsList.clear();
        mAchievementsList.addAll(achievementsList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mAchievementsList != null ? mAchievementsList.size() : 0;
    }

    public Achievement getItem(int position) {
        return mAchievementsList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Achievement achievement = mAchievementsList.get(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reflections_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.achievement = convertView.findViewById(R.id.reflecion_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.achievement.setText(achievement.getAchievement());
        return convertView;
    }
}