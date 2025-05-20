package dashboard.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class    UIController {

    @FXML private StackPane contentPane;
    private static StackPane staticContentPane;

    @FXML
    private void initialize() {
        staticContentPane = contentPane;
        showExcelImport();
    }

    @FXML
    private void showExcelImport() {
        loadView("/dashboard/excel_import.fxml");
    }

    @FXML
    private void showKPI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/kpi_view.fxml"));
            Node view = loader.load();

            KPIController controller = loader.getController();
            controller.refreshData();

            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setContent(Node view) {
        if (staticContentPane != null) {
            staticContentPane.getChildren().setAll(view);
        } else {
            System.err.println("⚠️ contentPane is null (UIController)");
        }
    }

    public void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
