package dashboard.controllers;

import dashboard.Model.TransformationDataStore;
import dashboard.Model.TransformationRecord;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SheetDetailController {

    @FXML private Label detailTitle;
    @FXML private TableView<TransformationRecord> detailTable;
    @FXML private TableColumn<TransformationRecord, String> colProcess;
    @FXML private TableColumn<TransformationRecord, String> colDetails;
    @FXML private TableColumn<TransformationRecord, String> colInput;
    @FXML private TableColumn<TransformationRecord, String> colOutput;
    @FXML private TableColumn<TransformationRecord, String> colStatus;
    @FXML private TableColumn<TransformationRecord, String> colDecision;

    public void loadDetails(String itemId, String itemRev, String sheetPrefix, String statusFilter) {
        detailTitle.setText("Details for " + itemId + " Rev: " + itemRev + " in " + sheetPrefix + " (" + statusFilter + ")");

        colProcess.setCellValueFactory(new PropertyValueFactory<>("process"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("processDetails"));
        colInput.setCellValueFactory(new PropertyValueFactory<>("input"));
        colOutput.setCellValueFactory(new PropertyValueFactory<>("output"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDecision.setCellValueFactory(new PropertyValueFactory<>("decision"));

        List<TransformationRecord> filtered = TransformationDataStore.getRecords().stream()
                .filter(r -> r.getItemId().equals(itemId))
                .filter(r -> r.getItemRev().equals(itemRev))
                .filter(r -> r.getStatus().equalsIgnoreCase(statusFilter))
                .filter(r -> r.getTransformationId().startsWith(sheetPrefix)) // e.g. all T009_*
                .collect(Collectors.toList());

        detailTable.setItems(FXCollections.observableArrayList(filtered));
    }

    public void loadDetails(String transformationId, String status) {
        detailTitle.setText("Details for " + transformationId + " with status: " + status);

        List<TransformationRecord> filtered = TransformationDataStore.getRecords().stream()
                .filter(r -> r.getTransformationId().equalsIgnoreCase(transformationId))
                .filter(r -> r.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());

        detailTable.setItems(FXCollections.observableArrayList(filtered));
    }


}
