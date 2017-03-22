package com.example.andre.nytreader;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Andre on 18/03/2017.
 */

public class FilterDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int startYear, startMonth, startDay;
    private TextView tvStartDate;
    private Spinner spinnerSortOrder;
    private CheckBox cbSports, cbFashion, cbArts;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
       // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(view)
        .setMessage("Set filter criteria")
            .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    saveFilterSettings();
                    MainActivity activity = (MainActivity) getActivity();
                    activity.load();
                }
            })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbFashion.setChecked(FilterCriteria.includeFashion);
        cbSports.setChecked(FilterCriteria.includeSports);
        cbArts.setChecked(FilterCriteria.includeArts);

        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        startDay = FilterCriteria.startDay;
        startMonth = FilterCriteria.startMonth;
        startYear = FilterCriteria.startYear;
        tvStartDate.setText(startDay + "-" + (startMonth + 1)+ "-" + startMonth);

        spinnerSortOrder = (Spinner) view.findViewById(R.id.spinnerSortOrder);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sortOptions, R.layout.support_simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerSortOrder.setAdapter(adapter);
        spinnerSortOrder.setSelection(FilterCriteria.sort);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void saveFilterSettings() {
        FilterCriteria.startDay = startDay;
        FilterCriteria.startMonth = startMonth;
        FilterCriteria.startYear = startYear;
        FilterCriteria.sort = spinnerSortOrder.getSelectedItemPosition();
        FilterCriteria.includeSports = cbSports.isChecked();
        FilterCriteria.includeFashion = cbFashion.isChecked();
        FilterCriteria.includeArts = cbArts.isChecked();
        FilterCriteria.page = 1;
    }

    public void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), this, startYear, startMonth - 1, startDay);
        dialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        tvStartDate.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
        startDay = dayOfMonth;
        startMonth = monthOfYear + 1;
        startYear = year;
    }
}