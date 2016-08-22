package com.google.calculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by 追风少年 on 2016/8/19.
 */
public class SettingActivity extends AppCompatActivity {

    private ListView mLv;
    String[] arr = {getString(R.string.precision_1),
            getString(R.string.precision_2),
            getString(R.string.precision_3),
            getString(R.string.precision_4),
            getString(R.string.precision_5),
            getString(R.string.precision_6),
            getString(R.string.precision_7),
            getString(R.string.precision_8),
            getString(R.string.precision_9),
            getString(R.string.precision_10)};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, arr));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra(Constants.PRECISION,String.valueOf(position+1));
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                        .setTitle(getString(R.string.precision_options) + (position + 1))
                        .setPositiveButton(getString(R.string.ensure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
