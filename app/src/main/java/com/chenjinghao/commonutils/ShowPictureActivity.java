package com.chenjinghao.commonutils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chenjinghao.commonutils.fileUtils.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShowPictureActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvShowPic;
    private Button mBtnChoosePic;
    private Button mBtnChangeOption;
    private Button mBtnChangeRGB;
    private Button mBtnPartLoad;
    private Button mBtnMovePic;

    private Bitmap mBitmap = null;
    private File mFile = null;
    private int SCREEN_WIDTH, SCREEN_HEIGHT;
    private int shiftpx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;

        initWidget();
        initListener();
    }

    private void initListener() {
        mBtnChoosePic.setOnClickListener(this);
        mBtnChangeOption.setOnClickListener(this);
        mBtnChangeRGB.setOnClickListener(this);
        mBtnPartLoad.setOnClickListener(this);
        mBtnMovePic.setOnClickListener(this);
    }

    private void initWidget() {
        mIvShowPic = (ImageView) findViewById(R.id.iv_show_picture);
        mBtnChoosePic = (Button) findViewById(R.id.btn_choose_picture);
        mBtnChangeOption = (Button) findViewById(R.id.btn_change_option);
        mBtnChangeRGB = (Button) findViewById(R.id.btn_change_rgb);
        mBtnPartLoad = (Button) findViewById(R.id.btn_part_lode);
        mBtnMovePic = (Button) findViewById(R.id.btn_move_picture);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_picture:
                Intent choosePicIntent = new Intent(Intent.ACTION_PICK, null);
                choosePicIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(choosePicIntent, 1);
                break;
            case R.id.btn_change_option:
                changePicOption();
                break;
            case R.id.btn_change_rgb:
                changePicOptionRGB();
                break;
            case R.id.btn_part_lode:
                partLode();
                break;
            case R.id.btn_move_picture:
                shiftpx += 10;
                partLode();
                break;
        }
    }

    private void partLode() {
        if (mFile == null) return;
        try {
            FileInputStream fin = new FileInputStream(mFile);
            BitmapFactory.Options tempOptions = new BitmapFactory.Options();
            tempOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fin, null, tempOptions);
            int width = tempOptions.outWidth;
            int height = tempOptions.outHeight;

            //设置显示图片的中心区域
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(fin, false);
            BitmapFactory.Options realOption = new BitmapFactory.Options();
            mBitmap = decoder.decodeRegion(new Rect(width / 2 - SCREEN_WIDTH / 2 + shiftpx,
                    height / 2 - SCREEN_HEIGHT / 2, width / 2 + SCREEN_WIDTH / 2 + shiftpx,
                    height / 2 + SCREEN_HEIGHT / 2), realOption);
            Log.d("jinghao", "bitmap.length:" + mBitmap.getByteCount());
            mIvShowPic.setImageBitmap(mBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePicOptionRGB() {
        if (mFile == null) return;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            FileInputStream fin = new FileInputStream(mFile);
            mBitmap = BitmapFactory.decodeStream(fin, null, options);
            Log.d("jinghao", "bitmap.length:" + mBitmap.getByteCount());
            mIvShowPic.setImageBitmap(mBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void changePicOption() {
        if (mFile == null) return;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(mFile), null, options);
            int tempWidth = options.outWidth;
            int tempHeight = options.outHeight;
            int scale = 2;
            while ((tempWidth / scale) > SCREEN_WIDTH) {
                scale *= 2;
            }
            scale /= 2;
            BitmapFactory.Options realOption = new BitmapFactory.Options();
            realOption.inSampleSize = scale;
            Log.d("jinghao", "縮放比例：" + scale);
            FileInputStream fin = new FileInputStream(mFile);
            mBitmap = BitmapFactory.decodeStream(fin, null, realOption);
            Log.d("jinghao", "bitmap.length:" + mBitmap.getByteCount());
            mIvShowPic.setImageBitmap(mBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String path = FileManager.getRealPathFromURI(this, data.getData());
        Log.d("jinghao", "requestCode:" + requestCode + "---resultCode:" + resultCode +
                "--data.toString():" + data.toString() + "--data.getData:" + data.getData());
        String path = FileManager.getRealPathFromURI(this, data.getData());
        mFile = new File(path);
        if (mFile == null) return;
        if (mFile.length() == 0) {
            mFile.delete();
            return;
        }
        Log.d("jinghao", "file:" + mFile.getName() + ",lenght:" + mFile.length());
        try {
            FileInputStream fin = new FileInputStream(mFile);
            mBitmap = BitmapFactory.decodeStream(fin);
            Log.d("jinghao", "bitmap length:" + mBitmap.getByteCount());
            mIvShowPic.setImageBitmap(mBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
