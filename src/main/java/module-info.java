module com.javafx.csit228capstone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.javafx.csit228capstone to javafx.fxml;
    exports com.javafx.csit228capstone;
    exports com.javafx.csit228capstone.screens;
    opens com.javafx.csit228capstone.screens to javafx.fxml;
    exports com.javafx.csit228capstone.helper;
    opens com.javafx.csit228capstone.helper to javafx.fxml;
    exports com.javafx.csit228capstone.screens.queue;
    opens com.javafx.csit228capstone.screens.queue to javafx.fxml;
}