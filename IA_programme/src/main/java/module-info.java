module ib.ia_programme {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens ib.ia_programme to javafx.fxml;
    exports ib.ia_programme;
    exports ib.ia_programme.controllers;
    opens ib.ia_programme.controllers to javafx.fxml;
    exports ib.ia_programme.others;
    opens ib.ia_programme.others to javafx.fxml;
    exports ib.ia_programme.util;
    opens ib.ia_programme.util to javafx.fxml;
}