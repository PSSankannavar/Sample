import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HappyFlow {
	public static void main(String[] args)
			throws IOException, SQLException, EncryptedDocumentException, InvalidFormatException {
		Scanner in = new Scanner(System.in);
		int numberOfPO = in.nextInt();
		if (numberOfPO < 0) {
			System.out.println("Not a number");
			System.exit(0);
		}
		int numberOfPOCount = 1;
		File fileInput = new File("C:\\Pc\\Automation\\Input\\MasterSheet.xlsx");

		FileInputStream fileReader = new FileInputStream(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(fileReader);
		Connection connection = OracleJDBC.getConnection();
		while (numberOfPOCount++ <= numberOfPO) {
			XSSFWorkbook outputWorkbook = new XSSFWorkbook();// .xlsx
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator rowIteator = sheet.rowIterator();
			Map<String, String> tab1PrintReturnedList = new HashMap<String, String>();
			Map<String, String> tab2PrintReturnedList = new HashMap<String, String>();
			Map<String, String> tab3PrintReturnedList = new HashMap<String, String>();
			Map<String, String> tab4PrintReturnedList = new HashMap<String, String>();
			Map<String, String> tab5PrintReturnedList = new HashMap<String, String>();
			Map<String, String> tab6PrintReturnedList = new HashMap<String, String>();
			Long dependentValue = null;
			String dependencyString = null;
			Boolean numericCheckForPercentage = false;
			while (rowIteator.hasNext()) {
				Row eachRow = (Row) rowIteator.next();
				Iterator<Cell> eachColumn = eachRow.cellIterator();
				EachRowData eachRowData = getEachRowDate(eachColumn);
				String returnedVlaue = null;
				String sqlQuery = null;
				if (eachRowData.getType().equals("db")) {

					if (eachRowData.getDependency().booleanValue()) {
						if (dependentValue != null) {
							Boolean isDynamic = eachRowData.getParameter().contains("?");
							if (isDynamic)
								sqlQuery = replace(eachRowData.getParameter(), "?", dependentValue.toString());
							else
								sqlQuery = eachRowData.getParameter() + dependentValue;
							PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
							ResultSet resultSet = preparedStatement.executeQuery();
							List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
							DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
							dependentValue = (Long) randomObjectSelected.getKey();
							returnedVlaue = randomObjectSelected.getValue();
							// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);

						} else if (dependentValue == null) {

							sqlQuery = eachRowData.getParameter();
							PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
							ResultSet resultSet = preparedStatement.executeQuery();
							List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
							DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
							dependentValue = (Long) randomObjectSelected.getKey();
							returnedVlaue = randomObjectSelected.getValue();
							// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);
						}

					} else {
						if (dependentValue != null) {
							Boolean isDynamic = eachRowData.getParameter().contains("?");
							if (isDynamic)
								sqlQuery = replace(eachRowData.getParameter(), "?", dependentValue.toString());
							else
								sqlQuery = eachRowData.getParameter() + dependentValue;
							dependentValue = null;
							dependencyString = null;
						} else
							sqlQuery = eachRowData.getParameter();
						PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
						ResultSet resultSet = preparedStatement.executeQuery();
						List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
						DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
						if (randomObjectSelected != null)
							returnedVlaue = randomObjectSelected.getValue();
						// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);

					}

				} else if (eachRowData.getType().equals("text")) {
					int maxLength = Integer.parseInt(eachRowData.getParameter());
					String randomString = getRandomString(maxLength);
					/*
					 * PrintToSheet printToSheet = new PrintToSheet();
					 * printToSheet.setField(eachRowData.getFieldName());
					 * printToSheet.setValue(randomString);
					 */
					returnedVlaue = randomString;
					// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);

				} else if (eachRowData.getType().equals("numeric")) {
					int maxLength = Integer.parseInt(eachRowData.getParameter());
					Long randomNumber = getRandomNumber(maxLength);
					returnedVlaue = Long.toString(randomNumber);
					// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);
				} else if (eachRowData.getType().equals("Auto")) {
					String preDefined = eachRowData.getParameter();
					returnedVlaue = preDefined;
					// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);
				} else if (eachRowData.getType().equals("radio")) {

					if (eachRowData.getDependency().booleanValue()) {
						if (dependencyString == null) {

							List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
							Random random = new Random();
							returnedVlaue = values.get(random.nextInt(values.size()));
							if (returnedVlaue.contains("Blanket"))
								dependencyString = "Blanket";
							else if (returnedVlaue.equalsIgnoreCase("BI") || returnedVlaue.equalsIgnoreCase("PD"))
								dependencyString = "ST";
							if (returnedVlaue.contains("Treaty"))
								dependencyString = "Treaty";
							else if (returnedVlaue.contains("Fac"))
								dependencyString = "Fac";

						} else {

							List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
							returnedVlaue = getBlanketDropDown(values, dependencyString);
							if (returnedVlaue.contains("%")) {
								numericCheckForPercentage = true;
							}
						}
					} else {
						List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
						if (dependencyString != null) {
							returnedVlaue = getBlanketDropDown(values, dependencyString);
							if (returnedVlaue.contains("%")) {
								numericCheckForPercentage = true;
							}
						} else {
							List<String> value = Arrays.asList(eachRowData.getParameter().split(","));
							Random random = new Random();
							returnedVlaue = value.get(random.nextInt(value.size()));
						}
						dependencyString = null;
					}
				} else if (eachRowData.getType().equals("radio-db")) {
					List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
					String radioVlaue = getBlanketDropDown(values, dependencyString);
					String sql = null;
					if (dependencyString.contains("Treaty"))
						sql = "Select trty_id,trty_nm from " + radioVlaue;
					else
						sql = "Select fac_prvdr_id,fac_prvdr_nm from " + radioVlaue;
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					ResultSet resultSet = preparedStatement.executeQuery();
					List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
					DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
					if (randomObjectSelected != null)
						returnedVlaue = randomObjectSelected.getValue();
					if (returnedVlaue.contains("%")) {
						numericCheckForPercentage = true;
					}
					dependencyString = null;
				} else if (eachRowData.getType().equals("date")) {
					LocalDate defaultLocalDate = LocalDate.now();
					defaultLocalDate = addDays(defaultLocalDate, Integer.parseInt(eachRowData.getParameter()));
					returnedVlaue = defaultLocalDate.toString();
				} else if (eachRowData.getType().equals("numeric-check")) {
					if (numericCheckForPercentage) {
						if (eachRowData.getFieldName().contains("min") || eachRowData.getFieldName().contains("Min"))
							returnedVlaue = Long.toString(getRandomNumberInRange(30, 50));
						else if (eachRowData.getFieldName().contains("max")
								|| eachRowData.getFieldName().contains("Max"))
							returnedVlaue = Long.toString(getRandomNumberInRange(50, 80));
						else
							returnedVlaue = Long.toString(getRandomNumberInRange(80, 100));
					} else {
						int maxLength = Integer.parseInt(eachRowData.getParameter());
						Long randomNumber = getRandomNumber(maxLength);
						returnedVlaue = Long.toString(randomNumber);
					}
					if (!eachRowData.getDependency().booleanValue())
						numericCheckForPercentage = false;
				}
				if (eachRowData.getTab() == 1.0) {
					tab1PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);

				} else if (eachRowData.getTab() == 2.1) {
					tab2PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);

				} else if (eachRowData.getTab() == 2.2) {
					tab3PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);

				} else if (eachRowData.getTab() == 2.3) {
					tab4PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);

				} else if (eachRowData.getTab() == 3) {
					tab5PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);

				} else if (eachRowData.getTab() == 4) {
					tab6PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);

				} else if (eachRowData.getTab() == 5) {

				}
				// Create Sheet
			}

			if (tab1PrintReturnedList != null) {
				System.out.println("Completed SI tab of PO " + (numberOfPOCount - 1));
				writeToOutPutSheet(tab1PrintReturnedList, "SI tab", outputWorkbook, numberOfPOCount);
			}
			if (tab2PrintReturnedList != null) {
				System.out.println("Completed OverallLimitsAndDeductibles of PO " + (numberOfPOCount - 1));
				writeToOutPutSheet(tab2PrintReturnedList, "OverallLimitsAndDeductibles", outputWorkbook,
						numberOfPOCount);
			}
			if (tab3PrintReturnedList != null) {
				System.out.println("Completed PerilData of PO " + (numberOfPOCount - 1));
				writeToOutPutSheet(tab3PrintReturnedList, "PerilData", outputWorkbook, numberOfPOCount);
			}
			if (tab4PrintReturnedList != null) {
				System.out.println("Completed PerilRegion of PO " + (numberOfPOCount - 1));
				writeToOutPutSheet(tab4PrintReturnedList, "PerilRegion", outputWorkbook, numberOfPOCount);
			}
			if (tab5PrintReturnedList != null) {
				System.out.println("Completed AIGParticipationData of PO " + (numberOfPOCount - 1));
				writeToOutPutSheet(tab5PrintReturnedList, "AIGParticipationData", outputWorkbook, numberOfPOCount);
			}
			if (tab6PrintReturnedList != null) {
				System.out.println("Completed ReinsuranceData of PO " + (numberOfPOCount - 1));
				writeToOutPutSheet(tab6PrintReturnedList, "ReinsuranceData", outputWorkbook, numberOfPOCount);
			}

			System.out.println("----------------------------------------------");
		}
		System.out.println("Successfully Created ");
		workbook.close();
		in.close();
	}

	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	static String replace(String str, String pattern, String replace) {
		int start = 0;
		int index = 0;
		StringBuffer result = new StringBuffer();

		while ((index = str.indexOf(pattern, start)) >= 0) {
			result.append(str.substring(start, index));
			result.append(replace);
			start = index + pattern.length();
		}
		result.append(str.substring(start));
		return result.toString();
	}

	public static LocalDate addDays(LocalDate date, int workdays) {
		if (workdays < 1) {
			return date;
		}

		LocalDate result = date;
		int addedDays = 0;
		while (addedDays < workdays) {
			result = result.plusDays(1);
			if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
				++addedDays;
			}
		}

		return result;
	}

	private static String getBlanketDropDown(List<String> values, String dependencyString) {
		String returnedVlaue = null;
		List<String> temp = new ArrayList<>();
		if (dependencyString.equalsIgnoreCase("Blanket")) {
			for (String blanket : values)
				if (blanket.contains("Blanket")) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			Random random = new Random();
			returnedVlaue = temp.get(random.nextInt(temp.size()));

		} else if (dependencyString.equalsIgnoreCase("ST")) {
			for (String blanket : values)
				if (!blanket.contains("Blanket")) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			Random random = new Random();
			returnedVlaue = temp.get(random.nextInt(temp.size()));
		} else if (dependencyString.equalsIgnoreCase("Treaty") || dependencyString.equalsIgnoreCase("Fac")) {
			for (String blanket : values)
				if (blanket.contains(dependencyString)) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			Random random = new Random();
			returnedVlaue = temp.get(random.nextInt(temp.size()));
		}
		return returnedVlaue;
	}

	private static Long getRandomNumber(int maxLength) {
		Random random = new Random();
		char[] digits = new char[maxLength];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < maxLength; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
	}

	private static void writeToOutPutSheet(Map<String, String> printReturnedList, String tabName,
			XSSFWorkbook outputWorkbook, int numberOfPOCount)
			throws IOException, EncryptedDocumentException, InvalidFormatException {

		XSSFSheet sheet = outputWorkbook.createSheet(tabName);

		int columnCount = 0;
		Row row1 = sheet.createRow(0);
		Row row2 = sheet.createRow(1);
		for (Map.Entry<String, String> entry : printReturnedList.entrySet()) {
			Cell cell = row1.createCell(++columnCount);
			cell.setCellValue(entry.getKey());
		}
		columnCount = 0;
		for (Map.Entry<String, String> entry : printReturnedList.entrySet()) {
			Cell cell = row2.createCell(++columnCount);
			cell.setCellValue(entry.getValue());

		}
		FileOutputStream out = new FileOutputStream(
				"C:\\Pc\\Automation\\Result\\Policyoption" + (numberOfPOCount - 1) + ".xlsx"); // Or
		// .xlsx
		outputWorkbook.write(out);
		out.close();
	}

	private static DynamicObject getRandomObject(List<DynamicObject> possibleValueObject) {
		Random randomno = new Random();
		DynamicObject returnObject = null;

		if (possibleValueObject != null) {
			int number = randomno.nextInt(possibleValueObject.size());
			if (number != 0)
				number = number - 1;
			returnObject = possibleValueObject.get(number);
		}
		return returnObject;
	}

	private static List<DynamicObject> getPossiblevalueObject(ResultSet resultSet) throws SQLException {
		List<DynamicObject> dynamicObjects = new ArrayList<DynamicObject>();
		while (resultSet.next()) {
			DynamicObject dynamicObject = new DynamicObject();
			dynamicObject.setKey(resultSet.getLong(1));
			dynamicObject.setValue(resultSet.getString(2));
			dynamicObjects.add(dynamicObject);
		}
		return dynamicObjects;
	}

	private static EachRowData getEachRowDate(Iterator<Cell> eachColumn) {
		EachRowData eachRowData = new EachRowData();
		while (eachColumn.hasNext()) {

			Cell cell = eachColumn.next();
			if (cell.getColumnIndex() == 0) {
				eachRowData.setTab((Double) cell.getNumericCellValue());
			}
			if (cell.getColumnIndex() == 1) {
				eachRowData.setFieldName(cell.getStringCellValue());
			}
			if (cell.getColumnIndex() == 2) {
				eachRowData.setType(cell.getStringCellValue());
			}
			if (cell.getColumnIndex() == 3) {
				eachRowData.setDependency(cell.getBooleanCellValue());
			}
			if (cell.getColumnIndex() == 4) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					eachRowData.setParameter(Integer.toString((int) cell.getNumericCellValue()));
				else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					eachRowData.setParameter(cell.getStringCellValue());
			}

		}
		return eachRowData;
	}

	private static String getRandomString(int MaxValue) {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < MaxValue) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}
}
