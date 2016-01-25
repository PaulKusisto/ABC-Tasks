package com.paulkusisto.abctasks;

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
        this.headerName = headerName;
        this.taskName = taskName;
        this.checked = false;

        // By default, set the priority to low.
        this.setPriority("Low");  //TODO: replace the string in this line with a string resource

        // By default, set the due date to the Unix Epoch
        Calendar epoch = Calendar.getInstance();
        TimeZone utc = TimeZone.getTimeZone("UTC");
        epoch.setTimeZone(utc);
        epoch.set(Calendar.YEAR, 1970);
        epoch.set(Calendar.MONTH, Calendar.JANUARY);
        epoch.set(Calendar.DATE, 1);
        epoch.set(Calendar.HOUR_OF_DAY, 0);
        epoch.set(Calendar.MINUTE, 0);
        epoch.set(Calendar.SECOND, 0);
        epoch.set(Calendar.MILLISECOND, 0);
        this.setDueDate(epoch);
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
}
