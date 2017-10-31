package com.bjsxt.height.excelImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
public class ForkJoinReadsheet extends RecursiveTask<List<Row>> {
	private final static ForkJoinPool forkJoinPool = new ForkJoinPool();

	private XSSFRow ros;
	private int row;
	private int threshold;// 阈值
	private XSSFSheet sheet;// 待拆分list
	private XSSFRow tableHead;//表头

	public ForkJoinReadsheet(int threshold, XSSFWorkbook wb, boolean first) {
		this.threshold = threshold;
		XSSFSheet sheet = wb.getSheetAt(0);
		
		if (first) {
			tableHead = sheet.getRow(0);
			sheet.removeRow(sheet.getRow(0));// 直接删除第一行数据
		}
		this.sheet = sheet;
		row = sheet.getPhysicalNumberOfRows();
		// row = sheet.getLastRowNum();1048575
	}
	public ForkJoinReadsheet(){
		super();
	}		
	@Override
	protected List<Row> compute() {
		System.out.println("当前线程===>" + Thread.currentThread().getName());
		List<Row> list = new ArrayList<Row>();
		if (row < threshold) {
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				ros = (XSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = ros.cellIterator();
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
						if ("tp07".equals(cell.getStringCellValue())  || 
								"tp23".equals(cell.getStringCellValue()) ||
								"tp99".equals(cell.getStringCellValue())) {
							String str = "空" + cell.getStringCellValue();
							System.out.print(str);
							//list.add(str);*/
							list.add(ros);
						}
						System.out.print(cell.getStringCellValue() + " \t\t ");
						break;
					}
				}
				System.out.println();

			}
		} else {
			// 如果当end与start之间的差大于阈值时,将大任务分解成两个小任务。
			int middles = row / 2;
			XSSFWorkbook leftli = subSheet(0, middles, sheet);

			XSSFWorkbook rightli = subSheet(middles, row, sheet);

			ForkJoinReadsheet left = new ForkJoinReadsheet(threshold, leftli, false);

			ForkJoinReadsheet right = new ForkJoinReadsheet(threshold, rightli, false);
			left.fork();
			right.fork();
			list.addAll(left.join());
			list.addAll(right.join());
		}
		return list;
	}

	/**
	 * 插入一个空表
	 * 
	 * @param srcStartRow
	 * @param srcEndRow
	 * @param sheet
	 * @return
	 */
	private XSSFWorkbook subSheet(int srcStartRow, int srcEndRow, final XSSFSheet sheet) {

		XSSFWorkbook workbookTemp = new XSSFWorkbook();
		XSSFSheet targetSheet = workbookTemp.createSheet();
		// int len =srcEndRow -srcStartRow;
		int rowid = 0;
		for (int i = srcStartRow; i < srcEndRow; i++) {
			XSSFRow sheetRow = sheet.getRow(i);
			if (sheetRow == null) {
				continue;
			}
			rowid++;
			subRow(sheetRow, targetSheet, rowid);
		}
		return workbookTemp;
	}

	/**
	 * 插入一行数据
	 * 
	 * @param sheetRow
	 * @param targetSheet
	 * @param rowid
	 */
	private void subRow(XSSFRow sheetRow, XSSFSheet targetSheet, int rowid) {
		XSSFRow targerRow = (XSSFRow) targetSheet.createRow(rowid);// 加一行
		Iterator<Cell> cellIterator = sheetRow.cellIterator();
		int cellid = 0;
		while (cellIterator.hasNext()) {
			Cell sheetcell = cellIterator.next();// 获取原表cell
			Cell targercell = targerRow.createCell(cellid++);// 新表存入位置
			subCell(sheetcell, targercell);
		}
	}

	/**
	 * 插入一个新单元格
	 * 
	 * @param sheetcell
	 * @param targercell
	 */
	private void subCell(Cell sheetcell, Cell targercell) {
		
		int cType = sheetcell.getCellType();
		targercell.setCellType(cType);
		switch (cType) {

		case XSSFCell.CELL_TYPE_BOOLEAN: targercell.setCellValue(sheetcell.getBooleanCellValue()); break;
		case XSSFCell.CELL_TYPE_ERROR: targercell.setCellValue(sheetcell.getErrorCellValue()); break;
		case XSSFCell.CELL_TYPE_FORMULA: targercell.setCellValue(sheetcell.getCellFormula()); break;
		case XSSFCell.CELL_TYPE_NUMERIC: targercell.setCellValue(sheetcell.getNumericCellValue()); break;
		case XSSFCell.CELL_TYPE_STRING: targercell.setCellValue(sheetcell.getRichStringCellValue()); break;
		case XSSFCell.CELL_TYPE_BLANK:
		default: targercell.setCellValue(""); break;
		}
	}
	@SuppressWarnings({ "unused", "resource" })
	private void writeOut(List<Row> row) throws IOException{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell error");
		int rowid =0;
		for (Iterator<Row> iterator = row.iterator(); iterator.hasNext();) {
			XSSFRow row2 = (XSSFRow) iterator.next();
			if(rowid == 0) subRow(tableHead,spreadsheet,rowid++);
			subRow(row2, spreadsheet, rowid++);//添加数据
		}
		FileOutputStream out = null;

		out = new FileOutputStream(new File("error.xlsx"));

		workbook.write(out);

		out.close();

	}
	public static void main(String[] args) throws Exception {
		
		long startTime = System.currentTimeMillis();
		FileInputStream fis = new FileInputStream(new File("Writesheet.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		ForkJoinReadsheet fjr =new ForkJoinReadsheet(5, workbook, true);
		ForkJoinTask<List<Row>> f = forkJoinPool.submit(fjr);
		System.out.println(Thread.currentThread());
		//System.out.println("查询的结果是：" + f.get());
		fjr.writeOut(f.get());
		forkJoinPool.shutdown();
		fis.close();
		long resultTime = System.currentTimeMillis() - startTime;
		System.out.println(resultTime);
	}

}