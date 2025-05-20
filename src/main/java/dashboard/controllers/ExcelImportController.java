package dashboard.controllers;

import dashboard.Model.TransformationSummary;
import dashboard.Model.TransformationRecord;
import dashboard.Model.TransformationDataStore;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelImportController {

    @FXML private Label dropLabel;
    @FXML private Label successLabel;

    @FXML
    private void initialize() {
        successLabel.setVisible(false);
        dropLabel.setOnDragOver(this::handleDragOver);
        dropLabel.setOnDragDropped(this::handleFileDrop);
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != dropLabel && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void handleFileDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            if (file.getName().endsWith(".xlsx")) {
                loadExcelToSummary(file);
                loadDetailedRecords(file);
                success = true;
            } else {
                showAlert("Invalid file", "Please drop a valid .xlsx Excel file.");
            }
        }
        event.setDropCompleted(success);
        event.consume();
        if (success) { successLabel.setVisible(true); }
    }

    private void loadExcelToSummary(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("summary");
            if (sheet == null) {
                showAlert("Tabblad niet gevonden", "Tabblad 'summary' niet gevonden.");
                return;
            }

            TransformationDataStore.clearAllSummaries();

            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row row = rows.next();
                String id = getCellValue(row.getCell(0));
                String name = getCellValue(row.getCell(1));
                // Deze waardes worden nu genegeerd — summary wordt nu dynamisch opgebouwd
                TransformationDataStore.addSummary(new TransformationSummary(id, name));
            }

            System.out.println("✅ Aantal lege summaries geïmporteerd: " + TransformationDataStore.getSummaries().size());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Import mislukt", "Fout bij summary-import:\n" + e.getMessage());
        }
    }

    private void loadDetailedRecords(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            TransformationDataStore.clearAll();

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                if (sheetName.equalsIgnoreCase("summary")) continue;

                System.out.println("➡️ Bezig met import van tabblad: " + sheetName);

                Iterator<Row> rows = sheet.iterator();
                List<String> headers = new ArrayList<>();

                if (!rows.hasNext()) continue;
                Row headerRow = rows.next();
                for (Cell cell : headerRow) {
                    headers.add(getCellValue(cell));
                }

                while (rows.hasNext()) {
                    Row row = rows.next();
                    Map<String, String> rowData = new HashMap<>();
                    for (int iCol = 0; iCol < headers.size(); iCol++) {
                        Cell cell = row.getCell(iCol);
                        rowData.put(headers.get(iCol), cell != null ? getCellValue(cell) : "");
                    }

                    String itemId = rowData.getOrDefault("itemId", "");
                    String itemRev = rowData.getOrDefault("itemRev", "");

                    if (itemId.isBlank() && itemRev.isBlank()) continue;

                    String process        = rowData.getOrDefault("process", "");
                    String processDetails = rowData.getOrDefault("processDetails", "");
                    String input          = rowData.getOrDefault("input", "");
                    String output         = rowData.getOrDefault("output", "");
                    String decision       = rowData.getOrDefault("decision", "");

                    for (int col = 2; col < headers.size(); col++) {
                        String transformationId = headers.get(col);
                        String cellValue = rowData.get(transformationId);

                        if (cellValue != null && !cellValue.equals("0") && !cellValue.isBlank()) {
                            String transformationName = findTransformationName(transformationId);

                            String sheetNameLower = sheetName.toLowerCase();
                            String status;
                            if (sheetNameLower.contains("error")) {
                                status = "Error";
                            } else if (sheetNameLower.contains("warning")) {
                                status = "Warning";
                            } else {
                                status = "Success";
                            }

                            TransformationRecord record = new TransformationRecord(
                                    transformationId,
                                    transformationName,
                                    itemId,
                                    itemRev,
                                    process,
                                    processDetails,
                                    input,
                                    output,
                                    status,
                                    decision
                            );

                            TransformationDataStore.addRecord(record);

                            // ✅ Update samenvatting
                            TransformationSummary summary = TransformationDataStore.getOrCreateSummary(transformationId, transformationName);
                            summary.increment(status);
                        }
                    }
                }

                System.out.println("✅ Records geïmporteerd uit tabblad: " + sheetName);
            }

            System.out.println("📦 Totale records geladen: " + TransformationDataStore.getRecords().size());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Import mislukt", "Fout bij detailrecords import:\n" + e.getMessage());
        }
    }

    private String findTransformationName(String transformationId) {
        return TransformationDataStore.getSummaries().stream()
                .filter(s -> s.getTransformationId().equals(transformationId))
                .map(TransformationSummary::getTransformationName)
                .findFirst()
                .orElse("?");
    }

    private String getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
