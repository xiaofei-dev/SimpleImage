package com.xiaofei_dev.github.simpleimage.extension

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Toast
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream

/**
 * Created by xiaofei on 2017/10/26.
 */
fun Activity.openLink(link: String, isThrowException: Boolean) {
    val intent = Intent(Intent.ACTION_VIEW)

    try {
        intent.data = Uri.parse(link)
        startActivity(intent)
    } catch (e: Exception) {
        if (isThrowException) {
            throw e
        } else {
            e.printStackTrace()
            Toast.makeText(this, "打开失败", Toast.LENGTH_SHORT).show()
        }
    }
}

//给 Activity 扩展的方法，获取普通 View 截图
fun Activity.getViewBitmap(view: View): Bitmap? {
    val drawingCacheEnabled = true
    view.setDrawingCacheEnabled(drawingCacheEnabled)
    view.buildDrawingCache(drawingCacheEnabled)
    val drawingCache = view.getDrawingCache()
    val bitmap: Bitmap?
    if (drawingCache != null) {
        bitmap = Bitmap.createBitmap(drawingCache)
        view.setDrawingCacheEnabled(false)
    } else {
        bitmap = null
    }
   /* val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
    val cvs = Canvas(bitmap)
    view.draw(cvs)*/
    /*view.setDrawingCacheEnabled(true)
    val bitmap = Bitmap.createBitmap(view.getDrawingCache())
    view.setDrawingCacheEnabled(false)*/
    return bitmap
}

//给 Activity 扩展的方法，保存图片到 SD 卡指定名称的文件夹
fun Activity.saveImageToDir(bmp: Bitmap,dir:String):String?{
    var save:Boolean = false
    val appDir: File = File(Environment.getExternalStorageDirectory(), dir)
    if(!appDir.exists()){
        appDir.mkdir()
    }
    val fileName:String = System.currentTimeMillis().toString() + ".png"
    val file: File = File(appDir, fileName)
    try {
        val fos: FileOutputStream = FileOutputStream(file)
        save = bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
        fos.flush()
        fos.close()
    }catch (e: java.lang.Exception){
        save = false
        e.printStackTrace()
        toast(e.toString())
    }
    if(save){
        //通知图库更新
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.path)))
//        toast(R.string.save_success)
        toast("图片已成功保存至\n ${file.path}")
    }else{
        toast("图片保存失败")
    }
    if (save){
        return file.path
    }else{
        return null
    }
}

fun Activity.saveImageToDirCache(bmp: Bitmap):File?{
    var save:Boolean = false
    val fileName:String = System.currentTimeMillis().toString() + ".png"
    val file: File = File(cacheDir, fileName)
    try {
        val fos: FileOutputStream = FileOutputStream(file)
        save = bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
        fos.flush()
        fos.close()
    }catch (e: java.lang.Exception){
        save = false
        e.printStackTrace()
    }
    if(save){
        return file
    }else{
        return null
    }
}

//请求权限
fun Activity.requestPermission(permission: Array<String>,requestCode: Int) {
    ActivityCompat.requestPermissions(this, permission, requestCode)
}
