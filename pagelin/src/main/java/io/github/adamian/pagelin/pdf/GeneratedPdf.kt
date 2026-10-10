package io.github.adamian.pagelin.pdf

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import java.io.File

class GeneratedPdf internal constructor(
    internal val file: File
) {

    private fun getUri(
        activity: ComponentActivity
    ): Uri =
        FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.pagelin.provider",
            file
        )

    fun preview(activity: ComponentActivity) {
        val uri = getUri(activity)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        activity.startActivity(intent)
    }

    fun share(activity: ComponentActivity) {
        val uri = getUri(activity)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        activity.startActivity(
            Intent.createChooser(intent, "Share PDF")
        )
    }

    fun saveToDownloads(
        activity: ComponentActivity
    ): Uri? {

        val resolver = activity.contentResolver

        val contentValues = ContentValues().apply {
            put(
                MediaStore.Downloads.DISPLAY_NAME,
                file.name
            )
            put(
                MediaStore.Downloads.MIME_TYPE,
                "application/pdf"
            )
        }

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return null

        resolver.openOutputStream(uri)?.use { output ->
            file.inputStream().use { input ->
                input.copyTo(output)
            }
        }

        return uri
    }
}