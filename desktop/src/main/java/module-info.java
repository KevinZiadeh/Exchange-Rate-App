
module com.youssef12347.exchange {
        requires javafx.controls;
        requires javafx.fxml;
        requires retrofit2;
        requires java.sql;
        requires gson;
        requires retrofit2.converter.gson;
        requires java.prefs;
        opens com.youssef12347.exchange to javafx.fxml;
        opens com.youssef12347.exchange.api to gson;
        exports com.youssef12347.exchange;
    opens com.youssef12347.exchange.api.model to javafx.base, gson;
    exports com.youssef12347.exchange.rates;
    opens com.youssef12347.exchange.rates to javafx.fxml;
        exports com.youssef12347.exchange.login;
        opens com.youssef12347.exchange.login to javafx.fxml;
        opens com.youssef12347.exchange.register to javafx.fxml;
 opens com.youssef12347.exchange.transactions to javafx.fxml;
    opens com.youssef12347.exchange.statistics to javafx.fxml;

}
