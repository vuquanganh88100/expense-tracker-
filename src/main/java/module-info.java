module com.example.ltnc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    opens com.example.ltnc to javafx.fxml;
    opens com.example.ltnc.Entity.Category to javafx.base;
    opens com.example.ltnc.Entity to javafx.base;
    exports com.example.ltnc;
//    exports com.example.ltnc.Controller;
//    opens com.example.ltnc.Controller to javafx.fxml;
}