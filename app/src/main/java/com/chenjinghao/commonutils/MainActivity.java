package com.chenjinghao.commonutils;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenjinghao.commonutils.memoryUtils.MemoryManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvInfo;
    private Button mBtnShowPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tv_info);
        mBtnShowPicture = (Button) findViewById(R.id.btn_show_picture);

        tvInfo.setText("内存："+ MemoryManager.getCommonMemoryValue(this)+"---最大内存："+
                MemoryManager.getLargeMemoryValue(this));
        mBtnShowPicture.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_show_picture:
                Intent toShowPicture = new Intent(this, ShowPictureActivity.class);
                startActivity(toShowPicture);
                break;
        }
    }
}
