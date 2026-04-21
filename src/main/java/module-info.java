module com.javafx.csit228capstone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.javafx.csit228capstone to javafx.fxml;
    exports com.javafx.csit228capstone;
    exports com.javafx.csit228capstone.login;
    opens com.javafx.csit228capstone.login to javafx.fxml;
    exports com.javafx.csit228capstone.dashboard;
    opens com.javafx.csit228capstone.dashboard to javafx.fxml;
}