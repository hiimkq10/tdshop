package com.hcmute.tdshop.utils.exporter;

import com.hcmute.tdshop.enums.ProductUserInteractExcel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductUserInteractExcelExporter {

  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<ProductUserInteractExcel> listDatas;
  final String excelFilePath = "src/main/resources/data/aidata.xlsx";

  public ProductUserInteractExcelExporter(List<ProductUserInteractExcel> listDatas) {
    this.listDatas = listDatas;
    workbook = new XSSFWorkbook();
  }


  private void writeHeaderLine() {
    sheet = workbook.createSheet("ProductUserInteractData");

    Row row = sheet.createRow(0);

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(15);
    style.setFont(font);

    createCell(row, 0, "order_id", style);
    createCell(row, 1, "user_id", style);
    createCell(row, 2, "product_id", style);
    createCell(row, 3, "bought", style);
    createCell(row, 4, "rating", style);
    createCell(row, 5, "category_ids", style);
  }

  private void createCell(Row row, int columnCount, Object value, CellStyle style) {
    sheet.autoSizeColumn(columnCount);
    Cell cell = row.createCell(columnCount);
    if (value instanceof Integer) {
      cell.setCellValue((Integer) value);
    } else if (value instanceof Boolean) {
      cell.setCellValue((Boolean) value);
    } else if (value instanceof Long) {
      cell.setCellValue((Long) value);
    } else {
      cell.setCellValue((String) value);
    }
    cell.setCellStyle(style);
  }

  private void writeDataLines() {
    int rowCount = 1;

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontHeight(13);
    style.setFont(font);

    for (ProductUserInteractExcel puie : listDatas) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;

      createCell(row, columnCount++, puie.getOrderId(), style);
      createCell(row, columnCount++, puie.getUserId(), style);
      createCell(row, columnCount++, puie.getProductId(), style);
      createCell(row, columnCount++, puie.getBought(), style);
      createCell(row, columnCount++, puie.getRating(), style);
      createCell(row, columnCount++, puie.getCategoryIds(), style);
    }
  }

  public void export(String fileName) throws IOException {
    writeHeaderLine();
    writeDataLines();

//    ServletOutputStream outputStream = response.getOutputStream();
//    workbook.write(outputStream);
//    workbook.close();
//
//    outputStream.close();
    OutputStream os = new FileOutputStream(fileName);
    workbook.write(os);

  }
}
