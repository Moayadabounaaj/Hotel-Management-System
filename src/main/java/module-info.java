module de.zuse.hotel
{
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.controlsfx.controls;


    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.prefs;
	requires javafx.graphics;
	requires javafx.base;


    opens de.zuse.hotel.gui to javafx.fxml, javafx.graphics, com.fasterxml.jackson.dataformat.yaml, com.fasterxml.jackson.databind;
    opens de.zuse.hotel.core to com.fasterxml.jackson.dataformat.yaml, com.fasterxml.jackson.databind,
            org.hibernate.orm.core, javafx.base;

    opens de.zuse.hotel.db to org.hibernate.orm.core;


    exports de.zuse.hotel.util;
    exports de.zuse.hotel.util.pdf;
}