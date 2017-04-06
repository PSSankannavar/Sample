import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadANDTest {
	
	

	public static void main(String[] args) throws IOException {
		
		String filePath = "C:\\Users\\PSankann\\Documents\\Automation_Output\\GES PMO - BOTICELLI.xls";
		File fileInput = new File(filePath);
		FileInputStream fileReader = new FileInputStream(fileInput);
		HSSFWorkbook workbook = new HSSFWorkbook(fileReader);		
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator rowIterator = sheet.rowIterator();
		 
		
		Row eachRowTest = sheet.getRow(4);
		
		Cell cellTest = eachRowTest.getCell(3);
		System.out.println(cellTest.getStringCellValue());
	        
		while (rowIterator.hasNext()) {

			//driver.findElement(By.id("nome")).sendKeys(nome2);
			Row eachRow = (Row) rowIterator.next();
			Iterator<Cell> eachRowIterator = eachRow.cellIterator();
			while (eachRowIterator.hasNext()) {
				Cell cell = eachRowIterator.next();
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				System.out.println(cell.getNumericCellValue());
				else if(cell.getCellType() == Cell.CELL_TYPE_STRING)
					System.out.println(cell.getStringCellValue());
					
			}
			}
		}

	}


