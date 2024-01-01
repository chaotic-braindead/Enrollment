module plm.rafaeltorres.irregularenrollmentsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires spring.security.crypto;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.material2;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.elusive;
    requires com.dlsc.formsfx;
    requires java.desktop;
    requires kernel;
    requires layout;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j;
    requires org.controlsfx.controls;
    requires io;

    opens plm.rafaeltorres.irregularenrollmentsystem to javafx.fxml;
    exports plm.rafaeltorres.irregularenrollmentsystem;
    exports plm.rafaeltorres.irregularenrollmentsystem.controllers;
    opens plm.rafaeltorres.irregularenrollmentsystem.controllers to javafx.fxml;

    opens plm.rafaeltorres.irregularenrollmentsystem.model to javafx.base;

}