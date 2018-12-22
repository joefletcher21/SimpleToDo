package com.example.josephfletcher.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    //numeric code to identify edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used to pass data between activities
    public final static String TASK_TEXT = "taskText";
    public final static String TASK_POSITION = "taskPosition";

    ArrayList<String>tasks;
    ArrayAdapter<String>taskAdapter;
    ListView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readTasks();
        taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        taskList = ((ListView)findViewById(R.id.taskList));
        taskList.setAdapter(taskAdapter);
        // Mock Data
        //tasks.add("Clean Room");
        //tasks.add("Practice guitar");
        setupListViewListener();
    }

    public void onAddTask(View v){
        String taskToAdd = ((EditText) findViewById(R.id.todo)).getText().toString();
        if(taskToAdd.equals("") != true){
            taskAdapter.add(taskToAdd);
            ((TextView) findViewById(R.id.todo)).setText("");
            writeTasks();
            Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Item unable to be added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener on list view");
        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list");
                tasks.remove(position);
                taskAdapter.notifyDataSetChanged();
                writeTasks();
                return true;
            }
        });
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create the new Activity
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                //Pass data being edited
                intent.putExtra(TASK_TEXT, tasks.get(position));
                intent.putExtra(TASK_POSITION, position);
                //Display Activity
                MainActivity.this.startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    // Handle results from edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            // extract updated item from result intent extras
            String updatedTask = data.getExtras().getString(TASK_TEXT);
            //extract original position of edited task
            int position = data.getExtras().getInt(TASK_POSITION);
            // update model with new task text at edited position
            tasks.set(position, updatedTask);
            // notify adapter that model changed
            taskAdapter.notifyDataSetChanged();
            // persist the changed model
            writeTasks();
            // notify user operation completed ok
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readTasks(){
        try {
            tasks = new ArrayList<>(FileUtils.readLines(getDataFile(), String.valueOf(Charset.defaultCharset())));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            tasks = new ArrayList<>();
        }
    }

    private void writeTasks(){
        try {
            FileUtils.writeLines(getDataFile(), tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

