package com.paulkusisto.abctasks;

/**
 * Created by Paul on 10/21/2015.
 * Based on code from http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Task>> _listDataChild;

    private LayoutInflater inflater;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Task>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public Task getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Task childTask = getChild(groupPosition, childPosition);

        convertView = inflater.inflate(R.layout.task_list_item,parent,false);

        //if (convertView == null) {
        //    LayoutInflater infalInflater = (LayoutInflater) this._context
        //            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    convertView = infalInflater.inflate(R.layout.task_list_item, null);
        //}

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        CheckBox childCheckbox = (CheckBox) convertView.findViewById(R.id.cbListItem);

        TextView txtDueDate = (TextView) convertView.findViewById(R.id.lblDueDate);

        // Use the device's regional date format to display the due date
        DateFormat regionalDateFormat = DateFormat.getDateInstance();
        regionalDateFormat.setCalendar(childTask.getDueDate());
        txtDueDate.setText(regionalDateFormat.format(childTask.getDueDate().getTime()));

        // Set the color of the priority indicator
        // TODO: that^

        // Set the Task's name
        txtListChild.setText(childTask.getTaskName());

        // Set up the checkbox to store state changes
        childCheckbox.setChecked(childTask.getChecked());
        childCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // We need to ensure that the task is marked correctly in the database
                TasksDatabaseHelper dbHelper = new TasksDatabaseHelper(_context);

                childTask.setChecked(isChecked);
                dbHelper.updateTask(childTask.getId(), childTask);
            }
        });

        // Tapping on the task opens the editing window
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(_context, EditTaskActivity.class);

                childTask.putIntentExtras(intent);

                intent.putStringArrayListExtra("listDataHeader", (ArrayList<String>) _listDataHeader);

                ((Activity) _context).startActivityForResult(intent, 2);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        convertView = inflater.inflate(R.layout.task_list_group, parent, false);

        //if (convertView == null) {
        //    LayoutInflater infalInflater = (LayoutInflater) this._context
        //            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    convertView = infalInflater.inflate(R.layout.task_list_group, null);
        //}

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
