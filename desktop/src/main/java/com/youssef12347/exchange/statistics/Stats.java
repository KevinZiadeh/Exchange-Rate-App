package com.youssef12347.exchange.statistics;

import com.youssef12347.exchange.Authentication;
import com.youssef12347.exchange.api.ExchangeService;
import com.youssef12347.exchange.api.model.ExchangeRates;
import com.youssef12347.exchange.api.model.Graph;
import com.youssef12347.exchange.api.model.Statistics;
import com.youssef12347.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stats {
    public Label maxlbptousd;
    public Label maxusdtolbp;
    public Label medianlbptousd;
    public Label medianusdtolbp;
    public Label minlbptousd;
    public Label minusdtolbp;
    public Label modelbptousd;
    public Label modeusdtolbp;
    public Label predictedlbptousd;
    public Label predictedusdtolbp;
    public Label stdlbptousd;
    public Label stdusdtolbp;
    @FXML LineChart<String, Number> lineChart;
    @FXML LineChart<String, Number> lineChart2;


    private float day1BuyUSD;
    private float day1SellUSD;
    private float day2BuyUSD;
    private float day2SellUSD;
    private float day3BuyUSD;
    private float day3SellUSD;
    private float day4BuyUSD;
    private float day4SellUSD;
    private float day5BuyUSD;
    private float day5SellUSD;
    private float day6BuyUSD;
    private float day6SellUSD;
    private float day7BuyUSD;
    private float day7SellUSD;
    private float day8BuyUSD;
    private float day8SellUSD;
    private float day9BuyUSD;
    private float day9SellUSD;
    private float day10BuyUSD;
    private float day10SellUSD;

    private String day1;
    private String day2;
    private String day3;
    private String day4;
    private String day5;
    private String day6;
    private String day7;
    private String day8;
    private String day9;
    private String day10;


    public void initialize() {
        fetchStats();
        fetchGraphDetails();
    }


    private void fetchStats(){
        ExchangeService.exchangeApi().getStatistics().enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call,
                                   Response<Statistics> response) {
                Statistics statistics = response.body();
                Platform.runLater(() -> {

                    maxlbptousd.setText(statistics.max_lbp_to_usd.toString());

                    maxusdtolbp.setText(statistics.max_usd_to_lbp.toString());

                    medianlbptousd.setText(statistics.median_lbp_to_usd.toString());

                    medianusdtolbp.setText(statistics.median_usd_to_lbp.toString());

                    minlbptousd.setText(statistics.min_lbp_to_usd.toString());

                    minusdtolbp.setText(statistics.min_usd_to_lbp.toString());

                    modelbptousd.setText(statistics.mode_lbp_to_usd.toString());

                    modeusdtolbp.setText(statistics.mode_usd_to_lbp.toString());

                    predictedlbptousd.setText(statistics.predicted_lbp_to_usd.toString());

                    predictedusdtolbp.setText(statistics.predicted_usd_to_lbp.toString());

                    stdlbptousd.setText(statistics.std_lbp_to_usd.toString());

                    stdusdtolbp.setText(statistics.std_usd_to_lbp.toString());


                });
            }
            @Override
            public void onFailure(Call<Statistics> call, Throwable
                    throwable) {
            }
        });
    }

    private void fetchGraphDetails() {

        ExchangeService.exchangeApi().getGraph().enqueue(new
             Callback<Graph>() {
                 @Override
                 public void onResponse(Call<Graph> call,
                                        Response<Graph> response) {
                     Graph graph = response.body();
                     Platform.runLater(() -> {
                         day1BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(0).toString());
                         day1SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(0).toString());
                         day2BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(1).toString());
                         day2SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(1).toString());
                         day3BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(2).toString());
                         day3SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(2).toString());
                         day4BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(3).toString());
                         day4SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(3).toString());
                         day5BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(4).toString());
                         day5SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(4).toString());
                         day6BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(5).toString());
                         day6SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(5).toString());
                         day7BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(6).toString());
                         day7SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(6).toString());
                         day8BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(7).toString());
                         day8SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(7).toString());
                         day9BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(8).toString());
                         day9SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(8).toString());
                         day10BuyUSD = Float.parseFloat(graph.mean_rate_lbp_to_usd.get(9).toString());
                         day10SellUSD = Float.parseFloat(graph.mean_rate_usd_to_lbp.get(9).toString());

                         day1 = graph.date.get(0).toString();
                         day2 = graph.date.get(1).toString();
                         day3 = graph.date.get(2).toString();
                         day4 = graph.date.get(3).toString();
                         day5 = graph.date.get(4).toString();
                         day6 = graph.date.get(5).toString();
                         day7 = graph.date.get(6).toString();
                         day8 = graph.date.get(7).toString();
                         day9 = graph.date.get(8).toString();
                         day10 = graph.date.get(9).toString();

                     });
                 }

                 @Override
                 public void onFailure(Call<Graph> call, Throwable
                         throwable) {
                 }
             });
    }

    public void fetchGraph(){

                    lineChart.getData().clear();

                    XYChart.Series<String, Number> series = new  XYChart.Series<String, Number>();
                    series.getData().add(new XYChart.Data<String, Number>(day1, day1BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day2, day2BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day3, day3BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day4, day4BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day5, day5BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day6, day6BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day7, day7BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day8, day8BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day9, day9BuyUSD));
                    series.getData().add(new XYChart.Data<String, Number>(day10, day10BuyUSD));

                    series.setName("Buy USD");



                    lineChart.getData().addAll(series);


                };
    public void fetchGraph2(){
        lineChart2.getData().clear();
        XYChart.Series<String, Number> series2 = new  XYChart.Series<String, Number>();
        series2.getData().add(new XYChart.Data<String, Number>(day1, day1SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day2, day2SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day3, day3SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day4, day4SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day5, day5SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day6, day6SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day7, day7SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day8, day8SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day9, day9SellUSD));
        series2.getData().add(new XYChart.Data<String, Number>(day10, day10SellUSD));

        series2.setName("Sell USD");
        lineChart2.getData().addAll(series2);

    }

}