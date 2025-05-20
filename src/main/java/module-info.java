module org.example.dashboardtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens dashboard to javafx.fxml;
    exports dashboard;
    exports dashboard.Model;
    opens dashboard.Model to javafx.fxml;
    exports dashboard.controllers;
    opens dashboard.controllers to javafx.fxml;
}