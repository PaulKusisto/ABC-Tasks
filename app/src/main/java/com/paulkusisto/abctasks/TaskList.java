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

    /**
     * Inits the tasklist by opening a connection to the database.
     *
     * @param context The application context used to open the database connection.
     */
    public TaskList(Context context){
        dbHelper = new TasksDatabaseHelper(context);
    }

    /**
     * Builds a list of headers which already exist in the database.
     *
     * @return An ArrayList of the unique header names found in the database.
     */
    public ArrayList<String> getListDataHeader(){
        ArrayList<String> listDataHeader = new ArrayList<>();  // We use arrayList (instead of List) so we can pass it in an intent
        listDataHeader.addAll(this.getListDataChild().keySet());
        return listDataHeader;
    }

    /**
     * Builds a HashMap for use with ExpandableListAdapter.
     *
     * @return A HashMap mapping headers (strings) to lists of Task objects.
     */
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

    /**
     * Removes from the database all tasks which have a checked value of True
     */
    public void clearCheckedTasks(){
        for(Task task : this.dbHelper.getAllTasks()){
            if(task.getChecked()){
                this.dbHelper.deleteTask(task);
            }
        }
    }

    /*private void createTestTasks(){
        this.createNewTask("'C' Priority", "write blog post");
        this.createNewTask("'C' Priority", "apply for internships");
        this.createNewTask("'C' Priority", "make video");

        this.createNewTask("'B' Priority", "sleep");
        this.createNewTask("'B' Priority", "math homework");
        this.createNewTask("'B' Priority", "write design paper");

        this.createNewTask("'A' Priority", "finish app!");
        this.createNewTask("'A' Priority", "write business paper");
        this.createNewTask("'A' Priority", "publish app");
    }*/

    @Override
    public Iterator<Task> iterator() {
        return this.dbHelper.getAllTasks().iterator();
    }
}
