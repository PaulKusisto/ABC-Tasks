package com.paulkusisto.abctasks;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Paul on 10/24/2015.
 * This class allows us to talk with the database like it's a simple list.
 */
public class TaskList implements Iterable<Task>{

    private TasksDatabaseHelper dbHelper;

    public TaskList(Context context){
        dbHelper = new TasksDatabaseHelper(context);
    }

    public ArrayList<String> getListDataHeader(){
        ArrayList<String> listDataHeader = new ArrayList<>();  // We use arrayList (instead of List) so we can pass it in an intent
        listDataHeader.addAll(this.getListDataChild().keySet());
        return listDataHeader;
    }

    public HashMap<String, List<Task>> getListDataChild(){
        HashMap<String, List<Task>> listDataChild = new HashMap<>();
        for(Task currentTask : this.dbHelper.getAllTasks()){
            List<Task> headerList;
            try{
                listDataChild.get(currentTask.getHeaderName()).add(currentTask);
            }
            catch (java.lang.NullPointerException e){
                // this header doesn't exist yet - create it now
                headerList = new ArrayList<>();
                headerList.add(currentTask);
                listDataChild.put(currentTask.getHeaderName(),headerList);
            }
        }
        return listDataChild;
    }

    public void createNewTask(String header, String taskName){
        // Adds a new task
        Task newTask = new Task(header, taskName);
        this.dbHelper.createTask(newTask);
    }

    public void editTask(int taskId, Task replacementTask){
        replacementTask.setId(taskId);
        replacementTask.setChecked(this.dbHelper.getTask(taskId).getChecked());
        this.dbHelper.updateTask(replacementTask);
    }

    public void clearCheckedTasks(){
        for(Task task : this.dbHelper.getAllTasks()){
            if(task.getChecked()){
                this.dbHelper.deleteTask(task);
            }
        }
    }

    private void createTestTasks(){
        // Set up some sample tasks

        this.createNewTask("'C' Priority", "write blog post");
        this.createNewTask("'C' Priority", "apply for internships");
        this.createNewTask("'C' Priority", "make video");

        this.createNewTask("'B' Priority", "sleep");
        this.createNewTask("'B' Priority", "math homework");
        this.createNewTask("'B' Priority", "write design paper");

        this.createNewTask("'A' Priority", "finish app!");
        this.createNewTask("'A' Priority", "write business paper");
        this.createNewTask("'A' Priority", "publish app");
    }

    //public void save(){
    //    for(Task task : this.dbHelper.getAllTasks()){
    //        this.dbHelper.updateTask(task);
    //    }
    //}

    @Override
    public Iterator<Task> iterator() {
        return this.dbHelper.getAllTasks().iterator();
    }
}
