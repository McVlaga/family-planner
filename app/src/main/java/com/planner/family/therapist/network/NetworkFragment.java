package com.planner.family.therapist.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.data.Contact;
import com.planner.family.therapist.home.ChildrenViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetworkFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private static final int ADD_CONTACT_DIALOG_REQUEST_CODE = 0;
    private static final int EDIT_CONTACT_DIALOG_REQUEST_CODE = 1;

    private NetworkViewModel mViewModel;
    private ChildrenViewModel mSharedViewModel;

    private ExpandableListView networkListView;
    private NetworkListAdapter networkListAdapter;

    private List<String> listDataHeader;
    private HashMap<String, List<Contact>> listDataChild;

    public NetworkFragment() {

    }

    public static NetworkFragment newInstance() {
        return new NetworkFragment();
    }

    public void setViewModel(@NonNull NetworkViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void setSharedViewModel(@NonNull ChildrenViewModel viewModel) {
        mSharedViewModel = viewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContacts();
    }

    private void loadContacts() {
        mSharedViewModel.getSelectedChild().observe(this, child -> mViewModel.setChild(child));
        mViewModel.getContacts().observe(this, this::updateContacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.network_fragment, container, false);

        networkListView = rootView.findViewById(R.id.network_list_view);

        prepareListData();

        networkListAdapter = new NetworkListAdapter(getActivity(), listDataHeader, listDataChild);
        networkListView.setAdapter(networkListAdapter);
        networkListView.setOnChildClickListener(this);

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Immediate");
        listDataHeader.add("Social");
        listDataHeader.add("Medical");
        listDataHeader.add("Other");

        List<Contact> immediate = new ArrayList<>();
        List<Contact> social = new ArrayList<>();
        List<Contact> medical = new ArrayList<>();
        List<Contact> other = new ArrayList<>();

        listDataChild.put(listDataHeader.get(0), immediate);
        listDataChild.put(listDataHeader.get(1), social);
        listDataChild.put(listDataHeader.get(2), medical);
        listDataChild.put(listDataHeader.get(3), other);
    }

    private void updateContacts(List<Contact> contactList) {
        listDataChild.clear();
        List<Contact> immediate = new ArrayList<>();
        List<Contact> social = new ArrayList<>();
        List<Contact> medical = new ArrayList<>();
        List<Contact> other = new ArrayList<>();

        for (Contact contact: contactList) {
            if (contact.getType() == 0) {
                immediate.add(contact);
            } else if (contact.getType() == 1) {
                social.add(contact);
            } else if(contact.getType() == 2) {
                medical.add(contact);
            } else if (contact.getType() == 3) {
                other.add(contact);
            }
        }

        listDataChild.put(listDataHeader.get(0), immediate);
        listDataChild.put(listDataHeader.get(1), social);
        listDataChild.put(listDataHeader.get(2), medical);
        listDataChild.put(listDataHeader.get(3), other);
        networkListAdapter.notifyDataSetChanged();
    }

    private void showAddContactDialog(int groupPosition) {
        Child selectedChild = mSharedViewModel.getSelectedChild().getValue();
        if (selectedChild == null) {
            return;
        }
        FragmentManager fm = getFragmentManager();
        AddEditContactDialogFragment addEditContactDialogFragment =
                AddEditContactDialogFragment.newInstance(
                        "Add a contact",
                        -1,
                        "",
                        "",
                        groupPosition);
        addEditContactDialogFragment.setTargetFragment(this, ADD_CONTACT_DIALOG_REQUEST_CODE);
        addEditContactDialogFragment.show(fm, null);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        FragmentManager fm = getFragmentManager();

        Contact contact = listDataChild.get(listDataHeader.get(i)).get(i1);
        AddEditContactDialogFragment addEditContactDialogFragment =
                AddEditContactDialogFragment.newInstance("Edit a contact",
                        contact.getId(),
                        contact.getName(),
                        contact.getNumber(),
                        i);

        addEditContactDialogFragment.setTargetFragment(this, EDIT_CONTACT_DIALOG_REQUEST_CODE);
        addEditContactDialogFragment.show(fm, null);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CONTACT_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(AddEditContactDialogFragment.NAME_KEY);
                String number = data.getStringExtra(AddEditContactDialogFragment.NUMBER_KEY);
                int type = data.getIntExtra(AddEditContactDialogFragment.TYPE_KEY, 0);
                mViewModel.saveContact(name, number, type);
            }
        }
        if (requestCode == EDIT_CONTACT_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(AddEditContactDialogFragment.NAME_KEY);
                String number = data.getStringExtra(AddEditContactDialogFragment.NUMBER_KEY);
                int type = data.getIntExtra(AddEditContactDialogFragment.TYPE_KEY, 0);
                int contactId = data.getIntExtra(AddEditContactDialogFragment.ID_KEY, -1);
                if (name.equals("")) {
                    mViewModel.deleteContact(contactId);
                } else {
                    mViewModel.updateContact(contactId, name, number, type);
                }
            }
        }
    }

    private void callContact(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + number)));
        }
    }

    class NetworkListAdapter extends BaseExpandableListAdapter {

        private class ViewHolder {
            TextView name;
            TextView number;
            ImageButton callButton;
        }

        private Context _context;

        private List<String> _listDataHeader;
        private HashMap<String, List<Contact>> _listDataChild;

        public NetworkListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<Contact>> listChildData) {
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

            final Contact contact = (Contact) getChild(groupPosition, childPosition);

            final ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.network_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.network_name);
                viewHolder.number = convertView.findViewById(R.id.network_number);
                viewHolder.callButton = convertView.findViewById(R.id.call_contact);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(contact.getName());
            viewHolder.number.setText(contact.getNumber());

            final String number = contact.getNumber();

            viewHolder.callButton.setFocusable(false);
            viewHolder.callButton.setOnClickListener(view -> {
                callContact(number);
            });

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return _listDataChild.get(_listDataHeader.get(groupPosition)).size();
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
                convertView = infalInflater.inflate(R.layout.network_group_item, null);
            }

            TextView lblListHeader = convertView.findViewById(R.id.network_group_header);
            lblListHeader.setText(headerTitle);

            ImageButton addContactButton = convertView.findViewById(R.id.add_contact_button);
            addContactButton.setFocusable(false);
            addContactButton.setOnClickListener(view -> {
                showAddContactDialog(groupPosition);
            });

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
