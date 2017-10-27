package com.xiaofei_dev.github.simpleimage.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import com.xiaofei_dev.github.simpleimage.R
import com.xiaofei_dev.github.simpleimage.extension.getViewBitmap
import com.xiaofei_dev.github.simpleimage.extension.openLink
import com.xiaofei_dev.github.simpleimage.extension.requestPermission
import com.xiaofei_dev.github.simpleimage.extension.saveImageToDir
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.yesButton
import java.io.File


class MainActivity : AppCompatActivity() {
    companion object {
        val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        }
        init()
    }

    //权限请求回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE_READ_ACCESS_PERMISSION ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else -> requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        }
    }

    private fun init(){
        for (m in 0..(rootView.childCount - 1)){
            val lineatLayout = rootView.getChildAt(m) as ViewGroup
            for (n in 0..(lineatLayout.childCount - 1)){
                lineatLayout.getChildAt(n).onClick { v ->
                    val dialog = AlertDialog.Builder(this@MainActivity,R.style.Dialog)
                            .setPositiveButton("发送") { dialog, which ->
                                val path = saveImageToDir(getViewBitmap(v!!)!!,"SimpleImage")
                                if (path != null){
                                    val imageUri = Uri.fromFile(File(path))
                                    val shareIntent = Intent()
                                    shareIntent.action = Intent.ACTION_SEND
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                                    shareIntent.type = "image/*"
                                    startActivity(Intent.createChooser(shareIntent, "分享到"))
                                }
                            }
                            .setNegativeButton("保存") {dialog, which ->
                                saveImageToDir(getViewBitmap(v!!)!!,"SimpleImage")
                            }
                            .create()
                    val layout = layoutInflater.inflate(R.layout.dialog_image,null) as ViewGroup
                    val imageView = layout.find<ImageView>(R.id.image)
                    var bitmap = getViewBitmap(v!!)
//                    bitmap = Bitmap.createScaledBitmap(bitmap,1242,1242,true)
                    imageView.setImageBitmap(bitmap)
                    dialog.setView(layout)
                    dialog.show()
                    setDialogWindowAttr(dialog,this@MainActivity)
                }
            }
        }
    }

    fun setDialogWindowAttr(dlg: Dialog, ctx: Context) {
        val window = dlg.getWindow()
        val lp = window.getAttributes()
        lp.gravity = Gravity.CENTER
        lp.width = dip(300)//宽高可设置具体大小
        lp.height = dip(300)
        dlg.getWindow().setAttributes(lp)
    }

    //加载菜单资源
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
//        return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.link ->{
                openLink("https://www.coolapk.com",true)
            }
            R.id.link1 ->{
                openLink("https://www.coolapk.com",true)
            }
            R.id.about ->{
                alert("关于关于关于关于关于关于关于关于关于关于","关于") {
                    yesButton { }
                }.show()
            }
            R.id.finish ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
