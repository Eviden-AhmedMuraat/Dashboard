package dashboard.controllers;

import dashboard.Model.TransformationDataStore;
import dashboard.Model.TransformationRecord;
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
import java.util.*;
import java.util.stream.Collectors;

public class ItemListController {

    @FXML private Label titleLabel;
    @FXML private TableView<ItemStatusCount> itemTable;
    @FXML private TableColumn<ItemStatusCount, String> colItemId;
    @FXML private TableColumn<ItemStatusCount, String> colItemRev;
    @FXML private TableColumn<ItemStatusCount, Integer> colStatusCount;

    public void loadItemsByStatus(String status) {
        titleLabel.setText("Items with status: " + status);

        Map<String, List<TransformationRecord>> grouped = TransformationDataStore.getRecords().stream()
                .filter(r -> r.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.groupingBy(r -> r.getItemId() + "|" + r.getItemRev()));

        List<ItemStatusCount> result = grouped.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("\\|");
                    String itemId = parts[0];
                    String itemRev = parts.length > 1 ? parts[1] : "";

                    long uniqueTransformations = entry.getValue().stream()
                            .map(TransformationRecord::getTransformationId)
                            .distinct()
                            .count();

                    return new ItemStatusCount(itemId, itemRev, (int) uniqueTransformations);
                })
                .sorted(Comparator.comparing(ItemStatusCount::getItemId))
                .collect(Collectors.toList());

        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemRev.setCellValueFactory(new PropertyValueFactory<>("itemRev"));
        colStatusCount.setCellValueFactory(new PropertyValueFactory<>("statusCount"));

        ObservableList<ItemStatusCount> data = FXCollections.observableArrayList(result);
        itemTable.setItems(data);

        itemTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && itemTable.getSelectionModel().getSelectedItem() != null) {
                ItemStatusCount selected = itemTable.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/sheet_list_view.fxml"));
                    Node view = loader.load();

                    SheetListController controller = loader.getController();
                    controller.loadSheetsForItem(selected.getItemId(), selected.getItemRev(), status);
                    UIController.setContent(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("📋 Items gevonden met status '" + status + "': " + result.size());
    }

    public static class ItemStatusCount {
        private final String itemId;
        private final String itemRev;
        private final int statusCount;

        public ItemStatusCount(String itemId, String itemRev, int statusCount) {
            this.itemId = itemId;
            this.itemRev = itemRev;
            this.statusCount = statusCount;
        }

        public String getItemId() {
            return itemId;
        }

        public String getItemRev() {
            return itemRev;
        }

        public int getStatusCount() {
            return statusCount;
        }
    }
}
