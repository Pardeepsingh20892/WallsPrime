package com.wallsprime.wallpapers.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat


fun shareOrSetWallpaper(savedImgUri: Uri?, context: Context, code: Int) {


    when (code) {

        2 -> {

            val intent = Intent(Intent.ACTION_ATTACH_DATA)
                .apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    setDataAndType(savedImgUri, "image/*")
                    putExtra("mimeType", "image/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            ContextCompat.startActivity(context, Intent.createChooser(intent, "Set as:"), null)
        }

        3 -> {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, savedImgUri)
                type = "image/jpeg"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            ContextCompat.startActivity(context, Intent.createChooser(shareIntent, "Share with"), null)
        }


    }


}
