package com.paulkusisto.abctasks;


import android.content.Context;
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
import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent sender = getIntent();
        ArrayList<String> listDataHeader = sender.getStringArrayListExtra("listDataHeader");

        // create the passed task
        final Task passedTask = new Task("","");
        passedTask.setTaskFromIntent(sender);

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

        taskHeaderSpinner.setSelection(listDataHeader.indexOf(passedTask.getHeaderName()));

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

        // set the other fields to the passed values
        taskText.setText(passedTask.getTaskName());

        taskDueDatePicker.updateDate(passedTask.getDueDate().get(Calendar.YEAR),
                passedTask.getDueDate().get(Calendar.MONTH),
                passedTask.getDueDate().get(Calendar.DAY_OF_MONTH));

        //TODO: Priority selection must be set

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

                // create the new task
                Task taskToPass = new Task(headerName,taskName);
                taskToPass.setId(passedTask.getId());  // we already know the ID

                // get the task priority
                // TODO
                taskToPass.setPriority(getResources().getString(R.string.priority_low));  // Default to low priority

                // get the calendar info for due date
                Calendar dueDate = Calendar.getInstance();
                dueDate.set(Calendar.YEAR,taskDueDatePicker.getYear());
                dueDate.set(Calendar.MONTH,taskDueDatePicker.getMonth());
                dueDate.set(Calendar.DAY_OF_MONTH,taskDueDatePicker.getDayOfMonth());
                taskToPass.setDueDate(dueDate);
                //TODO: time zone stuff?

                Intent intent = new Intent();
                taskToPass.putIntentExtras(intent);
//
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
    }
}
