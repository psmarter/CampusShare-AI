package com.example.campus_item_sharing.post

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.campus_item_sharing.R
import com.example.campus_item_sharing.retrofit.ItemRequest
import com.example.campus_item_sharing.retrofit.ItemResponse
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class PostActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var priceInput: EditText
    private lateinit var contactName: EditText
    private lateinit var contactNumber: EditText
    private lateinit var offerRadioButton: RadioButton
    private lateinit var tagNewProducts: TextView
    private lateinit var tagBooks: TextView
    private lateinit var tagElectronics: TextView
    private lateinit var tagLifestyle: TextView
    private lateinit var uploadImage: ImageView
    private lateinit var postDescription: EditText
    private lateinit var buttonPost: Button // 提交按钮

    private var imageUploaded = false // 表示图片是否上传
    private var selectedTag: TextView? = null // 用于存储当前选中的标签

    private lateinit var imageResultLauncher: ActivityResultLauncher<Intent> // 用于图库选择
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent> // 用于相机选择

    private companion object {
        const val PERMISSIONS_REQUEST_CODE = 1001
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_item)

        initViews() // 初始化视图
        setListeners() // 设置监听器
        checkAndRequestPermissions() // 检查权限
        setupCategorySpinner() // 设置分类下拉菜单

    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_login_back)
        categorySpinner = findViewById(R.id.category_spinner)
        priceInput = findViewById(R.id.price_input)
        contactName = findViewById(R.id.contact_name)
        contactNumber = findViewById(R.id.contact_number)
        offerRadioButton = findViewById(R.id.offer_radio_button)
        tagNewProducts = findViewById(R.id.tag_all)
        tagBooks = findViewById(R.id.tag_books)
        tagElectronics = findViewById(R.id.tag_electronics)
        tagLifestyle = findViewById(R.id.tag_sports)
        uploadImage = findViewById(R.id.upload_image)
        postDescription = findViewById(R.id.post_description)
        buttonPost = findViewById(R.id.button_post)
    }

    private fun setListeners() {
        btnBack.setOnClickListener { finish() } // 返回按钮
        setTagClickListener(tagNewProducts)
        setTagClickListener(tagBooks)
        setTagClickListener(tagElectronics)
        setTagClickListener(tagLifestyle)

        uploadImage.setOnClickListener { showImageSelectionDialog() }
        buttonPost.setOnClickListener { handlePostButtonClick() }

        // 注册用于图库选择的 ActivityResultLauncher
        imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    setAvatarUri(it) // 从图库选择头像
                    imageUploaded = true // 标记图片已上传
                }
            }
        }

        // 注册相机
        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photo = result.data?.extras?.get("data") as? Bitmap
                photo?.let {
                    setAvatarBitmap(it) // 设置获取的图片
                    imageUploaded = true // 图片已上传
                }
            }
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("求购", "出售")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter // 设置下拉选项
    }

    private fun setTagClickListener(tag: TextView) {
        tag.setOnClickListener {
            selectedTag?.isSelected = false // 取消先前选定
            tag.isSelected = true // 设置当前选定
            selectedTag = tag // 更新已选定位
        }
    }

    private fun handlePostButtonClick() {
        if (checkInput()) { // 输入检查
            val itemRequest = prepareItemRequest() // 准备发布请求
            itemRequest?.let { publishItem(it) } // 调用发布物品的函数
        }
    }

    private fun prepareItemRequest(): ItemRequest? {
        val accountName = getUserData().account // 获取当前用户账号
        val itemType = categorySpinner.selectedItem.toString() // 获取类别

        // 改为使用 try-catch 处理价格
        val price = priceInput.text.toString().toDoubleOrNull()
            ?: run {
                showToast("请输入有效的价格")
                return null // 如果价格无效，返回 null
            }

        val contactName = contactName.text.toString() // 获取联系人姓名
        val contactNumber = contactNumber.text.toString() // 获取联系号码
        val tags = selectedTag?.text.toString() // 获取选择的标签
        val description = postDescription.text.toString() // 获取描述

        // 将 Bitmap 转换为 Base64 编码字符串
        val imageData = convertBitmapToBase64()
        if (imageData.isEmpty()) {
            showToast("请上传有效的图像")
            return null
        }

        return ItemRequest(
            accountName = accountName,
            itemType = itemType,
            price = price,
            contactName = contactName,
            contactNumber = contactNumber,
            tags = tags,
            imageData = imageData, // 添加图像数据
            description = description
        )
    }

    // 将 Bitmap 转换为 Base64 字符串的方法
    private fun convertBitmapToBase64(): String {
        val drawable = uploadImage.drawable
        if (drawable != null) {
            val bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap // 处理 BitmapDrawable
                is VectorDrawableCompat -> {
                    // 将 VectorDrawable 转换为 Bitmap
                    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height) // 设置边界大小
                    drawable.draw(canvas) // 绘制到 Canvas
                    bitmap
                }
                is VectorDrawable -> {
                    // 对于 Android API 21 及以上版本的 VectorDrawable
                    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height) // 设置边界大小
                    drawable.draw(canvas) // 绘制到 Canvas
                    bitmap
                }
                else -> null // 返回 null 如果不支持的类型
            }

            bitmap?.let {
                return ByteArrayOutputStream().use { stream ->
                    // 压缩 Bitmap 为 PNG 格式并转换为 ByteArray
                    it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    return Base64.encodeToString(byteArray, Base64.DEFAULT) // 转换为 Base64 字符串
                }
            }
        }
        return "" // 如果没有图像，返回空字符串
    }

    private fun publishItem(itemRequest: ItemRequest) {
        RetrofitClient.apiService.addItem(itemRequest).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.status == "success") {
                            showToast("物品发布成功！")
                            finish() // 关闭当前活动
                        } else {
                            showToast(it.message ?: "发布失败，请重试.")
                        }
                    } ?: showToast("发布失败：响应为空")
                } else {
                    showToast("发布失败：${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                showToast("网络错误：${t.message}")
            }
        })
    }

    // 获取 SharedPreferences
    private fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    // 读取用户信息的方法
    private fun getUserData(): UserDetails {
        val sharedPreferences = getSharedPreferences()
        val studentNumber = sharedPreferences.getString("studentNumber", null)
        val account = sharedPreferences.getString("account", null)
        val passwordHash = sharedPreferences.getString("passwordHash", null)
        val email = sharedPreferences.getString("email", null)

        // 处理空值的情况
        return UserDetails(
            studentNumber ?: "未设置",
            account ?: "未设置",
            passwordHash ?: "未设置",
            email ?: "未设置"
        )
    }

    private fun checkInput(): Boolean{
        // 检查输入有效性
        val priceValid = priceInput.text.isNotEmpty()
        val contactValid = contactName.text.isNotEmpty() && contactNumber.text.isNotEmpty()
        val tagValid = selectedTag != null
        val descriptionValid = postDescription.text.isNotEmpty()

        if (!priceValid) {
            showToast("价格必须填写，面议请填写0")
            return false
        }
        if (!contactValid) {
            showToast("请填写联系人姓名和电话号码")
            return false
        }
        if (!tagValid) {
            showToast("请至少选择一个标签")
            return false
        }
        if (!imageUploaded) {
            showToast("请上传图片")
            return false
        }
        if (!descriptionValid) {
            showToast("请填写描述")
            return false
        }
        return true
    }

    // 显示选择图片对话框
    private fun showImageSelectionDialog() {
        val options = arrayOf("书籍", "电子设备", "体育", "拍照", "从图库选择")
        AlertDialog.Builder(this)
            .setTitle("选择上传图片")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        setAvatar(R.drawable.ic__post_book)
                        imageUploaded = true // 标记图片已上传
                    }
                    1 -> {
                        setAvatar(R.drawable.ic_post_digital)
                        imageUploaded = true // 标记图片已上传
                    }
                    2 -> {
                        setAvatar(R.drawable.ic_post_sport)
                        imageUploaded = true // 标记图片已上传
                    }
                    3 -> openCameraForImage() // 从相机拍照
                    4 -> openGalleryForImage() // 从图库选择
                }
            }
            .show()
    }

    // 从图库选择图片
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*" // 设置选择类型为图片
        }
        imageResultLauncher.launch(intent)
    }

    // 从相机拍照
    private fun openCameraForImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher.launch(intent) // 启动相机
    }

    // 根据 URI 设置图片
    private fun setAvatarUri(uri: Uri) {
        uploadImage.setImageURI(uri) // 更新上传的图片
    }

    // 根据 Bitmap 设置图片
    private fun setAvatarBitmap(bitmap: Bitmap) {
        uploadImage.setImageBitmap(bitmap) // 更新上传的图片
    }

    private fun setAvatar(resId: Int) {
        uploadImage.setImageResource(resId)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // 检查相机和媒体权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
        }

        // 请求权限
        if (permissionsToRequest.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("请求权限")
                .setMessage("为了能够上传图像，我们需要访问您的媒体文件和相机。")
                .setPositiveButton("确定") { _, _ ->
                    ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSIONS_REQUEST_CODE)
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                for (i in permissions.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        showToast("${permissions[i]} 权限被拒绝，请在设置中开启")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}