package com.planner.family.therapist.network;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.planner.family.therapist.R;

import static android.app.Activity.RESULT_OK;

public class AddEditContactDialogFragment extends DialogFragment {

    public static final String ID_KEY = "id";
    public static final String DIALOG_TITLE_KEY = "dialogTitle";
    public static final String NAME_KEY = "name";
    public static final String NUMBER_KEY = "number";
    public static final String TYPE_KEY = "type";

    private int contactId = -1;
    private int type = 0;

    public AddEditContactDialogFragment() {
    }

    public static AddEditContactDialogFragment newInstance(String dialogTitle, int contactId, String name, String number, int type) {
        AddEditContactDialogFragment addEditContactDialogFragment =
                new AddEditContactDialogFragment();

        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_KEY, dialogTitle);
        args.putString(NAME_KEY, name);
        args.putString(NUMBER_KEY, number);
        args.putInt(TYPE_KEY, type);
        args.putInt(ID_KEY, contactId);
        addEditContactDialogFragment.setArguments(args);

        return addEditContactDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getActivity(), getTheme());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_edit_contact_dialog_fragment, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(getArguments().getString(DIALOG_TITLE_KEY));

        final EditText nameEditText = dialogView.findViewById(R.id.name_edit_text);
        final EditText numberEditText = dialogView.findViewById(R.id.number_edit_text);

        Button deleteContactButton = dialogView.findViewById(R.id.delete_contact_button);
        type = getArguments().getInt(TYPE_KEY);

        String name = getArguments().getString(NAME_KEY);
        if (name != null && !name.equals("")) {
            nameEditText.setText(name);
            numberEditText.setText(getArguments().getString(NUMBER_KEY));
            contactId = getArguments().getInt(ID_KEY);
            deleteContactButton.setVisibility(View.VISIBLE);
        }

        deleteContactButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(ID_KEY, contactId);
            intent.putExtra(NAME_KEY, "");
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            dismiss();
        });

        dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent();
            if (nameEditText.getText().length() > 0 && numberEditText.getText().length() > 0) {
                intent.putExtra(ID_KEY, contactId);
                intent.putExtra(NAME_KEY, nameEditText.getText().toString());
                intent.putExtra(NUMBER_KEY, numberEditText.getText().toString());
                intent.putExtra(TYPE_KEY, type);
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        return dialogBuilder.create();
    }
}
