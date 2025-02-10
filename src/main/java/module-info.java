module in.corpore.team.todaytomorrow {
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
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;

    opens in.corpore.team.todaytomorrow to javafx.fxml;
    exports in.corpore.team.todaytomorrow;
}