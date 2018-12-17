package com.example.josephfletcher.simpletodo;

import android.os.Bundle;
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

