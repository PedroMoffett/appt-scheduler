module moffett.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens moffett.scheduler to javafx.fxml;
    exports moffett.scheduler;
    exports moffett.scheduler.controller;
    opens moffett.scheduler.controller to javafx.fxml;
    opens moffett.scheduler.model to javafx.base;
    opens moffett.scheduler.helper to javafx.base;


}