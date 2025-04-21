package com.example.campus_item_sharing.function

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.campus_item_sharing.databinding.ActivityScanBinding
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var cameraExecutor: ExecutorService
    private var hasScanned = false // 防止多次跳转

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startCamera()
        else {
            Toast.makeText(this, "需要摄像头权限才能扫码", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scanner = BarcodeScanning.getClient()
            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                processImage(scanner, imageProxy)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImage(scanner: com.google.mlkit.vision.barcode.BarcodeScanner, imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if (!hasScanned) {
                        hasScanned = true
                        barcode.rawValue?.let {
//                            val intent = Intent(this, WebActivity::class.java)
//                            intent.putExtra("web_url", it)
//                            startActivity(intent)
//                            finish()
                            val uri = Uri.parse(it)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            intent.setPackage("com.tencent.mm") // 强制用微信打开
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "未安装微信，无法打开链接", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }
                    }
                }
                imageProxy.close()
            }
            .addOnFailureListener { imageProxy.close() }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}






//class ScanActivity : AppCompatActivity() {
//
//    private lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
//            if (result.contents != null) {
//                val intent = Intent(this, WebActivity::class.java)
//                intent.putExtra("web_url", result.contents)
//                startActivity(intent)
//            }
//            finish()
//        }
//
//        val options = ScanOptions().apply {
//            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
//            setPrompt("请将二维码放入框中")
//            setCameraId(0)
//            setBeepEnabled(true)
//            setBarcodeImageEnabled(true)
//        }
//
//        barcodeLauncher.launch(options)
//    }
//}