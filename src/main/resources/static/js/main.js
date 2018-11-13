console.log("Hello, ");

$(document).ready(function () {
   $("#calculate_yield").submit(function (event) {
       //stop submit the form, we will post it manually
       event.preventDefault();
       fire_ajax_submit();
   });
});

function fire_ajax_submit() {
    var params = {
        "bondName": $("#calculateByBond").val(),
        "bondValue": $("#bond_value").val()
    };
    $("#btn_calculate").prop("disabled", true);
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "/user/market/calculateYield",
        data: params,
        success: function (data) {
            console.log(data);
            $("#display_yield").html(data);
            $("#btn_calculate").prop("disabled", false);
        },
        error: function (error) {
            console.log("Error", error);
            $("#display_yield").html(error);
            $("#btn_calculate").prop("disabled", false);
        }
    });
}

function getCurrentPrice(obj) {
    var stockName = $(obj).parents("tr").find("#stockName").text();
    $("#currentStockName").html(stockName);
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "/user/market/getStockPrice",
        data: {"stockName": stockName },
        success: function (data) {
            console.log("Current price of ", stockName , " is: ", data);
            $("#currentPrice").html(data);
        },
        error: function (error) {
            console.log("Error: ", error);
        }
    });
}

function trade(obj, buySell) {
    var newTrade = {
        "userName": $("#currentUsername").text(),
        "stockName": $("#currentStockName").text(),
        "stockPrice": $("#currentPrice").text(),
        "datetime": new Date()
    };
    if (buySell === true) {
        newTrade.quantity = $("#quantity").val();
    } else {
        newTrade.quantity = 0 - $("#quantity").val();
    }
    console.log("Buy a new stock: ", newTrade);
    $.ajax({
        type: "POST",
        url: "/user/market/trade",
        data: JSON.stringify(newTrade),
        contentType:'application/json;charset=UTF-8',
        success: function (data) {
            console.log("Success!");
        },
        error: function (error) {
            console.log("Error!");
        }
    });
}
