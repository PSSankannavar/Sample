
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ReadXLSX {
	
	static Long dependentValue = null;
	static String dependencyString = null;
	static Long defaultLayerAttachpoint =0L;
	static Boolean numericCheckForPercentage = false;
	static int startRowNumbertoRepeat= 0;
	static int endRowNumbertoRepeat= 0;
	static int  repeatCount = 0;
	static Boolean startRepeat  = false;
	static int countSeq = 1;
	static Connection connection;
	static boolean skipThisPOCreation = false;
	static Long bindProduct ;
	static Long bindSection ;
	static Long creditedBranch ;
	
	static Map<String,String> submissionInformation = new HashMap<String,String>();
	static Map<String,String> overallLimitsAndDeductibles = new HashMap<String,String>();
	static Map<String, List<String>> perilData = new HashMap<String, List<String>>();
	static Map<String, List<String>> perilRegion = new HashMap<String, List<String>>();
	static Map<String, List<String>> aIGParticipationData = new HashMap<String, List<String>>();
	static Map<String, List<String>> reinsuranceData = new HashMap<String, List<String>>();
	
	static Map<String,String> renewSubmissionInformationDataElements = new HashMap<String,String>();//5
	static Map<String, List<String>> additionalSublimit  = new HashMap<String, List<String>>();//6
	static Map<String,String> timeContingent = new HashMap<String,String>();//7
	static Map<String, List<String>> additionalPeril  = new HashMap<String, List<String>>();//8
	static Map<String,String> bindInfo = new HashMap<String,String>();//9
	static Map<String,String> bindAccount = new HashMap<String,String>();//10
	static Map<String, List<String>> bindAIGLayerData  = new HashMap<String, List<String>>();//11
	static Map<String,String> bindQuotaAIGData = new HashMap<String,String>();//12


	
	
	public static void main(String[] args)
			throws IOException, SQLException, EncryptedDocumentException, InvalidFormatException {

		System.out.println("Please Provide Master Sheet Path :"  );
		Scanner inputSheetPath = new Scanner(System.in);
		String filePath = inputSheetPath.nextLine();
		File fileInput = new File(filePath.replaceAll("\\\\", "\\\\\\\\"));
		
		System.out.println("Please enter number of PO's to be created");
		Scanner in = new Scanner(System.in);
		int numberOfPO = in.nextInt();
		if (numberOfPO < 0) {
			System.out.println("Not a number");
			System.exit(0);
		}
		int numberOfPOCount = 1;
	

		FileInputStream fileReader = new FileInputStream(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(fileReader);
		connection = OracleJDBC.getConnection();

		while (numberOfPOCount++ <= numberOfPO) {

			XSSFWorkbook outputWorkbook = new XSSFWorkbook();// .xlsx
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator rowIterator = sheet.rowIterator();

			while (rowIterator.hasNext()) {

				Row eachRow = (Row) rowIterator.next();
				Iterator<Cell> eachRowIterator = eachRow.cellIterator();
				EachRowData eachRowData = getEachRowDataIntoJavaObject(eachRowIterator);

				if (eachRowData.getToBeRepeated() != null && eachRowData.getToBeRepeated().contains("start")) {
					startRowNumbertoRepeat = eachRow.getRowNum();
					Random random = new Random();
					if (eachRowData.getFieldName().contains("Peril"))
						repeatCount = random.nextInt(8);
					else if (eachRowData.getFieldName().contains("Layer"))
						repeatCount = random.nextInt(4);
					else if (eachRowData.getFieldName().contains("Reinsurance"))
						repeatCount = random.nextInt(4);
				}
				if (eachRowData.getToBeRepeated() != null && eachRowData.getToBeRepeated().contains("end")) {
					endRowNumbertoRepeat = eachRow.getRowNum();
					if(repeatCount!=0)
					startRepeat = true;
				}
				if (startRepeat) {
					getOperationDone(eachRowData);
					for (int i = 0; i < repeatCount; i++) {
						int count = startRowNumbertoRepeat;
						while (count <= endRowNumbertoRepeat) {
							eachRow = sheet.getRow(count);
							Iterator<Cell> eachColumnRepeat = eachRow.cellIterator();
							EachRowData eachRowDataRepeat = getEachRowDataIntoJavaObject(eachColumnRepeat);
							getOperationDone(eachRowDataRepeat);

							count++;
							// call below code
						}
					}

					ReaetAllForNextTabRepeat();
				} else {
					getOperationDone(eachRowData);

				}
				if(skipThisPOCreation)
					break;
		}
			
			// Create Sheet
			if(!skipThisPOCreation)
			writeTosheet(numberOfPOCount, outputWorkbook);
			else
				System.out.println("PO " +( numberOfPOCount - 1) + " creation failed because of Invalid Data");

			System.out.println("----------------------------------------------");
			ResetAllForNewPO();
		}
		System.out.println("Successfully Created PO's to local path 'C://Pc//Automation//Result'");

		workbook.close();
		in.close();
		inputSheetPath.close();
		connection.close();
	}


	private static void ReaetAllForNextTabRepeat() {
		startRepeat = false;
		defaultLayerAttachpoint = 0L;
		dependentValue = null;
		countSeq = 1;
		repeatCount = 0;
		
	}


	private static void writeTosheet(int numberOfPOCount,XSSFWorkbook outputWorkbook ) throws EncryptedDocumentException, InvalidFormatException, IOException {
		if(submissionInformation != null){
			System.out.println("Completed SI tab of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(submissionInformation,"SI tab",outputWorkbook,numberOfPOCount);
			
		}
		if(overallLimitsAndDeductibles != null){
			System.out.println("Completed OverallLimitsAndDeductibles of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(overallLimitsAndDeductibles,"OverallLimitsAndDeductibles",outputWorkbook,numberOfPOCount);
			
		}
		if(perilData != null){
			System.out.println("Completed PerilData of PO " + (numberOfPOCount-1));
				writeToOutPutSheetMapList(perilData,"PerilData",outputWorkbook,numberOfPOCount);
				startRepeat = false;//Stop Repeat row 
			
		}
		/*if(tab4PrintReturnedList != null){
			System.out.println("Completed PerilRegion of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(tab4PrintReturnedList,"PerilRegion",outputWorkbook,numberOfPOCount);
			
		}*/
		if(perilRegion !=null){
			System.out.println("Completed PerilRegion of PO  " + (numberOfPOCount-1));
			writeToOutPutSheetMapList(perilRegion,"PerilRegion",outputWorkbook,numberOfPOCount);
			startRepeat = false;//Stop Repeat row 
		}
		/*if(tab5PrintReturnedList != null){
			System.out.println("Completed AIGParticipationData of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(tab5PrintReturnedList,"AIGParticipationData",outputWorkbook,numberOfPOCount);
			
		}*/
		if(aIGParticipationData !=null){
			System.out.println("Completed AIGParticipationData of PO  " + (numberOfPOCount-1));
			writeToOutPutSheetMapList(aIGParticipationData,"AIGParticipationData",outputWorkbook,numberOfPOCount);
			startRepeat = false;//Stop Repeat row 
		}
		/*if(tab6PrintReturnedList != null){
			System.out.println("Completed ReinsuranceData of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(tab6PrintReturnedList,"ReinsuranceData",outputWorkbook,numberOfPOCount);
			
		}*/
		if(reinsuranceData !=null){
			System.out.println("Completed ReinsuranceData of PO " + (numberOfPOCount-1));
			writeToOutPutSheetMapList(reinsuranceData,"ReinsuranceData",outputWorkbook,numberOfPOCount);
			startRepeat = false;//Stop Repeat row 
		}
		if(renewSubmissionInformationDataElements !=null){
			System.out.println("Completed renewSubmissionInformationData of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(renewSubmissionInformationDataElements,"renewSubmissionInformationData",outputWorkbook,numberOfPOCount);
		}
		if(additionalSublimit !=null){
			System.out.println("Completed additionalSublimit of PO " + (numberOfPOCount-1));
			writeToOutPutSheetMapList(additionalSublimit,"additionalSublimit",outputWorkbook,numberOfPOCount);
			startRepeat = false;//Stop Repeat row 
		}
		if(timeContingent !=null){
			System.out.println("Completed timeContingent of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(timeContingent,"timeContingent",outputWorkbook,numberOfPOCount);
		}
		if(additionalPeril !=null){
			System.out.println("Completed additionalPeril of PO " + (numberOfPOCount-1));
			writeToOutPutSheetMapList(additionalPeril,"additionalPeril",outputWorkbook,numberOfPOCount);
			startRepeat = false;//Stop Repeat row 
		}
		if(bindInfo !=null){
			System.out.println("Completed bindInfo of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(bindInfo,"bindInfo",outputWorkbook,numberOfPOCount);
		}
		if(bindAccount !=null){
			System.out.println("Completed bindAccount of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(bindAccount,"bindAccount",outputWorkbook,numberOfPOCount);
		}
		if(bindAIGLayerData !=null){
			System.out.println("Completed bindAIGLayerData of PO " + (numberOfPOCount-1));
			writeToOutPutSheetMapList(bindAIGLayerData,"bindAIGLayerData",outputWorkbook,numberOfPOCount);
			startRepeat = false;//Stop Repeat row 
		}
		if(bindQuotaAIGData !=null){
			System.out.println("Completed bindQuotaAIGData of PO " + (numberOfPOCount-1));
			writeToOutPutSheet(bindQuotaAIGData,"bindQuotaAIGData",outputWorkbook,numberOfPOCount);
		}
		
	}



	private static void writeToOutPutSheetMapList(Map<String, List<String>> printReturnedList, String tabName,
			XSSFWorkbook outputWorkbook, int numberOfPOCount) throws IOException {
		XSSFSheet sheet = outputWorkbook.createSheet(tabName);

		int rowCount = 0;
		int columnCount = 0;
		int numberOfRows = 0;
		String key = null;
		Row row = sheet.createRow(rowCount++);
		if(printReturnedList.containsKey("Peril Name"))
			key = "Peril Name";
		else if(printReturnedList.containsKey("Peril option"))
			key = "Peril option";
		else if(printReturnedList.containsKey("Peril Type"))
			key = "Peril Type";
			
		printReturnedList = RemoveAllDuplicateObjects(printReturnedList,key);
		for (Map.Entry<String, List<String>> entry : printReturnedList.entrySet()) {
			Cell cell = row.createCell(columnCount++);
			cell.setCellValue(entry.getKey());
			numberOfRows = entry.getValue().size();
		}
		for (int i = 0; i < numberOfRows; i++) {
			columnCount = 0;
			row = sheet.createRow(rowCount);
			for (Map.Entry<String, List<String>> entry : printReturnedList.entrySet()) {

				String string = entry.getValue().get(i);

				Cell cell = row.createCell(columnCount++);
				cell.setCellValue(string);

			}
			rowCount++;
		}
/*		FileOutputStream out = new FileOutputStream(
				"C:\\Pc\\Automation\\Result\\Policyoption" + (numberOfPOCount - 1) + ".xlsx");
		outputWorkbook.write(out);
		out.close();
*/

		File file = new File("C://Pc//Automation//Result");
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(file + "\\Policyoption" + (numberOfPOCount - 1) + ".xlsx"); // Or
		// .xlsx
		outputWorkbook.write(out);
		out.close();
	}

	private static Map<String, List<String>> RemoveAllDuplicateObjects(Map<String, List<String>> printReturnedList,String key) {
		Boolean isBreakable= false;
		for (Map.Entry<String, List<String>> entry : printReturnedList.entrySet()) {
			List<String> removeDuplicateValues = null ;
			if(key !=null)
			 removeDuplicateValues  = printReturnedList.get(key);
			if(removeDuplicateValues != null){
				Set<String> Strings =  findDuplicates(removeDuplicateValues);
				 List<String> duplicateIndex = findDuplicatesIndex(removeDuplicateValues,Strings);

				if (duplicateIndex != null && duplicateIndex.size() != 0) {
					Collections.sort(duplicateIndex);
					int index = 0;
					for (Map.Entry<String, List<String>> entryRemove : printReturnedList.entrySet()) {
						List<String> allList = entryRemove.getValue();
						for (int i = 0; i < duplicateIndex.size(); i++) {
							if (i == 0)
								index = Integer.parseInt(duplicateIndex.get(i));
							else
								index = Integer.parseInt(duplicateIndex.get(i)) - i;
							
							allList.remove(index);
						/*if(entryRemove.getKey().equals("Exclude")) entryRemove.getValue().set(i, "NO"); */
						}
						entryRemove.setValue(allList);
					}
					isBreakable = true;
				}
				if (isBreakable)
					break;
			
			}
		}
		return printReturnedList;
	}



	public static Set<String> findDuplicates(List<String> listContainingDuplicates)
	{ 
	  final Set<String> setToReturn = new HashSet<String>(); 
	  final Set<String> set1 = new HashSet<String>();

	  for (String yourInt : listContainingDuplicates)
	  {
	   if (!set1.add(yourInt))
	   {
	    setToReturn.add(yourInt);
	   }
	  }
	  return setToReturn;
	}
	
	private static List<String> findDuplicatesIndex(List<String> listA, Set<String> strings) {
	StringBuilder indexTodelete = new StringBuilder();
	for(String Duplicate: strings)
	{ 
		
		Boolean firstOccurance = false;
		for(int index = 0 ; index <listA.size(); index++){
			if(!firstOccurance){
				if(Duplicate.equals(listA.get(index)))
					firstOccurance = true;
			}else{
				if(Duplicate.equals(listA.get(index)))
						indexTodelete.append(index + ",") ;
			}
		}
		
	}
	if(indexTodelete.length() !=0){
	indexTodelete.deleteCharAt(indexTodelete.length()-1);
	List<String> values = Arrays.asList(indexTodelete.toString().split(","));
	return values;
	}
	return null;
	}

	private static void ResetAllForNewPO() {
		dependentValue = null;
		dependencyString = null;
		numericCheckForPercentage = false;
		startRowNumbertoRepeat = 0;
		endRowNumbertoRepeat = 0;
		repeatCount = 0;
		startRepeat = false;
		defaultLayerAttachpoint = 0L;
		submissionInformation = new HashMap<String, String>();
		overallLimitsAndDeductibles = new HashMap<String, String>();
		perilData = new HashMap<String, List<String>>();
		perilRegion = new HashMap<String, List<String>>();
		aIGParticipationData = new HashMap<String, List<String>>();
		reinsuranceData = new HashMap<String, List<String>>();
		skipThisPOCreation = false;
		renewSubmissionInformationDataElements = new HashMap<String, String>();// 5
		additionalSublimit = new HashMap<String, List<String>>();// 6
		timeContingent = new HashMap<String, String>();// 7
		additionalPeril = new HashMap<String, List<String>>();// 8
		bindInfo = new HashMap<String, String>();// 9
		bindAccount = new HashMap<String, String>();// 10
		bindAIGLayerData = new HashMap<String, List<String>>();// 11
		bindQuotaAIGData = new HashMap<String, String>();// 12
		bindProduct = null ;
		 bindSection  =null;
		 creditedBranch  = null;
		
	}



	private static String getOperationDone(EachRowData eachRowData) throws SQLException {
		
		String sqlQuery;
		String returnedVlaue = null;
		if (eachRowData.getType().equals("db")) {
			
			if(eachRowData.getFieldName().equals("Product-Bind"))
				dependentValue = bindProduct;
			if(eachRowData.getFieldName().equals("Section-Bind"))
				dependentValue = bindProduct;
			if(eachRowData.getFieldName().equals("Working Branch-Bind"))
				dependentValue = creditedBranch;

			if (eachRowData.getDependency().booleanValue()) {
				if (dependentValue != null ) {
					Boolean isDynamic = eachRowData.getParameter().contains("?");
					if(isDynamic)
						sqlQuery = replace(eachRowData.getParameter(), "?", dependentValue.toString());
					else
						sqlQuery = eachRowData.getParameter() ;//if specific data is to be 
					//sqlQuery = eachRowData.getParameter() + dependentValue;
					PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
					ResultSet resultSet = preparedStatement.executeQuery();
					List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
					if(possibleValueObject.isEmpty() || possibleValueObject.size() == 0)
						skipThisPOCreation = true;
					else{
					DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
					dependentValue = (Long) randomObjectSelected.getKey();
					returnedVlaue = randomObjectSelected.getValue();
					// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);
					}

				} else if(dependentValue == null ) {

					sqlQuery = eachRowData.getParameter();
					PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
					ResultSet resultSet = preparedStatement.executeQuery();
					List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
					if(possibleValueObject.isEmpty() || possibleValueObject.size() == 0)
						skipThisPOCreation = true;
					else{
					DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
					dependentValue = (Long) randomObjectSelected.getKey();
					returnedVlaue = randomObjectSelected.getValue();
					// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);
					}
				}
		
				if(eachRowData.getFieldName().equals("Division-NB"))
					bindProduct = dependentValue;
				if(eachRowData.getFieldName().equals("Credited Branch-NB"))
					creditedBranch = dependentValue;

			} else {
				if (dependentValue != null ) {
					Boolean isDynamic = eachRowData.getParameter().contains("?");
					if(isDynamic)
						sqlQuery = replace(eachRowData.getParameter(), "?", dependentValue.toString());
					else
						sqlQuery = eachRowData.getParameter();
					//sqlQuery = eachRowData.getParameter() + dependentValue;
					
					
					dependentValue = null;
					dependencyString = null;
				} else
					sqlQuery = eachRowData.getParameter();
				PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
				ResultSet resultSet = preparedStatement.executeQuery();
				List<DynamicObject> possibleValueObject = getPossiblevalueObject(resultSet);
				if(possibleValueObject.isEmpty() || possibleValueObject.size() == 0)
				skipThisPOCreation = true;
				else{
				DynamicObject randomObjectSelected = getRandomObject(possibleValueObject);
				if (randomObjectSelected != null)
					returnedVlaue = randomObjectSelected.getValue();
				// tab1PrintReturnedList.put(eachRowData.getFieldName(),returnedVlaue);
				if(eachRowData.getFieldName().equals("Credited Branch-NB"))
					creditedBranch = Long.parseLong(randomObjectSelected.getKey().toString());
				}

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
		} else if (eachRowData.getType().equals("radio") ) {

			if(eachRowData.getDependency().booleanValue())
			{
				if (dependencyString == null) {

					List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
					Random random = new Random();
					returnedVlaue = values.get(random.nextInt(values.size()));
					if (returnedVlaue.contains("Blanket"))
						dependencyString = "Blanket";
					else if (returnedVlaue.equalsIgnoreCase("BI") || returnedVlaue.equalsIgnoreCase("PD"))
						dependencyString = "ST";
					if(returnedVlaue.contains("Treaty"))
						dependencyString = "Treaty";
					else if(returnedVlaue.contains("Fac"))
						dependencyString = "Fac";
					else if(returnedVlaue.contains("50") ||returnedVlaue.contains("100"))
						dependencyString =  returnedVlaue;
					else if(returnedVlaue.contains("25") )
						dependencyString =  returnedVlaue;
				} else {

					List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
					returnedVlaue = getObjectFromDropDown(values,dependencyString);
					if(returnedVlaue.contains("%")){
						numericCheckForPercentage = true;
						
					}
						
				}
			
			
			}else{
				List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
				
				if(dependencyString != null)
				{
					returnedVlaue = getObjectFromDropDown(values,dependencyString);
					if(returnedVlaue.contains("%")){
						numericCheckForPercentage = true;
						
					}
				}else{
						List<String> value = Arrays.asList(eachRowData.getParameter().split(","));
						Random random = new Random();
						returnedVlaue = value.get(random.nextInt(value.size()));
					}
					
				dependencyString = null;
		}
		
		
		}
		else if(eachRowData.getType().equals("radio-db"))	{
			List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
			String radioVlaue = getObjectFromDropDown(values,dependencyString);
			
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
			
			if(returnedVlaue.contains("%")){
				numericCheckForPercentage = true;
				
			}
			dependencyString = null;
			
				
				
			
		}else if(eachRowData.getType().equals("date"))
		{
			
			LocalDate defaultLocalDate = LocalDate.now();
			returnedVlaue = addDays(defaultLocalDate,Integer.parseInt(eachRowData.getParameter()));
			
		}else if(eachRowData.getType().equals("numeric-check")){
			if(numericCheckForPercentage){
				
				if(eachRowData.getFieldName().contains("min") || eachRowData.getFieldName().contains("Min"))
				returnedVlaue = Long.toString(getRandomNumberInRange(30,50));
				else if(eachRowData.getFieldName().contains("max") || eachRowData.getFieldName().contains("Max"))
					returnedVlaue = Long.toString(getRandomNumberInRange(50,80));
				else returnedVlaue = Long.toString(getRandomNumberInRange(80,100));
			}
			else{
				int maxLength = Integer.parseInt(eachRowData.getParameter());
				Long randomNumber = getRandomNumber(maxLength);
				returnedVlaue = Long.toString(randomNumber);
			}
			if(!eachRowData.getDependency().booleanValue())
				numericCheckForPercentage = false;
		}else if(eachRowData.getType().equals("numeric-dependency")){

			if(eachRowData.getDependency())
			{
					int maxLength = Integer.parseInt(eachRowData.getParameter());
					Long randomNumber = getRandomNumber(maxLength);
					returnedVlaue = Long.toString(randomNumber);
					dependentValue = randomNumber;
			}else
			{
				
				if(startRepeat)
					returnedVlaue = defaultLayerAttachpoint.toString();
				else
					returnedVlaue = "0";
				defaultLayerAttachpoint = dependentValue + defaultLayerAttachpoint;
			}
	}else if(eachRowData.getType().equals("Auto-seq")){
			List<String> values = Arrays.asList(eachRowData.getParameter().split(","));
			String sqNumber = null;
			for(String string : values){
				if(string.contains(countSeq + "")){
					String[] secondValue = string.split(":");
					sqNumber = secondValue[1];
				}
					
			}
				returnedVlaue = sqNumber;
				countSeq++;
			
				
			
		}
		if (eachRowData.getTab() == 1.0) {
			submissionInformation.put(eachRowData.getFieldName(),returnedVlaue);

		} else if (eachRowData.getTab() == 2.1) {
			
			overallLimitsAndDeductibles.put(eachRowData.getFieldName(), returnedVlaue);

		} else if (eachRowData.getTab() == 2.2) {
			
			//tab3PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);
			putObjects (perilData, eachRowData.getFieldName(), returnedVlaue);
			

		}else if (eachRowData.getTab() == 2.3) {
			
			//tab4PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);
			putObjects (perilRegion, eachRowData.getFieldName(), returnedVlaue);

		}else if (eachRowData.getTab() == 3) {
			//tab5PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);
			putObjects (aIGParticipationData, eachRowData.getFieldName(), returnedVlaue);
			

		} else if (eachRowData.getTab() == 4) {
			//tab6PrintReturnedList.put(eachRowData.getFieldName(), returnedVlaue);
			putObjects (reinsuranceData, eachRowData.getFieldName(), returnedVlaue);
			

		} else if (eachRowData.getTab() == 5) {
			renewSubmissionInformationDataElements.put(eachRowData.getFieldName(), returnedVlaue);
			
		}
		 else if (eachRowData.getTab() == 6) {

			 putObjects(additionalSublimit, eachRowData.getFieldName(), returnedVlaue);
			}
		 else if (eachRowData.getTab() == 7) {

				timeContingent.put( eachRowData.getFieldName(), returnedVlaue);
			}
		 else if(eachRowData.getTab() == 8)
		 {
			 putObjects(additionalPeril, eachRowData.getFieldName(), returnedVlaue);
		 }
		 else if(eachRowData.getTab() == 9)
		 {
			 bindInfo.put( eachRowData.getFieldName(), returnedVlaue);
		 }
		 else if(eachRowData.getTab() == 10)
		 {
			 bindAccount.put(eachRowData.getFieldName(), returnedVlaue);
		 }
		 else if(eachRowData.getTab() == 11)
		 {
			 putObjects(bindAIGLayerData, eachRowData.getFieldName(), returnedVlaue);
		 }
		 else if(eachRowData.getTab() == 12)
		 {
			 bindQuotaAIGData.put(eachRowData.getFieldName(), returnedVlaue);
		 }
		
		return returnedVlaue;
	}
	
	private static void putObjects (Map<String, List<String>> outdoorElements, String key, String value) {
	    List<String> myClassList = outdoorElements.get(key);
	    if(myClassList == null) {
	        myClassList = new ArrayList<String>();
	        outdoorElements.put(key, myClassList);
	    }
	    myClassList.add(value);
	    
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
	
	public static String addDays(LocalDate date, int workdays) {
	    if (workdays < 1) {
	        return date.toString();
	    }

	    LocalDate result = date;
	    int addedDays = 0;
	    while (addedDays < workdays) {
	        result = result.plusDays(1);
	        if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY ||
	              result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
	            ++addedDays;
	        }
	    }

	    DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd,yyyy");
		Date dateConvert =java.sql.Date.valueOf(result);
		org.joda.time.DateTime dt = new org.joda.time.DateTime(dateConvert);
		String dateFormatted = formatter.print(dt);
	    return dateFormatted;
	}
	
	private static String getObjectFromDropDown(List<String> values,String dependencyString) {
		String returnedVlaue= null;
		List<String> temp = new ArrayList<>();
		if (dependencyString.equalsIgnoreCase("Blanket")) {
			for (String blanket : values)
				if (blanket.contains("Blanket")) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			Random random = new Random();
			returnedVlaue = temp.get(random.nextInt(temp.size()));

		} else if(dependencyString.equalsIgnoreCase("ST")){
			for (String blanket : values)
				if (!blanket.contains("Blanket")) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			Random random = new Random();
			returnedVlaue = temp.get(random.nextInt(temp.size()));
		}
		else if(dependencyString.equalsIgnoreCase("Treaty") || dependencyString.equalsIgnoreCase("Fac")){
			for (String blanket : values)
				if (blanket.contains(dependencyString)) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			Random random = new Random();
			returnedVlaue = temp.get(random.nextInt(temp.size()));
			
		}
		else if(dependencyString.contains("50") || dependencyString.contains("100")){
			for (String blanket : values)
				if (blanket.contains(dependencyString)) {
					String[] secondValue = blanket.split(":");
					temp.add(secondValue[1]);
				}
			LocalDate defaultLocalDate = LocalDate.now();
			int days = Integer.parseInt(temp.get(0));
			returnedVlaue = addDays(defaultLocalDate,days ).toString();
		}
		else if(dependencyString.contains("25")){
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


	private static void writeToOutPutSheet(Map<String, String> printReturnedList,String tabName,XSSFWorkbook outputWorkbook,int numberOfPOCount) throws IOException, EncryptedDocumentException, InvalidFormatException {

	
		XSSFSheet sheet = outputWorkbook.createSheet(tabName);

		int columnCount = 0;
		Row row1 = sheet.createRow(0);
		Row row2 = sheet.createRow(1);
		for (Map.Entry<String, String> entry : printReturnedList.entrySet()) {
			Cell cell = row1.createCell(columnCount++);
			cell.setCellValue(entry.getKey());
		}
		columnCount = 0;
		for (Map.Entry<String, String> entry : printReturnedList.entrySet()) {
			Cell cell = row2.createCell(columnCount++);
			cell.setCellValue(entry.getValue());

		}
		
		File file = new File("C://Pc//Automation//Result");
		if (!file.exists()) {
			file.mkdirs();
		} 
			FileOutputStream out = new FileOutputStream(
					file +"\\Policyoption" + (numberOfPOCount - 1) + ".xlsx"); // Or
			// .xlsx
			outputWorkbook.write(out);
			out.close();
		
	}

	private static DynamicObject getRandomObject(List<DynamicObject> possibleValueObject) {
		 Random randomno = new Random();
		 DynamicObject returnObject = null ;
		 
		 if(possibleValueObject != null){
			int number =  randomno.nextInt(possibleValueObject.size() );
			if(number != 0 )
				number = number -1;
		  returnObject = possibleValueObject.get(number);
		 }
		 return returnObject;
	}

	private static List<DynamicObject> getPossiblevalueObject(ResultSet resultSet) throws SQLException {
		List<DynamicObject> dynamicObjects = new ArrayList<DynamicObject>();
		while(resultSet.next())
		{
			DynamicObject dynamicObject = new DynamicObject();
			dynamicObject.setKey(resultSet.getLong(1));
			dynamicObject.setValue(resultSet.getString(2));
			dynamicObjects.add(dynamicObject);
		}
		return dynamicObjects;
	}

	private static EachRowData getEachRowDataIntoJavaObject(Iterator<Cell> eachColumn) {
		
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
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				eachRowData.setParameter(Integer.toString((int)cell.getNumericCellValue()));
				else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					eachRowData.setParameter(cell.getStringCellValue());
			}
			if (cell.getColumnIndex() == 5) {
				eachRowData.setToBeRepeated(cell.getStringCellValue());
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