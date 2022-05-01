package com.youssef12347.exchange.myexchanges;

import com.youssef12347.exchange.Authentication;
import com.youssef12347.exchange.api.ExchangeService;
import com.youssef12347.exchange.api.model.Interactions;
import com.youssef12347.exchange.api.model.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyExchanges implements Initializable {
    public TableColumn receiverUsername;
    public TableColumn usdAmount;
    public TableColumn lbpAmount;
    public TableColumn usdToLbp;
    public TableColumn addedDate;
    public TableColumn receiverUsernameR;
    public TableColumn usdAmountR;
    public TableColumn lbpAmountR;
    public TableColumn usdToLbpR;
    public TableColumn addedDateR;
    public TableView tableView;
    public TableView tableViewR;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        receiverUsername.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("receiverUsername"));
        usdAmount.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("usdAmount"));
        lbpAmount.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("lbpAmount"));
        usdToLbp.setCellValueFactory(new
                PropertyValueFactory<Transaction, Boolean>("usdToLbp"));
        addedDate.setCellValueFactory(new
                PropertyValueFactory<Transaction, String>("addedDate"));

        receiverUsernameR.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("receiverUsername"));
        usdAmountR.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("usdAmount"));
        lbpAmountR.setCellValueFactory(new
                PropertyValueFactory<Transaction, Long>("lbpAmount"));
        usdToLbpR.setCellValueFactory(new
                PropertyValueFactory<Transaction, Boolean>("usdToLbp"));
        addedDateR.setCellValueFactory(new
                PropertyValueFactory<Transaction, String>("addedDate"));


        ExchangeService.exchangeApi().getInteractions("Bearer " +
                Authentication.getInstance().getToken()).enqueue(new Callback<Interactions>() {
            @Override
            public void onResponse(Call<Interactions> call, Response<Interactions> response) {

                tableView.getItems().setAll(response.body().giveList);
                tableViewR.getItems().setAll(response.body().receiveList);

            }
            @Override
            public void onFailure(Call<Interactions> call,
                                  Throwable throwable) {
            }
        });
    }
}
