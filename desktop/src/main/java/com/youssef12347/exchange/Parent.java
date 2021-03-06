package com.youssef12347.exchange;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Parent implements Initializable, OnPageCompleteListener{
    public BorderPane borderPane;
    public Button transactionButton;
    public Button myexchangesButton;
    public Button statisticsButton;
    public Button exchangesButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNavigation();
    }

    public void ratesSelected() {
        swapContent(Section.RATES);
    }
    public void statsSelected() {
        swapContent(Section.STATS);
    }
    public void exchangesSelected() {
        swapContent(Section.EXCHANGES);
    }
    public void myexchangesSelected() {
        swapContent(Section.MYEXCHANGES);
    }
    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }
    public void loginSelected() {
        swapContent(Section.LOGIN);
    }
    public void registerSelected() {
        swapContent(Section.REGISTER);
    }
    public void logoutSelected() {
        Authentication.getInstance().deleteToken();
        swapContent(Section.RATES);
    }

    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
            if (section.doesComplete()) {
                ((PageCompleter)
                        loader.getController()).setOnPageCompleteListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateNavigation();
    }
    private void updateNavigation() {
        boolean authenticated = Authentication.getInstance().getToken() !=
                null;
        transactionButton.setManaged(authenticated);
        transactionButton.setVisible(authenticated);
        statisticsButton.setManaged(authenticated);
        statisticsButton.setVisible(authenticated);
        exchangesButton.setManaged(authenticated);
        exchangesButton.setVisible(authenticated);
        myexchangesButton.setManaged(authenticated);
        myexchangesButton.setVisible(authenticated);
        loginButton.setManaged(!authenticated);
        loginButton.setVisible(!authenticated);
        registerButton.setManaged(!authenticated);
        registerButton.setVisible(!authenticated);
        logoutButton.setManaged(authenticated);
        logoutButton.setVisible(authenticated);
    }
    @Override
    public void onPageCompleted() {
        swapContent(Section.RATES);
    }


    private enum Section {
        RATES,
        STATS,
        EXCHANGES,
        MYEXCHANGES,
        TRANSACTIONS,
        LOGIN,
        REGISTER;
        public String getResource() {
            return switch (this) {
                case RATES ->
                        "/com/youssef12347/exchange/rates/rates.fxml";
                case STATS ->
                        "/com/youssef12347/exchange/stats/stats.fxml";
                case EXCHANGES ->
                        "/com/youssef12347/exchange/exchanges/exchanges.fxml";
                case MYEXCHANGES ->
                        "/com/youssef12347/exchange/myexchanges/myexchanges.fxml";
                case TRANSACTIONS ->
                        "/com/youssef12347/exchange/transactions/transactions.fxml";
                case LOGIN ->
                        "/com/youssef12347/exchange/login/login.fxml";
                case REGISTER ->
                        "/com/youssef12347/exchange/register/register.fxml";
                default -> null;
            };
        }
        public boolean doesComplete() {
            return switch (this) {
                case LOGIN, REGISTER -> true;
                default -> false;
            };
        }



    }
}
