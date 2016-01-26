package com.paulkusisto.abctasks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Paul on 10/24/2015.
 * Defines a single task, which sits under a header.
 *
 * The required fields for this class are a headerName, (String) and a taskName (String).
 *
 * It can also include a priority value, and a due date.
 */
public class Task{
    private int id;
    private String taskName;
    private String headerName;
    private Boolean checked;
    private String priority;
    private Calendar dueDate;

    /**
     * Initializes a new Task object.
     *
     * @param headerName The header under which this task will sit.
     * @param taskName The name of this task - this String is the core of the task from the user's perspective.
     */
    public Task(String headerName, String taskName) {
        this.setHeaderName(headerName);
        this.setTaskName(taskName);

        this.setChecked(false);

        // By default, set id to be -1, to differentiate new tasks from tasks which exist in the database
        this.setId(-1);

        // By default, set the priority to the empty string
        this.setPriority("");

        //// By default, set the due date to the Unix Epoch
        //Calendar epoch = Calendar.getInstance();
        //TimeZone utc = TimeZone.getTimeZone("UTC");
        //epoch.setTimeZone(utc);
        //epoch.set(Calendar.YEAR, 1970);
        //epoch.set(Calendar.MONTH, Calendar.JANUARY);
        //epoch.set(Calendar.DATE, 1);
        //epoch.set(Calendar.HOUR_OF_DAY, 0);
        //epoch.set(Calendar.MINUTE, 0);
        //epoch.set(Calendar.SECOND, 0);
        //epoch.set(Calendar.MILLISECOND, 0);

        // By default, set the due date to today
        Calendar cal = Calendar.getInstance();
        this.setDueDate(cal);
    }

    public String getTaskName(){
        return taskName;
    }

    public String getHeaderName(){
        return headerName;
    }

    public Boolean getChecked(){
        return checked;
    }

    public String getPriority(){
        return priority;
    }

    public Calendar getDueDate(){
        return dueDate;
    }

    public int getId(){
        return id;
    }

    private void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public void setPriority(String priority) {
        // set the priority lazily for now
        // TODO: this function should validate input
        this.priority = priority;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public Intent putIntentExtras(Intent intent) {

        //TODO: make these names consts, and prefix a term to keep them separate from other extras
        intent.putExtra("headerName", this.headerName);
        intent.putExtra("taskName", this.taskName);
        intent.putExtra("dueDate", this.dueDate.getTimeInMillis());
        intent.putExtra("priority", this.priority);
        intent.putExtra("id", this.id);

        return intent;
    }

    public void setTaskFromIntent(Intent intent) {

        this.setHeaderName(intent.getStringExtra("headerName"));
        this.setTaskName(intent.getStringExtra("taskName"));

        Calendar dueDate = Calendar.getInstance();
        dueDate.setTimeInMillis(intent.getLongExtra("dueDate", 0));
        this.setDueDate(dueDate);
        //Log.i("TaskCreationInfo", Long.toString(intent.getLongExtra("dueDate", 0)));

        this.setPriority(intent.getStringExtra("priority"));
        //TODO this.setChecked();
        this.setId(intent.getIntExtra("id",-1));
    }
}
