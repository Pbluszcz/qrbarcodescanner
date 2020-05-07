package com.example.zxing

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity(),ZXingScannerView.ResultHandler {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 102
    }

    lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForPermission()
    }


    private fun initializeQRCamera(context: Context) {
        scannerView = ZXingScannerView(context)
        scannerView.setResultHandler(this)
        scannerView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        scannerView.setBorderColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        scannerView.setLaserColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        scannerView.setBorderStrokeWidth(10)
        scannerView.setSquareViewFinder(true)
        scannerView.setAutoFocus(true)
        startQRCamera()
        containerScanner.addView(scannerView)
    }

    private fun startQRCamera() {
        scannerView.startCamera()
    }


    private fun checkForPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initializeQRCamera(this)
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeQRCamera(this)
            } else
                requestPermission()
        }
    }

    override fun handleResult(rawResult: Result?) {
        onQrResult(rawResult?.text)
        scannerView.stopCamera()
        scannerView.startCamera()
    }

    private fun onQrResult(contents: String?) {
        if (contents.isNullOrEmpty())
            Toast.makeText(applicationContext, "null", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(applicationContext, contents, Toast.LENGTH_LONG).show()

        scannerView.resumeCameraPreview(this);
    }


}
