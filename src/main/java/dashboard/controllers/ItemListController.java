package dashboard.controllers;

import dashboard.Model.SummaryStatusRow;
import dashboard.Model.TransformationDataStore;
import dashboard.Model.TransformationSummary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ItemListController {

    @FXML private Label titleLabel;
    @FXML private TableView<SummaryStatusRow> itemTable;
    @FXML private TableColumn<SummaryStatusRow, String> colTransformationId;
    @FXML private TableColumn<SummaryStatusRow, String> colTransformationName;
    @FXML private TableColumn<SummaryStatusRow, Integer> colStatusCount;

    public void loadItemsByStatus(String statusFilter) {
        titleLabel.setText("Transformations with status: " + statusFilter);

        List<SummaryStatusRow> rows = TransformationDataStore.getSummaries().stream()
                .filter(s -> switch (statusFilter.toLowerCase()) {
                    case "success" -> s.getSuccessCount() > 0;
                    case "warning" -> s.getWarningCount() > 0;
                    case "error"   -> s.getErrorCount() > 0;
                    default        -> false;
                })
                .map(s -> new SummaryStatusRow(
                        s.getTransformationId(),
                        s.getTransformationName(),
                        s.getSuccessCount(),
                        s.getWarningCount(),
                        s.getErrorCount()
                ))
                .collect(Collectors.toList());

        colTransformationId.setCellValueFactory(new PropertyValueFactory<>("transformationId"));
        colTransformationName.setCellValueFactory(new PropertyValueFactory<>("transformationName"));

        switch (statusFilter.toLowerCase()) {
            case "success" -> colStatusCount.setCellValueFactory(new PropertyValueFactory<>("successCount"));
            case "warning" -> colStatusCount.setCellValueFactory(new PropertyValueFactory<>("warningCount"));
            case "error"   -> colStatusCount.setCellValueFactory(new PropertyValueFactory<>("errorCount"));
        }

        ObservableList<SummaryStatusRow> data = FXCollections.observableArrayList(rows);
        itemTable.setItems(data);

        itemTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && itemTable.getSelectionModel().getSelectedItem() != null) {
                SummaryStatusRow selected = itemTable.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/sheet_detail_view.fxml"));
                    Node view = loader.load();

                    SheetDetailController controller = loader.getController();
                    controller.loadDetails(selected.getTransformationId(), statusFilter);
                    UIController.setContent(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
