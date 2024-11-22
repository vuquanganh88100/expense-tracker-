module com.example.ltnc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.ltnc to javafx.fxml;
    opens com.example.ltnc.Entity.Category to javafx.base;

    exports com.example.ltnc;
}