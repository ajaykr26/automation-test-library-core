package library.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelHelper {
    private static final String EXCEPTION_PROCESSING_FILE = "empty data found in excel file.";

    private ExcelHelper() {

    }

    private static Logger getLogger() {
        return LogManager.getLogger(ExcelHelper.class);
    }

    public static List<ArrayList<Object>> getDataAsArrayList(String filepath, String worksheet, String... recordset) {
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filepath);
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet(worksheet);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            int maxDataCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    maxDataCount = row.getLastCellNum();
                }
                if (isRowEmpty(row)) {
                    break;
                }

                ArrayList<Object> singleRow = new ArrayList<>();

                for (int c = 0; c < maxDataCount; c++) {
                    Cell cell = row.getCell(c);
                    singleRow.add(getCellData(cell, formulaEvaluator));
                }

                if (recordset.length > 0) {
                    if (singleRow.get(0).toString().equalsIgnoreCase(recordset[0])) {
                        data.add(singleRow);
                    }
                } else {
                    data.add(singleRow);
                }
            }

        } catch (Exception exception) {
            getLogger().debug(EXCEPTION_PROCESSING_FILE, exception);
        }
        return data;
    }

    public static Map<String, LinkedHashMap<String, Object>> getDataAsMap(String filepath, String worksheet) {
        Map<String, LinkedHashMap<String, Object>> dataMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> mapTemp = null;
        String dataset = null;
        Row headerRow = null;
        Object key = null;
        Object value = null;
        try (FileInputStream file = new FileInputStream(filepath);
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet(worksheet);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            int lastRow = sheet.getLastRowNum();
            int currentRowNum = 0;
            while (currentRowNum < lastRow) {
                headerRow = sheet.getRow(currentRowNum);

                dataset = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(0), formulaEvaluator).toString();
                mapTemp = new LinkedHashMap<>();
                for (int cellCounter = 1; cellCounter < sheet.getRow(currentRowNum).getLastCellNum(); cellCounter++) {
                    key = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(cellCounter), formulaEvaluator);
                    value = getCellData(sheet.getRow(currentRowNum + 1).getCell(cellCounter), formulaEvaluator);
                    if (value != null && key != null) {
                        mapTemp.put(key.toString(), value);
                    }
                }
                currentRowNum += 2;
                dataMap.put(dataset, mapTemp);
            }
        } catch (FileNotFoundException exception) {
            getLogger().debug("StaticTestData.xlsx file not found. returning empty data.");
        } catch (IOException | NullPointerException ioException) {
            getLogger().debug("{} worksheet not found in StaticTestData.xlsx file. returning empty data.", worksheet);
        }
        return dataMap;
    }

    public static List<Map<String, Object>> getDataAsMapWithoutIndex(String filepath, String worksheet) {
        List<Map<String, Object>> dataMap = new ArrayList<>();
        LinkedHashMap<String, Object> mapTemp = null;
        Row headerRow = null;
        Object key = null;
        Object value = null;
        try (FileInputStream file = new FileInputStream(filepath);
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet(worksheet);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            int lastRow = sheet.getLastRowNum();
            int currentRowNum = 0;
            while (currentRowNum < lastRow) {
                if (isRowEmpty(sheet.getRow(currentRowNum))) {
                    currentRowNum++;
                    continue;
                }
                headerRow = sheet.getRow(currentRowNum);

                for (int j = currentRowNum + 1; j < lastRow; j++) {
                    if (isRowEmpty(sheet.getRow(j))) {
                        currentRowNum = j + 1;
                        break;
                    }

                    mapTemp = new LinkedHashMap<>();
                    for (int cellCounter = 1; cellCounter < sheet.getRow(j).getLastCellNum(); cellCounter++) {
                        key = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(0), formulaEvaluator);
                        value = getCellData(sheet.getRow(j).getCell(cellCounter), formulaEvaluator);
                        if (value != null && key != null) {
                            mapTemp.put(key.toString(), value);
                        }
                    }

                }
                currentRowNum++;
                dataMap.add(mapTemp);
            }
        } catch (Exception exception) {
            getLogger().debug(EXCEPTION_PROCESSING_FILE);
        }
        return dataMap;
    }

    private static Object getCellData(Cell cell, FormulaEvaluator formulaEvaluator) {
        Object obj = null;
        if (cell == null) return null;
        switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = (new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
                } else {
                    obj = (long) cell.getNumericCellValue();
                }
                break;
            case BLANK:
                break;
            case STRING:
            default:
                obj = (cell.getStringCellValue());
        }
        return obj;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, LinkedHashMap<String, Object>> setDataAsMap(String filepath, String worksheet) {
        Map<String, LinkedHashMap<String, Object>> dataMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> mapTemp = null;
        String dataset = null;
        Row headerRow = null;
        Object key = null;
        Object value = null;
        try (FileInputStream file = new FileInputStream(filepath);
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet(worksheet);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            int lastRow = sheet.getLastRowNum();
            int currentRowNum = 0;
            while (currentRowNum < lastRow) {
                headerRow = sheet.getRow(currentRowNum);

                dataset = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(0), formulaEvaluator).toString();
                mapTemp = new LinkedHashMap<>();
                for (int cellCounter = 1; cellCounter < sheet.getRow(currentRowNum).getLastCellNum(); cellCounter++) {
                    key = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(cellCounter), formulaEvaluator);
                    value = getCellData(sheet.getRow(currentRowNum + 1).getCell(cellCounter), formulaEvaluator);
                    if (value != null && key != null) {
                        mapTemp.put(key.toString(), value);
                    }
                }
                currentRowNum += 2;
                dataMap.put(dataset, mapTemp);
            }
        } catch (Exception exception) {
            getLogger().debug(EXCEPTION_PROCESSING_FILE);
        }
        return dataMap;
    }

    public static void UpdateDictionaryValueToExistingExcelFile(File file, String dictionaryKey) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();

            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            ArrayList<String> details = (ArrayList<String>) TestContext.getInstance().testdataGet(dictionaryKey);
            for (Object info : details) {
                Cell cell = row.createCell(columnCount++);
                if (info instanceof String) {
                    cell.setCellValue((String) info);
                } else if (info instanceof Integer) {
                    cell.setCellValue((Integer) info);
                }
            }
            inputStream.close();
            FileOutputStream os = new FileOutputStream(file);
            workbook.write(os);
            workbook.close();
            os.close();
            getLogger().debug("Excel file has been updated successfully.");
        } catch (EncryptedDocumentException | IOException e) {
            getLogger().debug("Exception while updating an existing excel file.");
            e.printStackTrace();
        }
    }

    public static void UpdateDictionaryValuesToExistingExcelFile(File file, List<ArrayList<Object>> lists) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();

            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            for (ArrayList<Object> list : lists) {
                for (Object info : list) {
                    Cell cell = row.createCell(columnCount++);
                    if (info instanceof String) {
                        cell.setCellValue((String) info);
                    } else if (info instanceof Integer) {
                        cell.setCellValue((Integer) info);
                    }
                }
            }

            inputStream.close();
            FileOutputStream os = new FileOutputStream(file);
            workbook.write(os);
            workbook.close();
            os.close();
            getLogger().debug("Excel file has been updated successfully.");
        } catch (EncryptedDocumentException | IOException e) {
            getLogger().debug("Exception while updating an existing excel file.");
            e.printStackTrace();
        }
    }
}