module com.mycompany.crudcomandasjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    
    opens com.mycompany.crudcomandasjfx to javafx.fxml, org.hibernate.orm.core, java.sql;
    
    exports com.mycompany.crudcomandasjfx;
}
