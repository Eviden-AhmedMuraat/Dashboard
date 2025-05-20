package dashboard.controllers;

import dashboard.Model.TransformationDataStore;
import dashboard.Model.TransformationRecord;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SheetListController {

    @FXML private Label sheetListTitle;
    @FXML private ListView<String> sheetListView;

    private String itemId;
    private String itemRev;
    private String statusFilter;

    public void loadSheetsForItem(String itemId, String itemRev, String statusFilter) {
        this.itemId = itemId;
        this.itemRev = itemRev;
        this.statusFilter = statusFilter;

        sheetListTitle.setText("Transformations for item: " + itemId + " Rev: " + itemRev + " (" + statusFilter + ")");

        List<TransformationRecord> filtered = TransformationDataStore.getRecords().stream()
                .filter(r -> r.getItemId().equals(itemId))
                .filter(r -> r.getItemRev().equals(itemRev))
                .filter(r -> r.getStatus().equalsIgnoreCase(statusFilter))
                .collect(Collectors.toList());

        // verzamel unieke TXXX-prefixen, zoals T009
        Set<String> transformationPrefixes = filtered.stream()
                .map(r -> r.getTransformationId().substring(0, 4)) // e.g. T009
                .collect(Collectors.toCollection(TreeSet::new));

        sheetListView.setItems(FXCollections.observableArrayList(transformationPrefixes));

        sheetListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !sheetListView.getSelectionModel().isEmpty()) {
                String selectedPrefix = sheetListView.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/sheet_detail_view.fxml"));
                    Node view = loader.load();

                    SheetDetailController controller = loader.getController();
                    controller.loadDetails(itemId, itemRev, selectedPrefix, statusFilter);
                    UIController.setContent(view);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Je kan hier eventueel een alert tonen
                }
            }
        });
    }
}
