module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.myapp to javafx.fxml;
    exports com.myapp;
}
