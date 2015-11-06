package com.paulkusisto.abctasks;

/**
 * Created by Paul on 10/24/2015.
 * Defines a single task, which sits under a header
 */
public class Task{
    private int id;
    private String taskName;
    private String headerName;
    private Boolean checked;

    public Task(String headerName, String taskName){
        this.headerName = headerName;
        this.taskName = taskName;
        this.checked = false;
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

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }
}
