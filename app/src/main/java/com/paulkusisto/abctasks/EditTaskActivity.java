package com.paulkusisto.abctasks;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent sender = getIntent();
        ArrayList<String> listDataHeader = sender.getStringArrayListExtra("listDataHeader");
        taskId = sender.getIntExtra("id",-1);

        // get all of the elements we'll be using
        final EditText taskHeaderText = (EditText) findViewById(R.id.createTask_HeaderText);
        final EditText taskText = (EditText) findViewById(R.id.createTask_TaskText);
        final Spinner taskHeaderSpinner = (Spinner) findViewById(R.id.createTask_HeaderSpinner);
        final DatePicker taskDueDatePicker = (DatePicker) findViewById(R.id.createTask_DueDatePicker);

        // set the spinner options from the intent data, with one extra option for creating a new header
        listDataHeader.add(getString(R.string.create_task_header_spinner_create_task));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listDataHeader);
        taskHeaderSpinner.setAdapter(adapter);
        taskHeaderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (taskHeaderSpinner.getSelectedItem().toString().equals(getString(R.string.create_task_header_spinner_create_task))) {
                    taskHeaderText.setVisibility(View.VISIBLE);
                } else {
                    taskHeaderText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                taskHeaderText.setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the task header
                String headerName;
                String selectedSpinnerOptionString = taskHeaderSpinner.getSelectedItem().toString();
                if (selectedSpinnerOptionString.equals(getString(R.string.create_task_header_spinner_create_task))) {
                    headerName = taskHeaderText.getText().toString();
                } else {
                    headerName = selectedSpinnerOptionString;
                }

                // get the task name
                String taskName = taskText.getText().toString();

                // get the calendar info for due date
                Integer dueDateYear = taskDueDatePicker.getYear();
                Integer dueDateMonth = taskDueDatePicker.getMonth();
                Integer dueDateDayOfMonth = taskDueDatePicker.getDayOfMonth();

                Intent intent = new Intent();
                intent.putExtra("headerName", headerName);
                intent.putExtra("taskName", taskName);
                intent.putExtra("dueDateYear", dueDateYear);
                intent.putExtra("dueDateMonth", dueDateMonth);
                intent.putExtra("dueDateDayOfMonth", dueDateDayOfMonth);
                intent.putExtra("id", taskId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        taskHeaderSpinner.setSelection(listDataHeader.indexOf(sender.getStringExtra("header")));
        taskText.setText(sender.getStringExtra("task"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
    }
}
