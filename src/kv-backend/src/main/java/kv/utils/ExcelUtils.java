package kv.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class ExcelUtils {

    public static XSSFWorkbook getWorkBook(MultipartFile multipartFile) {
        XSSFWorkbook workbook = null;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;
    }

    public static int getColIdx(String colStr) {
        int number = 0;
        int multiple = 1;
        for (int i = colStr.length() - 1; i >= 0; i--) {
            int k = colStr.charAt(i) - 'A' + 1;
            number += k * multiple;
            multiple *= 26;
        }
        return number - 1;
    }

    public static XSSFCell getCell(XSSFSheet sheet, String address) {
        if (address == null || address.length() < 2) {
            return null;
        }

        address = address.toUpperCase();
        // get column and row
        int idx = 0;
        while (idx < address.length() && address.charAt(idx) >= 'A' && address.charAt(idx) <= 'Z') {
            idx++;
        }
        String colStr = address.substring(0, idx);
        String rowStr = address.substring(idx);

        // get index of column and row
        int colIdx = getColIdx(colStr);
        int rowIdx = Integer.parseInt(rowStr) - 1;

        // get cell
        XSSFRow row = sheet.getRow(rowIdx);
        if (row != null) {
            return row.getCell(colIdx);
        } else {
            return null;
        }
    }

    public static boolean isCellEmpty(XSSFCell cell) {
        return cell == null || cell.getCellTypeEnum() == CellType.BLANK;
    }

    public static boolean isCellNumeric(XSSFCell cell) {
        return cell != null && cell.getCellTypeEnum() == CellType.NUMERIC;
    }

    public static Date getJavaDate(XSSFCell cell) {
        return HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
    }

}
