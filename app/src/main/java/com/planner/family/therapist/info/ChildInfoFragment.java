package com.planner.family.therapist.info;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.planner.family.therapist.BuildConfig;
import com.planner.family.therapist.view.CircledImageView;
import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.home.ChildrenViewModel;
import com.planner.family.therapist.utils.BitmapUtils;

import static android.app.Activity.RESULT_OK;

public class ChildInfoFragment extends Fragment implements View.OnClickListener {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    private static final int PICK_PHOTO_REQUEST_CODE = 0;

    private ChildrenViewModel mViewModel;

    private CircledImageView childImageView;
    private TextView childName;
    private TextView childBirthday;
    private TextView childBio;
    private TextView childDisability;

    private ImageButton childInfoModeButton;
    private EditText childNameEditText;
    private Button childBirthdayButton;
    private EditText childDisabilityEditText;
    private EditText childBioEditText;
    private LinearLayout editModeButtonsParent;
    private Button cancelChangesButton;
    private Button saveChildButton;
    private Button changeImageButton;

    private String firstName;
    private String lastName;
    private String disability;
    private String bio;
    private Uri imageUri;
    private Uri newImageUri;

    private long birthdayMillis;

    private boolean editMode = false;

    public ChildInfoFragment() {

    }

    public static ChildInfoFragment newInstance() {
        return new ChildInfoFragment();
    }

    public void setViewModel(@NonNull ChildrenViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_info_fragment, container, false);

        childImageView = rootView.findViewById(R.id.child_info_image_view);
        childImageView.setImageBitmap(BitmapUtils.getSquaredBitmapWithColor("#9FA8DA"));
        childName = rootView.findViewById(R.id.child_info_name);
        childBio = rootView.findViewById(R.id.bio_text_view);
        childDisability = rootView.findViewById(R.id.disability_text_view);
        childBirthday = rootView.findViewById(R.id.birthday_text_view);

        childInfoModeButton = rootView.findViewById(R.id.child_info_mode_image_button);
        childInfoModeButton.setOnClickListener(this);

        childNameEditText = rootView.findViewById(R.id.child_info_name_edit_text);
        childDisabilityEditText = rootView.findViewById(R.id.child_info_disability_edit_text);
        childBioEditText = rootView.findViewById(R.id.child_info_bio_edit_text);
        childBirthdayButton = rootView.findViewById(R.id.child_info_birthday_button);
        childBirthdayButton.setOnClickListener(this);

        cancelChangesButton = rootView.findViewById(R.id.child_info_cancel_button);
        cancelChangesButton.setOnClickListener(this);
        saveChildButton = rootView.findViewById(R.id.child_info_save_button);
        saveChildButton.setOnClickListener(this);

        changeImageButton = rootView.findViewById(R.id.change_child_image_button);
        changeImageButton.setOnClickListener(this);

        editModeButtonsParent = rootView.findViewById(R.id.child_info_edit_mode_buttons);

        loadChild();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadChild() {
        mViewModel.getSelectedChild().observe(this, this::updateChildInfo);
    }

    private void updateChildInfo(Child child) {
        if (editMode) {
            editMode = false;
            changeLayoutForStaticMode();
        }
        if (child != null) {
            childImageView.setImageBitmap(BitmapUtils.uriToBitmap(getActivity(), Uri.parse(child.getImagePath())));
            firstName = child.getFirstName();
            lastName = child.getLastName();
            birthdayMillis = child.getBirthdayMillis();
            imageUri = Uri.parse(child.getImagePath());
            newImageUri = null;
            childName.setText(firstName + " " + lastName);
            if (child.getBirthdayMillis() > 0) {
                SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, yyyy");
                childBirthday.setText(formatter.format(new Date(child.getBirthdayMillis())));
            }
            childDisability.setText(child.getDisability());
            childBio.setText(child.getBio());
            childInfoModeButton.setVisibility(View.VISIBLE);
        } else {
            childImageView.setImageBitmap(BitmapUtils.getSquaredBitmapWithColor("#9FA8DA"));
            childBirthday.setText("");
            childBio.setText("");
            childDisability.setText("");
            childName.setText("Add a child");
            childInfoModeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.child_info_mode_image_button:
                if (editMode) {
                    editMode = false;
                    changeLayoutForStaticMode();
                } else {
                    editMode = true;
                    changeLayoutForEditMode();
                }
                break;

            case R.id.child_info_birthday_button:
                showDatePickerDialog();
                break;
            case R.id.child_info_save_button:
                saveChild();
                changeLayoutForStaticMode();
                editMode = false;
                break;
            case R.id.child_info_cancel_button:
                changeLayoutForStaticMode();
                editMode = false;
                break;
            case R.id.change_child_image_button:
                showImagePickerDialog();
                break;
        }
    }

    private void saveChild() {
        if (!isInputGood()) {
            return;
        }
        String newName = childNameEditText.getText().toString();
        String[] names = newName.split(" ");
        childName.setText(newName);
        disability = childDisabilityEditText.getText().toString();
        childDisability.setText(disability);
        bio = childBioEditText.getText().toString();
        childBio.setText(bio);
        childBirthday.setText(childBirthdayButton.getText());

        if (newImageUri != null) {
            imageUri = newImageUri;
        }

        Child currentChild = mViewModel.getSelectedChild().getValue();

        Child newChild = new Child(
                currentChild.getId(),
                names[0],
                names[1],
                disability,
                bio,
                birthdayMillis,
                imageUri.toString());
        mViewModel.updateChild(newChild);
    }

    private boolean isInputGood() {
        String newName = childNameEditText.getText().toString();
        String[] names = newName.split(" ");
        if (names.length != 2) {
            Toast.makeText(getActivity(), "First and last names please", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void showImagePickerDialog() {
        Intent pickIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        pickIntent.setType("image/*");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(
                Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + "_image.jpg");
        newImageUri = FileProvider.getUriForFile(getActivity(), AUTHORITY, photo);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, newImageUri);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(chooserIntent, PICK_PHOTO_REQUEST_CODE);
    }

    private void showDatePickerDialog() {
        Calendar birthdayCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    birthdayCalendar.set(year, monthOfYear, dayOfMonth);
                    birthdayMillis = birthdayCalendar.getTimeInMillis();
                    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, yyyy");
                    childBirthdayButton.setText(formatter.format(birthdayCalendar.getTime()));
                },
                birthdayCalendar.get(Calendar.YEAR),
                birthdayCalendar.get(Calendar.MONTH),
                birthdayCalendar.get(Calendar.DATE));
        datePicker.show();
    }

    private void changeLayoutForEditMode() {
        childDisability.setVisibility(View.GONE);
        childDisabilityEditText.setText(childDisability.getText());
        childDisabilityEditText.setVisibility(View.VISIBLE);

        childBio.setVisibility(View.GONE);
        childBioEditText.setText(childBio.getText());
        childBioEditText.setVisibility(View.VISIBLE);

        childBirthday.setVisibility(View.GONE);
        childBirthdayButton.setText(childBirthday.getText());
        childBirthdayButton.setVisibility(View.VISIBLE);

        childName.setVisibility(View.GONE);
        childNameEditText.setText(childName.getText());
        childNameEditText.setVisibility(View.VISIBLE);

        editModeButtonsParent.setVisibility(View.VISIBLE);
        changeImageButton.setVisibility(View.VISIBLE);
    }

    private void changeLayoutForStaticMode() {
        childNameEditText.setVisibility(View.GONE);
        childName.setVisibility(View.VISIBLE);

        childDisabilityEditText.setVisibility(View.GONE);
        childDisability.setVisibility(View.VISIBLE);

        childBioEditText.setVisibility(View.GONE);
        childBio.setVisibility(View.VISIBLE);

        childBirthdayButton.setVisibility(View.GONE);
        childBirthday.setVisibility(View.VISIBLE);

        editModeButtonsParent.setVisibility(View.GONE);
        changeImageButton.setVisibility(View.GONE);

        Bitmap imageBitmap = BitmapUtils.getBitmapFromUri(getActivity(), imageUri);
        childImageView.setImageBitmap(imageBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case PICK_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    Uri imgUri = intent.getData();

                    // If it comes from the pick a picture action, get data from intent
                    if (imgUri != null) {
                        newImageUri = imgUri;
                        Bitmap imageBitmap = BitmapUtils.getBitmapFromUri(getActivity(), newImageUri);
                        childImageView.setImageBitmap(imageBitmap);
                    } else if (newImageUri != null) {
                        Bitmap imageBitmap = BitmapUtils.getBitmapFromUri(getActivity(), newImageUri);
                        childImageView.setImageBitmap(imageBitmap);
                    }
                }
                break;
        }
    }
}
