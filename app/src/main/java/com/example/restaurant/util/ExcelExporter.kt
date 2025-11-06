package com.example.restaurant.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.example.restaurant.R
import com.example.restaurant.data.entity.Order
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    private const val NOTIFICATION_CHANNEL_ID = "excel_export_channel"

    fun exportOrdersToExcel(context: Context, orders: List<Order>, fileName: String): Result<String> {
        return try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Отчет по заказам")

            // Заголовки
            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("ID Заказа")
            headerRow.createCell(1).setCellValue("ID Пользователя")
            headerRow.createCell(2).setCellValue("Дата")
            headerRow.createCell(3).setCellValue("Статус")
            headerRow.createCell(4).setCellValue("Способ оплаты")
            headerRow.createCell(5).setCellValue("Сумма")

            // Данные
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            orders.forEachIndexed { index, order ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(order.id.toDouble())
                row.createCell(1).setCellValue(order.user_id.toDouble())
                row.createCell(2).setCellValue(dateFormat.format(Date(order.created_at)))
                row.createCell(3).setCellValue(order.status_order)
                row.createCell(4).setCellValue(order.payment_method)
                row.createCell(5).setCellValue(order.total_amount)
            }

            // Сохранение
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val file = File(downloadsDir, fileName)
            val fileOut = FileOutputStream(file)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()

            // Показываем уведомление
            showExportNotification(context, file)

            Result.success("Отчет успешно сохранен: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun showExportNotification(context: Context, file: File) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, 
                "Экспорт в Excel", 
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val fileUri: Uri = FileProvider.getUriForFile(
            context, 
            context.applicationContext.packageName + ".provider", 
            file
        )

        val openIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // ИСПРАВЛЕНО
            .setContentTitle("Отчет сохранен")
            .setContentText("Нажмите, чтобы открыть файл: ${file.name}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
