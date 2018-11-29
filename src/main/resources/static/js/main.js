
//
// function fire_ajax_submit() {
//     var params = {
//         "bondName": $("#calculateByBond").val()
//     };
//     $("#btn_calculate").prop("disabled", true);
//     $.ajax({
//         type: "GET",
//         contentType: "application/json; charset=utf-8",
//         url: "/user/market/calculateYield",
//         data: params,
//         success: function (data) {
//             console.log(data);
//             $("#display_yield").html(data);
//             $("#btn_calculate").prop("disabled", false);
//         },
//         error: function (error) {
//             console.log("Error", error);
//             $("#display_yield").html(error);
//             $("#btn_calculate").prop("disabled", false);
//         }
//     });
// }


function calculate(method) {
    var params = {};
    if (method === 'bond') {
        params.bondName = $("#calculateByYield").val();
        params.method = 'bond';
        params.yield = $('#yield_value').val();
        params.couponValue1=$('#couponValue1').val();
        params.bondValue = '0';
        params.couponValue = '0';


    } else if (method === 'yield') {
        params.bondName = $('#calculateByBond').val();
        params.method = 'yield';
        params.bondValue = $('#bond_value').val();
        params.couponValue=$('#couponValue').val();
        params.yield = '0';
        params.couponValue1 = '0';

    }
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "/user/market/calculate",
        data: params,
        success: function (data) {
            console.log(data);
            if (method === 'bond')
                $("#display_bond").html(data);
            else if (method === 'yield')
                $("#display_yield").html(data);
        },
        error: function (error) {
            console.log("Error", error);
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

function buyOption(obj) {
    let balance = $('#balance').text();
    let newTrade = {
        "userName": $('#currentUsername').text(),
        "optionName": $(obj).parents('tr').find('#optionName').text(),
        "underlying": $(obj).parents('tr').find('#underlying').text(),
        "strikePrice": $(obj).parents('tr').find('#strikePrice').text(),
        "datetime": new Date(),
        "expire": $(obj).parents('tr').find('#expire').text(),
        "putCall": $(obj).parents('tr').find('#putCall').text(),
        "ameEur": $(obj).parents('tr').find('#ameEur').text()
    };
    console.log(newTrade);
    if (newTrade.strikePrice - balance > 0.0) {
        $('#successMsg').html('');
        $('#errorMsg').html("Error: You don't have sufficient balance to buy this option!");
        $('#buyOption').modal('show');
        return;
    }
    $.ajax({
        type: "POST",
        url: "/user/market/optionTrade",
        data: JSON.stringify(newTrade),
        contentType:'application/json;charset=UTF-8',
        success: function (data) {
            $('#errorMsg').html('');
            $('#successMsg').html('Success!');
            $('#buyOption').modal('show');
            getBalance();
        },
        error: function (error) {
            console.log("Error!", error);
        }
    });
}

function buyBond(obj) {
    let balance = $('#balance').text();
    let buyNewBond = {
        "userName": $('#currentUsername').text(),
        "bondName": $(obj).parents('tr').find('#bondName').text(),
        "value": $(obj).parents('tr').find('#value').text(),
        "coupon": $(obj).parents('tr').find('#coupon').text(),
        "issuedOn": $(obj).parents('tr').find('#issuedOn').text(),
        "maturityLength": $(obj).parents('tr').find('#length').text(),
        "datetime": new Date()
    };
    if (balance - buyNewBond.value < 0) {
        $('#successMsg').html('');
        $('#errorMsg').html("Error: You don't have sufficient balance to buy this bond!");
        $('#buyBond').modal('show');
        return;
    }
    $.ajax({
        type: "POST",
        url: "/user/market/bondTrade",
        data: JSON.stringify(buyNewBond),
        contentType:'application/json;charset=UTF-8',
        success: function (data) {
            $('#errorMsg').html('');
            $('#successMsg').html('Success!');
            $('#buyBond').modal('show');
            getBalance();
        },
        error: function (error) {
            console.log('Error: ', error);
        }
    });
}

function trade(obj, buySell) {
    let newTrade = {
        "userName": $("#currentUsername").text(),
        "stockName": $("#currentStockName").text(),
        "stockPrice": $("#currentPrice").text(),
        "datetime": new Date()
    };
    $.ajax({
        type: "GET",
        url: "/user/market/getBalance",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (buySell === true) {
                var quantity = $("#quantity").val();
                newTrade.quantity = quantity;
                if(quantity * newTrade.stockPrice > data) {
                    console.log("You don't have sufficient money left.");
                    $('#errorMsg').html('You don\'t have sufficient money left.');
                    setTimeout(function () {
                        $('#buyStock').modal('hide');
                        $('#errorMsg').html("");
                    }, 2000);
                    return;
                }
            } else {
                newTrade.quantity = 0 - $("#quantity").val();
            }
            $.ajax({
                type: "POST",
                url: "/user/market/trade",
                data: JSON.stringify(newTrade),
                contentType:'application/json;charset=UTF-8',
                success: function (data) {
                    console.log("Success!");
                    $('#buyStock').modal('hide');
                    getBalance();
                },
                error: function (error) {
                    console.log("Error!", error);
                }
            });

        },
        error: function(error) {
            console.log(error);
        }
    });

}

function getBalance() {
    let balance = 0.0;
    $.ajax({
        type: "GET",
        url: "/user/market/getBalance",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $('#balance').html(data);
            },
        error: function(error) {
            console.log(error);
            }
    });
    return balance;
}

function filter(obj) {
    let stockName = $('#selectedStock option:selected').val();
    let strHtml = '';
    $.ajax({
        type: "GET",
        url: "/user/history/filter",
        data: {"stockName": stockName},
        dataType: 'json',
        contentType:'application/json;charset=UTF-8',
        success: function (data) {
            $.each(data, function (index, item) {
                var datetimeStr = JSON.stringify(item.datetime);
                strHtml += '<tr><td>' + item.stockName + '</td>' + '<td>' + item.stockPrice + '</td>' + '<td>' +
                    datetimeStr.substr(1,10) + ' ' + datetimeStr.substr(12,10)
                    + '</td>' + '<td>' + item.quantity + '</td></tr>';
            });
            $(".table-content").html(strHtml);
        },
        error: function (error) {
            console.log("Error: ", error);
        }
    });
}

function calculateDays(obj) {
    let exp = $(obj).parents("tr").find('#expiration').text();
    console.log(exp);
}