package com.google.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private Button[] btnNum = new Button[11];// 数值按钮
    private Button[] btnCommand = new Button[5];// 符号按钮
    private EditText editText = null;// 显示区域
    private Button btnClear = null; // clear按钮
    private String lastCommand; // 用于保存运算符
    private boolean clearFlag; // 用于判断是否清空显示区域的值,true需要,false不需要
    private boolean firstFlag; // 用于判断是否是首次输入,true首次,false不是首次
    private double result; // 计算结果
    private SlidingDrawer mSd;
    private ImageView mIvArrow1;
    private ImageView mIvArrow2;
    private Button mBtn_sin;
    private String mPrecision;

    public MainActivity() {
        // 初始化各项值
        result = 0; // x的值
        firstFlag = true; // 是首次运算
        clearFlag = false; // 不需要清空
        lastCommand = "="; // 运算符
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initArrowAnim();
    }

    private void initView() {
        // 获取运算符
        btnCommand[0] = (Button) findViewById(R.id.add);
        btnCommand[1] = (Button) findViewById(R.id.subtract);
        btnCommand[2] = (Button) findViewById(R.id.multiply);
        btnCommand[3] = (Button) findViewById(R.id.divide);
        btnCommand[4] = (Button) findViewById(R.id.equal);
        // 获取数字
        btnNum[0] = (Button) findViewById(R.id.num0);
        btnNum[1] = (Button) findViewById(R.id.num1);
        btnNum[2] = (Button) findViewById(R.id.num2);
        btnNum[3] = (Button) findViewById(R.id.num3);
        btnNum[4] = (Button) findViewById(R.id.num4);
        btnNum[5] = (Button) findViewById(R.id.num5);
        btnNum[6] = (Button) findViewById(R.id.num6);
        btnNum[7] = (Button) findViewById(R.id.num7);
        btnNum[8] = (Button) findViewById(R.id.num8);
        btnNum[9] = (Button) findViewById(R.id.num9);
        btnNum[10] = (Button) findViewById(R.id.point);

        //获取slidingDrawer中控件
        mSd = (SlidingDrawer) findViewById(R.id.sd_pm_setting);
        mIvArrow1 = (ImageView) findViewById(R.id.iv_pm_arrow_1);
        mIvArrow2 = (ImageView) findViewById(R.id.iv_pm_arrow_2);
        mBtn_sin = (Button) findViewById(R.id.btn_sin);
        // 初始化显示结果区域
        editText = (EditText) findViewById(R.id.result);
        editText.setText("0.0");
        editText.setSelection(editText.getText().length());
        // 实例化监听器对象
        NumberAction na = new NumberAction();
        CommandAction ca = new CommandAction();
        for (Button bc : btnCommand) {
            bc.setOnClickListener(ca);
        }
        for (Button bc : btnNum) {
            bc.setOnClickListener(na);
        }
        // clear按钮的动作
        btnClear = (Button) findViewById(R.id.clear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("0.0");
                // 初始化各项值
                result = 0; // x的值
                firstFlag = true; // 是首次运算
                clearFlag = false; // 不需要清空
                lastCommand = "="; // 运算符
            }
        });
        mBtn_sin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                double tangle = Double.parseDouble(editText.getText().toString());
                double sin = Math.sin(tangle * Math.PI / 180);
                ChangePrecision(sin);
            }
        });

        // SlidingDrawer打开关闭监听
        mSd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                stopArrowAnim();
            }
        });
        mSd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                initArrowAnim();
            }
        });

    }

    // 数字按钮监听器
    private class NumberAction implements OnClickListener {
        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            String input = btn.getText().toString();
            if (firstFlag) { // 首次输入
                // 一上就".",就什么也不做
                if (input.equals(".")) {
                    return;
                }
                // 如果是"0.0"的话,就清空
                if (editText.getText().toString().equals("0.0")) {
                    editText.setText("");
                }
                firstFlag = false;// 改变是否首次输入的标记值
            } else {
                String editTextStr = editText.getText().toString();
                // 判断显示区域的值里面是否已经有".",如果有,输入的又是".",就什么都不做
                if (editTextStr.indexOf(".") != -1 && input.equals(".")) {
                    return;
                }
                // 判断显示区域的值里面只有"-",输入的又是".",就什么都不做
                if (editTextStr.equals("-") && input.equals(".")) {
                    return;
                }
                // 判断显示区域的值如果是"0",输入的不是".",就什么也不做
                if (editTextStr.equals("0") && !input.equals(".")) {
                    return;
                }
            }
            // 如果我点击了运算符以后,再输入数字的话,就要清空显示区域的值
            if (clearFlag) {
                editText.setText("");
                clearFlag = false;// 还原初始值,不需要清空
            }
            editText.setText(editText.getText().toString() + input);// 设置显示区域的值
        }
    }

    // 符号按钮监听器
    private class CommandAction implements OnClickListener {
        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            String inputCommand = (String) btn.getText();
            if (firstFlag) {// 首次输入"-"的情况
                if (inputCommand.equals("-")) {
                    editText.setText("-");// 显示区域的内容设置为"-"
                    firstFlag = false;// 改变首次输入的标记
                }
            } else {
                if (!clearFlag) {// 如果flag=false不需要清空显示区的值,就调用方法计算
                    calculate(Double.parseDouble(editText.getText().toString()));// 保存显示区域的值,并计算
                }
                // 保存你点击的运算符
                lastCommand = inputCommand;
                clearFlag = true;// 因为我这里已经输入过运算符,
            }
        }
    }

    // 计算用的方法
    private void calculate(double x) {


        if (lastCommand.equals("+")) {
            result += x;
        } else if (lastCommand.equals("-")) {
            result -= x;
        } else if (lastCommand.equals("*")) {
            result *= x;
        } else if (lastCommand.equals("/")) {
            result /= x;
        } else if (lastCommand.equals("=")) {
            result = x;
        }
        ChangePrecision(result);

    }

    private void ChangePrecision(double result) {
        if (mPrecision == null) {
            editText.setText("" + result);
            Log.d("tag", "ds" + mPrecision);
        } else {
            Log.d("tag", "ds" + mPrecision);
            String resultString = String.valueOf(result);
            int precison = Integer.parseInt(mPrecision);
            int index = resultString.indexOf(".");
            String substring ="";
            if (resultString.length() - index > precison) {
                substring = resultString.substring(0, index + precison + 1);
            } else {
                substring = resultString;
            }
            editText.setText(substring);
        }
    }

    private void initArrowAnim() {
        AlphaAnimation anim1 = new AlphaAnimation(0.2f, 1.0f);
        anim1.setDuration(1000);
        anim1.setRepeatCount(Animation.INFINITE);
        anim1.setRepeatMode(Animation.REVERSE);
        AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.2f);
        anim2.setDuration(1000);
        anim2.setRepeatCount(Animation.INFINITE);
        anim2.setRepeatMode(Animation.REVERSE);
        // 重新开始要重新设置图片
        mIvArrow1.setImageResource(R.drawable.drawer_arrow_up);
        mIvArrow2.setImageResource(R.drawable.drawer_arrow_up);
        mIvArrow1.startAnimation(anim1);
        mIvArrow2.startAnimation(anim2);
    }

    protected void stopArrowAnim() {
        mIvArrow1.setImageResource(R.drawable.drawer_arrow_down);
        mIvArrow2.setImageResource(R.drawable.drawer_arrow_down);
        // 停止动画
        mIvArrow1.clearAnimation();
        mIvArrow2.clearAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_button:
                startActivityForResult(new Intent(MainActivity.this, SettingActivity.class), REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
             mPrecision = data.getStringExtra(Constants.PRECISION);
        }

    }
}
