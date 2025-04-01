package com.example.campus_item_sharing.post

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.campus_item_sharing.R
import com.example.campus_item_sharing.retrofit.ItemDetails
import com.example.campus_item_sharing.retrofit.ResponseModel
import com.example.campus_item_sharing.retrofit.RetrofitClient
import com.example.campus_item_sharing.retrofit.UserDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostInfoActivity : AppCompatActivity(){
    private lateinit var btnBack: ImageView
    private lateinit var tagAll: TextView
    private lateinit var tagBooks: TextView
    private lateinit var tagElectronics: TextView
    private lateinit var tagSports: TextView
    private lateinit var addMyFriendButton: Button // 添加好友按钮

    private lateinit var priceInput: EditText
    private lateinit var accountName: EditText
    private lateinit var contactName: EditText
    private lateinit var contactNumber: EditText
    private lateinit var categorySpinner: EditText // Assuming it's an EditText
    private lateinit var postDescription: EditText

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_item_info)

        // 从 SharedPreferences 获取保存的数据
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)

        val description = sharedPreferences.getString("description", "默认描述")
        val accountNameText = sharedPreferences.getString("accountName", "默认账户名")
        val itemType = sharedPreferences.getString("itemType", "默认物品类型")
        val price = sharedPreferences.getString("price", "0.0")
        val contactNameText = sharedPreferences.getString("contactName", "默认联系人姓名")
        val contactNumberText = sharedPreferences.getString("contactNumber", "默认联系人号码")
        val tags = sharedPreferences.getString("tags", "")
        val imageData = sharedPreferences.getString("imageData", "")
        val imageUniqueId = sharedPreferences.getString("imageUniqueId", "默认ID")


        btnBack = findViewById(R.id.btn_login_back)
        addMyFriendButton = findViewById(R.id.add_my_friend)

        // 使 EditText 不可编辑
        accountName = findViewById<EditText>(R.id.account_name).apply {
            setText(accountNameText)
            isEnabled = false // 将 EditText 设置为不可编辑
        }
        categorySpinner = findViewById<EditText>(R.id.category_spinner).apply {
            setText(itemType)
            isEnabled = false // 将 EditText 设置为不可编辑
        }
        priceInput = findViewById<EditText>(R.id.price_input).apply {
            setText(price)
            isEnabled = false // 将 EditText 设置为不可编辑
        }
        contactName = findViewById<EditText>(R.id.contact_name).apply {
            setText(contactNameText)
            isEnabled = false // 将 EditText 设置为不可编辑
        }
        contactNumber = findViewById<EditText>(R.id.contact_number).apply {
            setText(contactNumberText)
            isEnabled = false // 将 EditText 设置为不可编辑
        }
        postDescription = findViewById<EditText>(R.id.post_description).apply {
            setText(description)
            isEnabled = false // 将 EditText 设置为不可编辑
        }

        tagAll = findViewById(R.id.tag_all)
        tagBooks = findViewById(R.id.tag_books)
        tagElectronics = findViewById(R.id.tag_electronics)
        tagSports = findViewById(R.id.tag_sports)

        btnBack.setOnClickListener { finish() }

        // 将 imageData 解码为 Bitmap，并设置到 ImageView
        val imageView: ImageView = findViewById(R.id.upload_image)
        if (!imageData.isNullOrEmpty()) {
            val bitmap = decodeBase64ToBitmap(imageData)
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap) // 将 Bitmap 设置到 ImageView
            } else {
                imageView.setImageResource(R.drawable.ic_avatar) // 使用默认图片
            }
        } else {
            imageView.setImageResource(R.drawable.ic_avatar) // 使用默认图片
        }

        highlightSelectedTag(tags)

        // 保存新项目数据
        if (price != null) {
            saveItemData(sharedPreferences, ItemDetails(accountNameText.toString(), itemType.toString(), price.toDouble(), contactNameText.toString(), contactNumberText.toString(), tags.toString(), imageData.toString(), imageUniqueId.toString(), description.toString()))
        }

        // 点击添加好友按钮的事件处理
        addMyFriendButton.setOnClickListener {
            if (accountNameText.isNullOrEmpty()) {
                Toast.makeText(this, "目标好友账户名为空", Toast.LENGTH_SHORT).show()
            } else {
                saveFriendData(getUserData().account, accountNameText)
            }
        }
    }

    private fun saveFriendData(accountName: String?, friendAccount: String?) {
        // 判断账户名是否为空
        if (accountName.isNullOrEmpty() || friendAccount.isNullOrEmpty()) {
            Toast.makeText(this, "账户名不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        // 调用 Retrofit 接口，发起添加好友请求
        RetrofitClient.apiService.addFriend(accountName, friendAccount)
            .enqueue(object : retrofit2.Callback<ResponseModel> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseModel>,
                    response: retrofit2.Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val resp = response.body()
                        if (resp != null && resp.status == "success") {
                            Toast.makeText(this@PostInfoActivity, "好友添加成功", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this@PostInfoActivity,
                                resp?.message ?: "添加好友失败",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@PostInfoActivity,
                            "添加好友失败：" + response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(
                        this@PostInfoActivity,
                        "网络错误：" + t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    @SuppressLint("MutatingSharedPrefs")
    private fun saveItemData(sharedPreferences: SharedPreferences, newItem: ItemDetails) {
        val editor = sharedPreferences.edit()

        // 读取现有的项目列表
        val existingItemsJson = sharedPreferences.getString("items", "[]") ?: "[]"
        val itemListType = object : TypeToken<MutableList<ItemDetails>>() {}.type
        val itemList: MutableList<ItemDetails> = gson.fromJson(existingItemsJson, itemListType)

        // 检查是否存在重复的唯一ID
        val existingItem = itemList.find { it.imageUniqueId == newItem.imageUniqueId }
        if (existingItem != null) {
            // 如果存在，根据唯一ID移除
            itemList.remove(existingItem)
        }

        // 保存新项
        itemList.add(newItem)
        val updatedItemsJson = gson.toJson(itemList)
        editor.putString("items", updatedItemsJson) // 更新项目列表
        editor.apply() // 提交更改
    }

    // 将 Base64 字符串解码为 Bitmap 的辅助方法
    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun highlightSelectedTag(tag: String?) {
        when (tag) {
            "全部" -> highlightTag(tagAll)
            "书籍" -> highlightTag(tagBooks)
            "电子设备" -> highlightTag(tagElectronics)
            "体育" -> highlightTag(tagSports)
        }
    }

    private fun highlightTag(tag: TextView) {
        tag.isSelected = true // Set the tag as selected
        tag.setBackgroundResource(R.drawable.tag_background) // Use your selector drawable
        tag.setTextColor(ContextCompat.getColor(this, R.color.white)) // Change text color for visibility
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    // 获取 SharedPreferences
    private fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
}