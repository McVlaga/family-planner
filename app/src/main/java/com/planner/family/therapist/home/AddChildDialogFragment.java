package com.planner.family.therapist.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planner.family.therapist.BuildConfig;
import com.planner.family.therapist.R;
import com.planner.family.therapist.utils.BitmapUtils;
import com.planner.family.therapist.view.CircledImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddChildDialogFragment extends DialogFragment {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    public static final String IMAGE_PATH_KEY = "imageUri";
    public static final String LAST_NAME_KEY = "lastName";
    public static final String FIRST_NAME_KEY = "firstName";
    public static final String DISABILITY_KEY = "disability";
    public static final String BIO_KEY = "bio";
    public static final String BIRTHDAY_MILLIS_KEY = "birthdayMillis";

    private static final int PICK_PHOTO_REQUEST_CODE = 0;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private CircledImageView childImageView;
    private Button addChildImageButton;
    private Button addBirthdayButton;
    private EditText disabilityEditText;
    private EditText bioEditText;

    private Uri imageUri;
    private Bitmap imageBitmap;
    private long birthdayMillis = 0;

    public AddChildDialogFragment() {
    }

    public static AddChildDialogFragment newInstance() {
        return new AddChildDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getActivity(), getTheme());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_child_dialog_fragment, null);

        addChildImageButton = dialogView.findViewById(R.id.add_child_image_button);
        addBirthdayButton = dialogView.findViewById(R.id.add_birthday_button);
        childImageView = dialogView.findViewById(R.id.child_image_view);
        childImageView.setImageBitmap(BitmapUtils.getSquaredBitmapWithColor("#E8EAF6"));

        addChildImageButton.setOnClickListener(view -> showImagePickerDialog());

        addBirthdayButton.setOnClickListener(view -> showDatePickerDialog());

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add a child");

        firstNameEditText = dialogView.findViewById(R.id.first_name_edit_text);
        lastNameEditText = dialogView.findViewById(R.id.last_name_edit_text);
        disabilityEditText = dialogView.findViewById(R.id.disability_edit_text);
        bioEditText = dialogView.findViewById(R.id.bio_edit_text);


        dialogBuilder.setPositiveButton("Save", null);
        dialogBuilder.setNegativeButton("Cancel", null);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(dialog -> {

            Button saveChildButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveChildButton.setOnClickListener(view -> {
                if (isInputGood()) {
                    Intent intent = new Intent();
                    intent.putExtra(LAST_NAME_KEY, lastNameEditText.getText().toString());
                    intent.putExtra(FIRST_NAME_KEY, firstNameEditText.getText().toString());
                    intent.putExtra(DISABILITY_KEY, disabilityEditText.getText().toString());
                    intent.putExtra(BIO_KEY, bioEditText.getText().toString());
                    if (birthdayMillis > 0) {
                        intent.putExtra(BIRTHDAY_MILLIS_KEY, birthdayMillis);
                    }

                    File imageFile = BitmapUtils.getCroppedScaledBitmap(imageBitmap);
                    imageUri = FileProvider.getUriForFile(getActivity(), AUTHORITY, imageFile);

                    intent.putExtra(IMAGE_PATH_KEY, imageUri.toString());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                    alertDialog.dismiss();
                }
            });
        });

        return alertDialog;
    }

    private void showDatePickerDialog() {
        Calendar birthdayCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    birthdayCalendar.set(year, monthOfYear, dayOfMonth);
                    birthdayMillis = birthdayCalendar.getTimeInMillis();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                    addBirthdayButton.setText(dateFormat.format(birthdayCalendar.getTime()));
                },
                birthdayCalendar.get(Calendar.YEAR),
                birthdayCalendar.get(Calendar.MONTH),
                birthdayCalendar.get(Calendar.DATE));
        datePicker.show();
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
        imageUri = FileProvider.getUriForFile(getActivity(), AUTHORITY, photo);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(chooserIntent, PICK_PHOTO_REQUEST_CODE);
    }

    private boolean isInputGood() {
        if (imageBitmap == null) {
            Toast.makeText(getActivity(), "You should add a picture", Toast.LENGTH_LONG).show();
            return false;
        }
        if (firstNameEditText.getText().length() == 0) {
            Toast.makeText(getActivity(), "Type the first name please", Toast.LENGTH_LONG).show();
            return false;
        }
        if (lastNameEditText.getText().length() == 0) {
            Toast.makeText(getActivity(), "Type the last name please", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case PICK_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    Uri imgUri = intent.getData();

                    // If it comes from the pick a picture action, get data from intent
                    if (imgUri != null) {
                        imageUri = imgUri;
                    }
                    imageBitmap = BitmapUtils.getBitmapFromUri(getActivity(), imageUri);
                    childImageView.setImageBitmap(imageBitmap);
                }
                break;
        }
    }
}