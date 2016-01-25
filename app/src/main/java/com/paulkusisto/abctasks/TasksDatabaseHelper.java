package com.paulkusisto.abctasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 10/24/2015.
 * Based on code from http://examples.javacodegeeks.com/android/core/database/android-database-example/
 * TODO: document methods in this class
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String TASK_DATABASE_NAME = "tasksDB";
    private static final String TASK_TABLE_NAME = "tasks";

    private static final String task_ID = "id";
    private static final String task_HEADER = "header";
    private static final String task_TASK = "task";
    private static final String task_CHECKED = "checked";
    private static final String task_PRIORITY = "priority";  // Stored as a string
    private static final String task_DUEDATE = "dueDate";  // Stored as a long (Unix time * 1000)

    private static final String TASK_TABLE_CREATE = "CREATE TABLE " + TASK_TABLE_NAME + " ( "
            + task_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + task_HEADER + " TEXT, "
            + task_TASK + " TEXT, "
            + task_CHECKED + " INTEGER, "
            + task_PRIORITY + " TEXT, "
            + task_DUEDATE + " LONG" + ");";

    TasksDatabaseHelper(Context context){
        super(context, TASK_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        this.onCreate(db);
    }

    private ContentValues getTaskValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(task_HEADER, task.getHeaderName());
        values.put(task_TASK, task.getTaskName());
        values.put(task_CHECKED, task.getChecked() ? 1 : 0);
        values.put(task_PRIORITY, task.getPriority());
        values.put(task_DUEDATE, task.getDueDate().getTimeInMillis());

        return values;
    }

    /**
     * Creates a new task, and writes it to the database.
     *
     * @param task A task object to be added to the database.  The task's ID field is ignored by this function.
     */
    public void createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // insert task
        db.insert(TASK_TABLE_NAME, null, getTaskValues(task));

        // close database transaction
        db.close();
    }

    /**
     * Changes the ID of replacementTask to taskID, and overwrites the
     * corresponding task (matching ID) in the database with the replacementTask.
     *
     * @param taskId The ID of the task to be replaced.
     * @param replacementTask A complete task, which will overwrite the selected task.
     */
    public int updateTask(Integer taskId, Task replacementTask){
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TASK_TABLE_NAME,
                getTaskValues(replacementTask),
                task_ID + " = ?",
                new String[] { String.valueOf(taskId)}
        );

        db.close();
        return i;
    }

    public List<Task> getAllTasks(){
        List<Task> allTasks = new ArrayList<>();

        String query = "SELECT * FROM  " + TASK_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                allTasks.add(getTaskFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allTasks;
    }

    public Task getTask(int taskId){

        String query = "SELECT * FROM  " + TASK_TABLE_NAME + " WHERE " + task_ID + " = " + taskId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return getTaskFromCursor(cursor);
    }

    private Task getTaskFromCursor(Cursor cursor){
        // create the basic task
        Task task = new Task(cursor.getString(1),cursor.getString(2));
        // set the correct ID
        task.setId(Integer.parseInt(cursor.getString(0)));
        // set if the task has been checked
        if(Integer.parseInt(cursor.getString(3)) == 1){
            task.setChecked(true);
        }
        // set the priority string
        task.setPriority(cursor.getString(4));
        // create a new calendar, and set its time from the stored Long value
        Calendar taskDueDate = Calendar.getInstance();
        taskDueDate.setTimeInMillis(cursor.getLong(5));
        task.setDueDate(taskDueDate);

        return task;
    }

    public void clearAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        onCreate(db);
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TASK_TABLE_NAME, task_ID + " = ?", new String[] { String.valueOf(task.getId())});
        db.close();
    }
}
