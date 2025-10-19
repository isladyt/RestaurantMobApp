package com.example.restaurant.repository

import android.content.Context
import android.os.Environment
import com.example.restaurant.data.dao.OrderDao
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReportRepository @Inject constructor(
    private val orderDao: OrderDao,
    @ApplicationContext private val context: Context
) {

    suspend fun generateReport(): Result<String> {
        return try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val startTime = calendar.timeInMillis
            
            val orders = orderDao.getTodaysOrders(startTime)

            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Orders Report")

            // Header row
            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Order ID")
            headerRow.createCell(1).setCellValue("User ID")
            headerRow.createCell(2).setCellValue("Status")
            headerRow.createCell(3).setCellValue("Payment Method")
            headerRow.createCell(4).setCellValue("Total Amount")
            headerRow.createCell(5).setCellValue("Created At")

            // Data rows
            orders.forEachIndexed { index, order ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(order.id.toDouble())
                row.createCell(1).setCellValue(order.user_id.toDouble())
                row.createCell(2).setCellValue(order.status_order)
                row.createCell(3).setCellValue(order.payment_method)
                row.createCell(4).setCellValue(order.total_amount)
                row.createCell(5).setCellValue(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(order.created_at)))
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Order_Report_$timeStamp.xlsx"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()

            Result.success(file.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
