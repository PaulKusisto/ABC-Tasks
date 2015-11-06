package com.paulkusisto.abctasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

public class AllTasksActivity extends AppCompatActivity {

    TaskList taskList = new TaskList(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch the Create Task activity
                Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
                intent.putStringArrayListExtra("listDataHeader", taskList.getListDataHeader());
                intent.putExtra("header","");
                intent.putExtra("task","");
                intent.putExtra("id", -1);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void  onResume(){
        super.onResume();
        rebuildTaskList(taskList);
    }

    private void rebuildTaskList(TaskList taskList){
        ExpandableListView expandableTaskList = (ExpandableListView) findViewById(R.id.taskList);
        expandableTaskList.setDividerHeight(2);
        expandableTaskList.setGroupIndicator(null);
        expandableTaskList.setClickable(true);

        ExpandableListAdapter adapter = new ExpandableListAdapter(this, taskList.getListDataHeader(), taskList.getListDataChild());
        expandableTaskList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 2){
                String headerName = data.getStringExtra("headerName");
                String taskName = data.getStringExtra("taskName");
                int taskId = data.getIntExtra("id", -1);

                if (taskId == -1) {
                    // This is a new task
                    taskList.createNewTask(headerName, taskName);
                }
                else{
                    // We're editing an existing task
                    Task replacementTask = new Task(headerName, taskName);
                    taskList.editTask(taskId, replacementTask);
                }
                rebuildTaskList(taskList);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_delete_checked_tasks){
            this.taskList.clearCheckedTasks();
            this.rebuildTaskList(this.taskList);
        }

        return super.onOptionsItemSelected(item);
    }
}
