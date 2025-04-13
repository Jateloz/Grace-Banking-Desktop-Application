module com.example.jatelobank {
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
    requires java.logging;
    requires javafx.graphics;
    requires javafx.media;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.beans;
    requires com.zaxxer.hikari;
    requires spring.web;
    requires static lombok;
    requires Java.WebSocket;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires spring.websocket;
    requires jakarta.persistence;
    requires javafx.swing;
    requires spring.tx;
    requires spring.aop;
    requires org.apache.pdfbox;
    requires org.antlr.antlr4.runtime;
    requires vosk;
    requires org.jsoup;


    opens com.example.jatelobank to javafx.fxml, spring.core, spring.beans, spring.context, spring.data.jpa, spring.boot, spring.boot.autoconfigure, spring.aop;
    opens com.example.jatelobank.ActivityWindow to javafx.fxml;
    opens com.example.jatelobank.Admin;
    exports com.example.jatelobank;
    exports com.example.jatelobank.ActivityWindow;
    exports com.example.jatelobank.Settings;
    exports com.example.jatelobank.Settings.Accounts;
    exports com.example.jatelobank.WebSocket;
    exports com.example.jatelobank.AllExpenseIncome;
    exports com.example.jatelobank.Admin;
    exports com.example.jatelobank.Documents;
    exports com.example.jatelobank.ActivityWindow.Reports;



}