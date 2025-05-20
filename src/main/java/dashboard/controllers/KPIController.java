package dashboard.controllers;

import dashboard.Model.TransformationDataStore;
import dashboard.Model.TransformationSummary;
import dashboard.Model.TransformationRecord;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class KPIController {

    @FXML private VBox kpiRoot;
    @FXML private HBox kpiContainer;

    @FXML
    public void initialize() {
        refreshData();
    }

    public void refreshData() {
        var summaries = TransformationDataStore.getSummaries();
        var records = TransformationDataStore.getRecords();

        int totalSuccess = summaries.stream().mapToInt(TransformationSummary::getSuccessCount).sum();
        int totalWarning = summaries.stream().mapToInt(TransformationSummary::getWarningCount).sum();
        int totalError   = summaries.stream().mapToInt(TransformationSummary::getErrorCount).sum();

        // unieque combinaties van itemId|itemRev
        Map<String, Long> statusToUniqueItems = records.stream()
                .collect(Collectors.groupingBy(
                        TransformationRecord::getStatus,
                        Collectors.mapping(r -> r.getItemId() + "|" + r.getItemRev(), Collectors.toSet())
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()));

        long uniqueSuccess = statusToUniqueItems.getOrDefault("Success", 0L);
        long uniqueWarning = statusToUniqueItems.getOrDefault("Warning", 0L);
        long uniqueError   = statusToUniqueItems.getOrDefault("Error", 0L);

        kpiContainer.getChildren().clear();
        kpiRoot.getChildren().removeIf(node -> node instanceof PieChart);

        VBox successBox = createKpiBox("✅ Success", totalSuccess, uniqueSuccess, "#28a745", "Success");
        VBox warningBox = createKpiBox("⚠️ Warnings", totalWarning, uniqueWarning, "#ffc107", "Warning");
        VBox errorBox   = createKpiBox("❌ Errors", totalError, uniqueError, "#dc3545", "Error");

        kpiContainer.getChildren().addAll(successBox, warningBox, errorBox);

        PieChart pieChart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Success", totalSuccess),
                new PieChart.Data("Warning", totalWarning),
                new PieChart.Data("Error", totalError)
        ));
        pieChart.setTitle("Record Distribution");

        pieChart.getStylesheets().add(getClass().getResource("/css/chart.css").toExternalForm());
        kpiRoot.getChildren().add(pieChart);

    }

    private VBox createKpiBox(String title, int total, long uniqueItems, String color, String statusFilter) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label totalLabel = new Label("Total: " + total);
        totalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        Label uniqueLabel = new Label("Unique Items: " + uniqueItems);
        uniqueLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        VBox box = new VBox(6, titleLabel, totalLabel, uniqueLabel);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(200, 100);
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");

        box.setOnMouseClicked(e -> openItemListView(statusFilter));
        return box;
    }

    private void openItemListView(String status) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/item_list_view.fxml"));
            Node view = loader.load();
            dashboard.controllers.ItemListController controller = loader.getController();
            controller.loadItemsByStatus(status);
            kpiRoot.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Kon itemlijst niet laden.").showAndWait();
        }
    }
}
