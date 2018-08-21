package com.planner.family.therapist.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.planner.family.therapist.view.CircledImageView;
import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Child;

import static com.planner.family.therapist.utils.BitmapUtils.uriToBitmap;

public class ChildrenDrawerAdapter extends ArrayAdapter<Child> {

    private static class ViewHolder {
        CircledImageView childImage;
        TextView childName;
    }

    private Context mContext;

    private List<Child> mChildArrayList = new ArrayList<>();

    public ChildrenDrawerAdapter(@NonNull Context context, int resource, List<Child> childList) {
        super(context, resource, childList);
        mContext = context;
        mChildArrayList = childList;
    }

    public void setData(List<Child> childData) {
        mChildArrayList.clear();
        mChildArrayList.addAll(childData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mChildArrayList != null ? mChildArrayList.size() : 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Child child = mChildArrayList.get(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.children_drawer_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.childImage = convertView.findViewById(R.id.child_image);
            viewHolder.childName = convertView.findViewById(R.id.child_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.childName.setText(child.getLastName() + " " + child.getFirstName());

        Bitmap bitmap = uriToBitmap(mContext, Uri.parse(child.getImagePath()));
        viewHolder.childImage.setImageBitmap(bitmap);


        return convertView;
    }
}
