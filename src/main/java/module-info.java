module test {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires javafx.graphics;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;

  opens tp_project.client;
}