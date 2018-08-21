package com.planner.family.therapist.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.utils.BitmapUtils;
import com.planner.family.therapist.view.CircledImageView;

import static com.planner.family.therapist.utils.BitmapUtils.uriToBitmap;

public class ChildrenDrawerFragment extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {

    public interface OnDrawerItemClicked {
        void onDrawerItemClick(View buttonView);
    }

    private ChildrenViewModel mViewModel;

    private ListView mChildrenListView;
    private LinearLayout mChildrenView;
    private ChildrenDrawerAdapter mChildrenDrawerAdapter;

    private LinearLayout mMenuItemsView;

    private NavigationView mNavigationView;
    private View mNavigationHeader;
    private Button mAddChildButton;
    private ImageButton mDeleteChildButton;
    private ImageButton mShowChildrenButton;

    private List<Button> mNavigationButtons = new ArrayList<>();

    private CircledImageView mChildImageView;
    private TextView mChildNameTextView;

    private OnDrawerItemClicked mOnDrawerItemClickedListener;

    private int selectedChildIndex;

    private static final int ADD_CHILD_DIALOG_REQUEST_CODE = 1;

    public ChildrenDrawerFragment() {
    }

    public static ChildrenDrawerFragment newInstance() {
        return new ChildrenDrawerFragment();
    }

    public void setViewModel(@NonNull ChildrenViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.children_drawer_fragment, container, false);
        mNavigationView = root.findViewById(R.id.home_navigation_view);

        mChildrenListView = root.findViewById(R.id.children_list_view);
        mChildrenView = root.findViewById(R.id.children_view);

        LinearLayout listViewFooter = (LinearLayout)inflater.inflate(
                R.layout.children_list_view_footer, null);

        mAddChildButton = listViewFooter.findViewById(R.id.add_child_button);

        mAddChildButton.setOnClickListener(this);
        mChildrenListView.addFooterView(listViewFooter, null, false);

        mChildrenDrawerAdapter =
                new ChildrenDrawerAdapter(getActivity(), R.layout.children_drawer_item, new ArrayList<Child>());
        mChildrenListView.setAdapter(mChildrenDrawerAdapter);
        mChildrenListView.setOnItemClickListener(this);

        mMenuItemsView = root.findViewById(R.id.menu_items_view);
        Button reflectionsButton = root.findViewById(R.id.reflections_button);
        Button childInfoButton = root.findViewById(R.id.child_info_button);
        Button resourcesButton = root.findViewById(R.id.resources_button);
        Button networkButton = root.findViewById(R.id.network_button);
        Button nearbyButton = root.findViewById(R.id.nearby_button);

        mNavigationButtons.add(reflectionsButton);
        mNavigationButtons.add(childInfoButton);
        mNavigationButtons.add(resourcesButton);
        mNavigationButtons.add(networkButton);
        mNavigationButtons.add(nearbyButton);

        for (Button button: mNavigationButtons) {
            button.setOnClickListener(this);
        }
        reflectionsButton.performClick();

        mNavigationHeader = root.findViewById(R.id.drawer_header);
        mNavigationHeader.setOnClickListener(this);
        mDeleteChildButton = mNavigationHeader.findViewById(R.id.delete_child_button);
        mDeleteChildButton.setOnClickListener(this);

        mShowChildrenButton = root.findViewById(R.id.show_children_button);

        mChildImageView = mNavigationHeader.findViewById(R.id.child_image_view);
        mChildNameTextView = mNavigationHeader.findViewById(R.id.child_name_text_view);

        selectedChildIndex = 0;
        loadChildren();

        return root;
    }

    private void loadChildren(){
        mViewModel.getSelectedChild().observe(this, child -> {
            if (child != null) {
                mChildNameTextView.setText(child.getFirstName() + " " + child.getLastName());
                Bitmap bitmap = uriToBitmap(getActivity(), Uri.parse(child.getImagePath()));
                mChildImageView.setImageBitmap(bitmap);
            }
        });
        mViewModel.getChildren().observe(this, childrenList -> {
            if (childrenList.size() > 0) {
                mViewModel.selectChildAt(selectedChildIndex);
            } else {
                showPlaceholderInHeader();
                mViewModel.setSelectedChildEmpty();
            }
            mChildrenDrawerAdapter.setData(childrenList);
        });
    }

    private void showPlaceholderInHeader() {
        mChildImageView.setImageBitmap(
                BitmapUtils.getSquaredBitmapWithColor("#C5CAE9"));
        mChildNameTextView.setText(getString(R.string.child_name_placeholder));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_header:
                final int[] stateSet;
                if (mChildrenListView.getVisibility() == View.GONE) {
                    mMenuItemsView.setVisibility(View.GONE);
                    mChildrenListView.setVisibility(View.VISIBLE);
                    mDeleteChildButton.setVisibility(View.VISIBLE);
                    stateSet = new int[]{android.R.attr.state_checked};
                } else {
                    mDeleteChildButton.setVisibility(View.GONE);
                    mChildrenListView.setVisibility(View.GONE);
                    mMenuItemsView.setVisibility(View.VISIBLE);
                    stateSet = new int[]{android.R.attr.state_checked * -1};
                }
                mShowChildrenButton.setImageState(stateSet, true);
                break;
            case R.id.add_child_button:
                showAddChildDialog();
                break;
            case R.id.delete_child_button:
                if (mViewModel.getSelectedChild().getValue() != null) {
                    showDeleteChildDialog();
                }
                break;
            default:
                mOnDrawerItemClickedListener.onDrawerItemClick(view);
                for (Button button: mNavigationButtons) {
                    if (button.isSelected()) {
                        button.setSelected(false);
                    }
                }
                view.setSelected(true);
        }
    }

    private void showDeleteChildDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.child_delete_dialog_title));
        builder.setMessage(getActivity().getString(R.string.child_delete_dialog_message));
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Delete",
                (dialog, id) -> {
                    Child childToDelete = mViewModel.getSelectedChild().getValue();
                    if (childToDelete != null) {
                        if (selectedChildIndex > 0) selectedChildIndex--;
                        mViewModel.deleteChild(childToDelete);
                    }
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedChildIndex = i;
        mViewModel.selectChildAt(i);
    }

    private void showAddChildDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddChildDialogFragment addChildDialogFragment = AddChildDialogFragment.newInstance();
        addChildDialogFragment.setTargetFragment(this, ADD_CHILD_DIALOG_REQUEST_CODE);
        addChildDialogFragment.show(fm, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CHILD_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String firstName = data.getStringExtra(AddChildDialogFragment.FIRST_NAME_KEY);
                String lastName = data.getStringExtra(AddChildDialogFragment.LAST_NAME_KEY);
                String disability = data.getStringExtra(AddChildDialogFragment.DISABILITY_KEY);
                String bio = data.getStringExtra(AddChildDialogFragment.BIO_KEY);
                String imagePath = data.getStringExtra(AddChildDialogFragment.IMAGE_PATH_KEY);
                long birthdayMillis = data.getLongExtra(AddChildDialogFragment.BIRTHDAY_MILLIS_KEY, 0);

                mViewModel.saveChild(new Child(
                        firstName, lastName, disability, bio, birthdayMillis, imagePath));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity){
            activity = (Activity) context;
            try {
                mOnDrawerItemClickedListener = (OnDrawerItemClicked) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString());
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnDrawerItemClickedListener = null;
    }
}
