package com.example.lml.dz_reader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_collect){
            Toast.makeText(this,"收藏开始",Toast.LENGTH_LONG).show();
        } if(itemId == R.id.action_addtion) {
            Toast.makeText(this,"添加开始",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
