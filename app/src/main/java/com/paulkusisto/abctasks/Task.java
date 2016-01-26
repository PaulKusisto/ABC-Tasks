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

    // Constant definitions for Intent extra names
    public static final String HEADER_NAME_INTENT_EXTRA = "passedTaskHeaderName";
    public static final String TASK_NAME_INTENT_EXTRA = "passedTaskTaskName";
    public static final String DUE_DATE_INTENT_EXTRA = "passedTaskDueDate";
    public static final String PRIORITY_INTENT_EXTRA = "passedTaskPriority";
    public static final String ID_INTENT_EXTRA = "passedTaskId";

    public Intent putIntentExtras(Intent intent) {

        intent.putExtra(HEADER_NAME_INTENT_EXTRA, this.headerName);
        intent.putExtra(TASK_NAME_INTENT_EXTRA, this.taskName);
        intent.putExtra(DUE_DATE_INTENT_EXTRA, this.dueDate.getTimeInMillis());
        intent.putExtra(PRIORITY_INTENT_EXTRA, this.priority);
        intent.putExtra(ID_INTENT_EXTRA, this.id);

        return intent;
    }

    public void setTaskFromIntent(Intent intent) {

        this.setHeaderName(intent.getStringExtra(HEADER_NAME_INTENT_EXTRA));
        this.setTaskName(intent.getStringExtra(TASK_NAME_INTENT_EXTRA));

        Calendar dueDate = Calendar.getInstance();
        dueDate.setTimeInMillis(intent.getLongExtra(DUE_DATE_INTENT_EXTRA, 0));
        this.setDueDate(dueDate);

        this.setPriority(intent.getStringExtra(PRIORITY_INTENT_EXTRA));
        //TODO set checked status, or default to unchecked?
        this.setId(intent.getIntExtra(ID_INTENT_EXTRA,-1));
    }
}
