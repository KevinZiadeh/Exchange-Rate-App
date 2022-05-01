package com.youssef12347.exchange.rates;


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

public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public Label answerusd2;
    public Label answerlbp2;

    public TextField lbpTextField;
    public TextField usdTextField;
    public ToggleGroup transactionType;


    public TextField conamount;
    public TextField conamount2;

    public void initialize() {
        fetchRates();
    }

    private void fetchRates(){
    ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
     @Override
     public void onResponse(Call<ExchangeRates> call,
                            Response<ExchangeRates> response) {
         ExchangeRates exchangeRates = response.body();
         Platform.runLater(() -> {

             buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());

             sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());


         });
     }
     @Override
     public void onFailure(Call<ExchangeRates> call, Throwable
             throwable) {
     }
    });
}

    public void addTransaction(ActionEvent actionEvent) {
        Transaction transaction = new Transaction(
                Float.parseFloat(usdTextField.getText()),
                Float.parseFloat(lbpTextField.getText()),
                ((RadioButton)
                        transactionType.getSelectedToggle()).getText().equals("Sell USD"),
                null
        );

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addTransaction(transaction,
                authHeader).enqueue(new Callback<Object>() {
              @Override
              public void onResponse(Call<Object> call, Response<Object>
                      response) {
                  fetchRates();
                  Platform.runLater(() -> {
                      usdTextField.setText("");
                      lbpTextField.setText("");
                  });
              }
              @Override
              public void onFailure(Call<Object> call, Throwable throwable)
              {
              }
          });
    }

    public void convert(){
        var x = Float.parseFloat(conamount.getText());
        var y = Float.parseFloat(buyUsdRateLabel.getText());
        var x1 = x * y;
        answerusd2.setText(String.valueOf(x1));

    }

    public void convert1(){
        var x2 = Float.parseFloat(conamount2.getText());
        var y2 = Float.parseFloat(sellUsdRateLabel.getText());
        var x3 = x2 / y2;
        answerlbp2.setText(String.valueOf(x3));
    }

}