module com.javafx.csit228capstone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires com.google.zxing;
    requires java.desktop;
    requires javafx.swing;
    requires com.google.zxing.javase;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.javafx.csit228capstone to javafx.fxml;
    exports com.javafx.csit228capstone;
    exports com.javafx.csit228capstone.screens;
    opens com.javafx.csit228capstone.screens to javafx.fxml;
    opens com.javafx.csit228capstone.screens.admin to javafx.fxml;
    exports com.javafx.csit228capstone.helper;
    opens com.javafx.csit228capstone.helper to javafx.fxml;
    exports com.javafx.csit228capstone.screens.queue;
    opens com.javafx.csit228capstone.screens.queue to javafx.fxml;
    exports com.javafx.csit228capstone.screens.Account;
    opens com.javafx.csit228capstone.screens.Account to javafx.fxml;
    exports com.javafx.csit228capstone.model;
    opens com.javafx.csit228capstone.model to javafx.fxml;
    exports com.javafx.csit228capstone.screens.schedule;
    opens com.javafx.csit228capstone.screens.schedule to javafx.fxml;
}