package com.youssef12347.exchange.interaction;

import com.youssef12347.exchange.Authentication;
import com.youssef12347.exchange.api.ExchangeService;
import com.youssef12347.exchange.api.model.ExchangeRates;
import com.youssef12347.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Interaction {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;

    public TextField lbp1TextField;
    public TextField usd1TextField;
    public TextField nameTextField;
    public ToggleGroup transactionType1;


    public void addexchange(ActionEvent actionEvent) {
        Transaction transaction = new Transaction(
                Float.parseFloat(usd1TextField.getText()),
                Float.parseFloat(lbp1TextField.getText()),
                ((RadioButton)
                        transactionType1.getSelectedToggle()).getText().equals("Sell USD"),
                nameTextField.getText()
        );

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addexchange(transaction,
                authHeader).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {
                Platform.runLater(() -> {
                    usd1TextField.setText("");
                    lbp1TextField.setText("");
                    nameTextField.setText("");
                });
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
            }
        });
    }
}