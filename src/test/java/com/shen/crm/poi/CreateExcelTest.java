package com.shen.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateExcelTest {
    public static void main(String[] args) throws IOException {
        HSSFWorkbook we = new HSSFWorkbook();
        HSSFSheet sheet = we.createSheet("学生列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("年龄");
        HSSFCellStyle cellStyle = we.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 1; i <= 10; i++) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(100 + i);
            cell = row.createCell(1);
            cell.setCellValue("NAME" + i);
            cell = row.createCell(2);
            cell.setCellValue(20 + i);

            FileOutputStream os = new FileOutputStream("D:\\Java\\excel\\studentList.xls");
            we.write(os);
            os.close();
            we.close();
            System.out.println("================ok===========");

        }

    }
}
