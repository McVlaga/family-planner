package com.planner.family.therapist.resources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.planner.family.therapist.R;
import com.planner.family.therapist.home.ChildrenViewModel;

public class ResourcesFragment extends Fragment {

    private ChildrenViewModel mViewModel;

    private ExpandableListView resourcesListView;

    private List<String> listDataHeader;
    private HashMap<String, List<Resource>> listDataChild;

    public ResourcesFragment() {

    }

    public static ResourcesFragment newInstance() {
        return new ResourcesFragment();
    }

    public void setViewModel(@NonNull ChildrenViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.resources_fragment, container, false);

        resourcesListView = rootView.findViewById(R.id.resources_list_view);

        prepareListData();

        ResourcesListAdapter resourcesAdapter = new ResourcesListAdapter(getActivity(), listDataHeader, listDataChild);

        resourcesListView.setAdapter(resourcesAdapter);

        resourcesListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            TextView tv = v.findViewById(R.id.resource_link);
            String link = tv.getText().toString();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);

            return true;
        });

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Disability Research");
        listDataHeader.add("Government Research Centers");
        listDataHeader.add("Non-Profit Agencies");
        listDataHeader.add("Social Media");

        List<Resource> disResearch = new ArrayList<>();
        disResearch.add(new Resource("Disability Research and Dissemination Center", "https://www.disabilityresearchcenter.com/"));
        disResearch.add(new Resource("Florida Center for Reading Research", "http://www.fcrr.org"));
        disResearch.add(new Resource("Office of Special Education Programs", "https://www2.ed.gov/about/offices/list/osers/osep/index.html"));
        disResearch.add(new Resource("National Association for Down Syndrome", "http://www.nads.org/"));

        List<Resource> govtAgencies = new ArrayList<>();
        govtAgencies.add(new Resource("Federation for Children with Special Needs", "https://fcsn.org/"));
        govtAgencies.add(new Resource("The National Association for Bilingual Education", "http://www.nabe.org/"));
        govtAgencies.add(new Resource("The United States International Council on Disabilities", "http://www.usicd.org/"));
        govtAgencies.add(new Resource("The United States International Council on Disabilities", "http://www.usicd.org/"));

        List<Resource> nonProfit = new ArrayList<>();
        nonProfit.add(new Resource("ParaQuad", "https://www.paraquad.org/"));
        nonProfit.add(new Resource("Lions Club International", "http://www.lionsclubs.org/EN/index.php"));
        nonProfit.add(new Resource("Missouri Parents Act", "http://www.missouriparentsact.org/"));
        nonProfit.add(new Resource("Parents Helping Parents", "http://www.phponline.org/"));

        List<Resource> socialMedia = new ArrayList<>();
        socialMedia.add(new Resource("Angelman Syndrome Foundation", "http://angelman.org/"));
        socialMedia.add(new Resource("Social Thinking", "https://www.youtube.com/user/Socialthinking"));
        socialMedia.add(new Resource("Special Books by Special Kids", "https://www.facebook.com/specialbooksbyspecialkids/"));
        socialMedia.add(new Resource("Simply Special Ed", "https://www.facebook.com/SimplySpecialEd/"));

        listDataChild.put(listDataHeader.get(0), disResearch); // Header, Child data
        listDataChild.put(listDataHeader.get(1), govtAgencies);
        listDataChild.put(listDataHeader.get(2), nonProfit);
        listDataChild.put(listDataHeader.get(3), socialMedia);
    }

    class ResourcesListAdapter extends BaseExpandableListAdapter {

        private Context _context;

        private List<String> _listDataHeader;
        private HashMap<String, List<Resource>> _listDataChild;

        public ResourcesListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<Resource>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final Resource resource = (Resource) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.resources_item, null);
            }

            TextView title = convertView.findViewById(R.id.resource_title);

            TextView link = convertView.findViewById(R.id.resource_link);

            title.setText(resource.getTitle());
            link.setText(resource.getLink());
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.resources_group_item, null);
            }

            TextView lblListHeader = convertView.findViewById(R.id.resource_group_header);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
