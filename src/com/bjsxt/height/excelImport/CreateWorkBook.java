package com.bjsxt.height.excelImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

import org.apache.poi.ss.usermodel.AutoFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bjsxt.height.ForkJoin.ForkJoinTest2;

public class CreateWorkBook {
	public static void main(String[] args) throws Exception {
		// Create Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create file system using specific name
		FileOutputStream out = new FileOutputStream(new File("createworkbook.xlsx"));
		// write operation workbook using file out object
		workbook.write(out);
		out.close();
		System.out.println("createworkbook.xlsx written successfully");
	}
}

class OpenWorkBook {
	public static void main(String args[]) throws Exception {
		File file = new File("createworkbook.xlsx");
		FileInputStream fIP = new FileInputStream(file);
		// Get the workbook instance for XLSX file
		XSSFWorkbook workbook = new XSSFWorkbook(fIP);
		if (file.isFile() && file.exists()) {
			System.out.println("openworkbook.xlsx file open successfully.");
		} else {
			System.out.println("Error to open openworkbook.xlsx file.");
		}
	}
}

class Writesheet {
	public static void main(String[] args) throws Exception {
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet(" Employee Info ");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new TreeMap<String, Object[]>();
		empinfo.put("1", new Object[] { "EMP ID", "EMP NAME", "DESIGNATION" });
		empinfo.put("2", new Object[] { "tp01", "Gopal", "Technical Manager" });
		empinfo.put("3", new Object[] { "tp02", "Manisha", "Proof Reader" });
		empinfo.put("4", new Object[] { "tp03", "Masthan", "Technical Writer" });
		empinfo.put("5", new Object[] { "tp04", "Satish", "Technical Writer" });
		empinfo.put("6", new Object[] { "tp05", "Krishna", "Technical Writer" });
		// Iterate over data and write to sheet
		Set<String> keyid = empinfo.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = empinfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) obj);
			}
		}
		// Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File("Writesheet.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("Writesheet.xlsx written successfully");
	}
}

class Readsheet {
	static XSSFRow row;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		FileInputStream fis = new FileInputStream(new File("WriteSheet.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// The supplied data appears to be in the Office 2007+ XML. You are calling the
		// part of POI that deals with OLE2 Office Documents. You need to call a
		// different part of POI to process this data (eg XSSF instead of HSSF)
		// 使用HSSFSheet导入.xlsx 会报错
		XSSFSheet spreadsheet = workbook.getSheetAt(0);
		spreadsheet.removeRow(spreadsheet.getRow(0));//删除表头
		int physicalNum=spreadsheet.getPhysicalNumberOfRows();
		Iterator<Row> rowIterator = spreadsheet.iterator();
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			// POI设置EXCEL单元格格式为文本、小数、百分比、货币、日期、科学计数法和中文大写
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:// 代表空白单元格
					break;
				case Cell.CELL_TYPE_NUMERIC:

					
					System.out.print(cell.getNumericCellValue() + " \t\t ");
					break;
				case Cell.CELL_TYPE_STRING:
					System.out.print(cell.getStringCellValue() + " \t\t ");
					break;
				}
			}
			System.out.println();
		}
		fis.close();
		long resultTime = System.currentTimeMillis() - startTime;
		System.out.println(resultTime);
	}

	private static String checkDate(Cell cell) {
		if(cell.getCellType() ==cell.CELL_TYPE_STRING) {
			String str =cell.getStringCellValue();
			if(str.length()>7) {
				//070909
				str = str.substring(0, 7);
			}
		}
		return null;
	}
	private static String checkStringAndNumber(Cell cell) {
		DecimalFormat df = new DecimalFormat("0");
		return cell.getCellType() == cell.CELL_TYPE_STRING ? cell.getStringCellValue()
				: df.format(cell.getNumericCellValue());
	}
}

class TypesofCells {
	public static void main(String[] args) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");
		XSSFRow row = spreadsheet.createRow((short) 2);
		row.createCell(0).setCellValue("Type of Cell");
		row.createCell(1).setCellValue("cell value");
		row = spreadsheet.createRow((short) 3);
		row.createCell(0).setCellValue("set cell type BLANK");
		row.createCell(1);
		row = spreadsheet.createRow((short) 4);
		row.createCell(0).setCellValue("set cell type BOOLEAN");
		row.createCell(1).setCellValue(true);
		row = spreadsheet.createRow((short) 5);
		row.createCell(0).setCellValue("set cell type ERROR");
		row.createCell(1).setCellValue(XSSFCell.CELL_TYPE_ERROR);
		row = spreadsheet.createRow((short) 6);
		row.createCell(0).setCellValue("set cell type date");
		row.createCell(1).setCellValue(new Date());
		row = spreadsheet.createRow((short) 7);
		row.createCell(0).setCellValue("set cell type numeric");
		row.createCell(1).setCellValue(20);
		row = spreadsheet.createRow((short) 8);
		row.createCell(0).setCellValue("set cell type string");
		row.createCell(1).setCellValue("A String");
		FileOutputStream out = new FileOutputStream(new File("typesofcells.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("typesofcells.xlsx written successfully");
	}
}


