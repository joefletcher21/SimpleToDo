package com.example.josephfletcher.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.josephfletcher.simpletodo.MainActivity.TASK_POSITION;
import static com.example.josephfletcher.simpletodo.MainActivity.TASK_TEXT;

public class EditTaskActivity extends AppCompatActivity {

    //Track edit text
    EditText editText;
    // Position of edited task in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        // resolve edit text from intent extra
        editText = (EditText) findViewById(R.id.editText);
        // set edit text value from intent extra
        editText.setText(getIntent().getStringExtra(TASK_TEXT));
        //update position from intent extra
        position = getIntent().getIntExtra(TASK_POSITION, 0);
        // update the title bar of activity
        getSupportActionBar().setTitle("Edit Task");

    }

    //Handler for save button
    public void onSaveTask(View v) {
        // prepare new intent for result
        Intent i = new Intent();
        // pas updated task as extra
        i.putExtra(TASK_TEXT, editText.getText().toString());
        // pss original position as extra
        i.putExtra(TASK_POSITION, position);
        // set the intent as the result of activity
        setResult(RESULT_OK, i);
        // close activity and redirect to Main
        finish();
    }

}
