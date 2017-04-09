package com.chenjinghao.commonutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenjinghao.commonutils.memoryUtils.MemoryManager;
import com.chenjinghao.zxingsdk.zxing.app.CaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvInfo;
    private Button mBtnShowPicture;
    private Button mBtnScanCode;
    private TextView mTvQCodeResult;

    private static final int REQUEST_QRCODE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        tvInfo.setText("内存："+ MemoryManager.getCommonMemoryValue(this)+"---最大内存："+
                MemoryManager.getLargeMemoryValue(this));
    }

    private void initViews() {
        tvInfo = (TextView) findViewById(R.id.tv_info);
        mBtnShowPicture = (Button) findViewById(R.id.btn_show_picture);
        mBtnShowPicture.setOnClickListener(this);
        mBtnScanCode = (Button) findViewById(R.id.btn_scan_q_code);
        mBtnScanCode.setOnClickListener(this);
        mTvQCodeResult = (TextView) findViewById(R.id.tv_show_q_code_result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //扫码逻辑处理
            case REQUEST_QRCODE:
                Log.d("jinghao", "resultCode="+requestCode);
                if (resultCode == Activity.RESULT_OK){
                    Log.d("jinghao", "扫码成功");
                    String scan_result = data.getStringExtra("SCAN_RESULT");
                    mTvQCodeResult.setText("扫码结果是："+scan_result);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_show_picture:
                Intent toShowPicture = new Intent(this, ShowPictureActivity.class);
                startActivity(toShowPicture);
                break;
            case R.id.btn_scan_q_code:
                Intent toScanCode = new Intent(this, CaptureActivity.class);
                startActivityForResult(toScanCode, REQUEST_QRCODE);
//                startActivity(toScanCode);
                break;
        }
    }
}
