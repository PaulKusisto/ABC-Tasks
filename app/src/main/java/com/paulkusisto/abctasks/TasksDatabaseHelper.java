package com.paulkusisto.abctasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 10/24/2015.
 * Based on code from http://examples.javacodegeeks.com/android/core/database/android-database-example/
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String TASK_DATABASE_NAME = "tasksDB";
    private static final String TASK_TABLE_NAME = "tasks";
    private static final String task_ID = "id";
    private static final String task_HEADER = "header";
    private static final String task_TASK = "task";
    private static final String task_CHECKED = "checked";
    private static final String TASK_TABLE_CREATE = "CREATE TABLE " + TASK_TABLE_NAME + " ( "
            + task_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + task_HEADER + " TEXT, "
            + task_TASK + " TEXT, "
            + task_CHECKED + " INTEGER" + ");";

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

    public void createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(task_HEADER, task.getHeaderName());
        values.put(task_TASK, task.getTaskName());
        values.put(task_CHECKED, task.getChecked() ? 1 : 0);

        // insert task
        db.insert(TASK_TABLE_NAME, null, values);

        // close database transaction
        db.close();
    }

    public int updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(task_HEADER, task.getHeaderName());
        values.put(task_TASK, task.getTaskName());
        values.put(task_CHECKED, task.getChecked() ? 1 : 0);

        int i = db.update(TASK_TABLE_NAME, values, task_ID + " = ?", new String[] { String.valueOf(task.getId())});

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
        Task task = new Task(cursor.getString(1),cursor.getString(2));
        task.setId(Integer.parseInt(cursor.getString(0)));
        if(Integer.parseInt(cursor.getString(3)) == 1){
            task.setChecked(true);
        }
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
