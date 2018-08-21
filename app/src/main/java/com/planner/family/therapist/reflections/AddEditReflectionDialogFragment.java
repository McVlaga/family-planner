package com.planner.family.therapist.reflections;

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

public class AddEditReflectionDialogFragment extends DialogFragment {

    public static final String ID_KEY = "id";
    public static final String DIALOG_TITLE_KEY = "dialogTitle";
    public static final String REFLECTION_KEY = "reflection";

    private int reflectionId = -1;

    public AddEditReflectionDialogFragment() {
    }

    public static AddEditReflectionDialogFragment newInstance(String dialogTitle,
                                                              int reflectionId,
                                                              String reflection) {
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                new AddEditReflectionDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ID_KEY, reflectionId);
        args.putString(DIALOG_TITLE_KEY, dialogTitle);
        args.putString(REFLECTION_KEY, reflection);
        addEditReflectionDialogFragment.setArguments(args);

        return addEditReflectionDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getActivity(), getTheme());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_edit_reflection_dialog_fragment, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(getArguments().getString(DIALOG_TITLE_KEY));

        final EditText reflectionEditText = dialogView.findViewById(R.id.reflection_edit_text);

        Button deleteReflectionButton = dialogView.findViewById(R.id.delete_reflection_button);

        String reflection = getArguments().getString(REFLECTION_KEY);
        if (reflection != null && !reflection.equals("")) {
            reflectionEditText.setText(getArguments().getString(REFLECTION_KEY));
            reflectionId = getArguments().getInt(ID_KEY);
            deleteReflectionButton.setVisibility(View.VISIBLE);
        }

        deleteReflectionButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(ID_KEY, reflectionId);
            intent.putExtra(REFLECTION_KEY, "");
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            dismiss();
        });

        dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent();
            if (reflectionEditText.getText().length() > 0) {
                intent.putExtra(ID_KEY, reflectionId);
                intent.putExtra(REFLECTION_KEY, reflectionEditText.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        return dialogBuilder.create();
    }
}