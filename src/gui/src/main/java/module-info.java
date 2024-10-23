module com.example {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.myapp to javafx.fxml;
    exports com.myapp;
}
