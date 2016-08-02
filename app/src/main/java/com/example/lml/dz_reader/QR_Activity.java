package com.example.lml.dz_reader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QR_Activity extends AppCompatActivity implements QRCodeView.Delegate {
        private static final String TAG = QR_Activity.class.getSimpleName();
        private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

        private QRCodeView mQRCodeView;

        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_qr);

                mQRCodeView = (ZXingView) findViewById(R.id.zxingView);
                mQRCodeView.setDelegate(this);
                mQRCodeView.startSpot();
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.main_qr,menu);
                return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.scan_qrcode){
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                } if(itemId == R.id.scan_barcode){
                        mQRCodeView.changeToScanBarcodeStyle();
                }
                return super.onOptionsItemSelected(item);
        }




//    @Override
//    protected void onStart() {
//        super.onStart();
//        mQRCodeView.startCamera();
////        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
//    }
//
//    @Override
//    protected void onStop() {
//        mQRCodeView.stopCamera();
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mQRCodeView.onDestroy();
//        super.onDestroy();
//    }

        private void vibrate() {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
        }

        @Override
        public void onScanQRCodeSuccess(String result) {
//        Log.i(TAG, "result:" + result);
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QR_Activity.this, WebContentActivity.class);
                intent.putExtra("url", result);
                startActivity(intent);
                vibrate();
                mQRCodeView.startSpot();
        }

        @Override
        public void onScanQRCodeOpenCameraError() {
                Log.e(TAG, "打开相机出错");
        }

        public void onClick(View v) {
                switch (v.getId()) {
//                        case R.id.start_spot:
//                                mQRCodeView.startSpot();
//                                break;
//                        case R.id.stop_spot:
//                                mQRCodeView.stopSpot();
//                                break;
//                        case R.id.start_spot_showrect:
//                                mQRCodeView.startSpotAndShowRect();
//                                break;
//                        case R.id.stop_spot_hiddenrect:
//                                mQRCodeView.stopSpotAndHiddenRect();
//                                break;
//                        case R.id.show_rect:
//                                mQRCodeView.showScanRect();
//                                break;
//                        case R.id.hidden_rect:
//                                mQRCodeView.hiddenScanRect();
//                                break;
//                        case R.id.start_preview:
//                                mQRCodeView.startCamera();
//                                break;
//                        case R.id.stop_preview:
//                                mQRCodeView.stopCamera();
//                                break;
//                        case R.id.open_flashlight:
//                                mQRCodeView.openFlashlight();
//                                break;
//                        case R.id.close_flashlight:
//                                mQRCodeView.closeFlashlight();
//                                break;
                        case R.id.scan_barcode:
                                mQRCodeView.changeToScanBarcodeStyle();
                                break;
                        case R.id.scan_qrcode:
                                mQRCodeView.changeToScanQRCodeStyle();
                                break;
//                        case R.id.choose_qrcde_from_gallery:
//                                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//                                break;
                }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                mQRCodeView.showScanRect();

                if (requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
                        String picturePath;
                        try {
                                Uri selectedImage = data.getData();
                                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                picturePath = c.getString(columnIndex);
                                c.close();
                        } catch (Exception e) {
                                picturePath = data.getData().getPath();
                        }

                        if (new File(picturePath).exists()) {
                                QRCodeDecoder.decodeQRCode(BitmapFactory.decodeFile(picturePath), new QRCodeDecoder.Delegate() {
                                        @Override
                                        public void onDecodeQRCodeSuccess(String result) {
//                        Toast.makeText(QR_Activity.this, result, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(QR_Activity.this, WebContentActivity.class);
                                                startActivity(intent);
                                        }

                                        @Override
                                        public void onDecodeQRCodeFailure() {
                                                Toast.makeText(QR_Activity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                                        }
                                });
                        }
                }
        }
}