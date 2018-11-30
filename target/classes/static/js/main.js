var optionNames = [];
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
    let newOptionTrade = {
        "userName": $('#currentUsername').text(),
        "optionName": $(obj).parents('tr').find('#optionName').text(),
        "underlying": $(obj).parents('tr').find('#underlying').text(),
        "strikePrice": $(obj).parents('tr').find('#strikePrice').text(),
        "optionValue": $(obj).parents('tr').find('#optionValue').text(),
        "datetime": new Date(),
        "expire": $(obj).parents('tr').find('#expire').text(),
        "putCall": $(obj).parents('tr').find('#putCall').text(),
        "ameEur": $(obj).parents('tr').find('#ameEur').text()
    };
    console.log(newOptionTrade);
    if (newOptionTrade.strikePrice - balance > 0.0) {
        $('#successMsg').html('');
        $('#errorMsg').html("Error: You don't have sufficient balance to buy this option!");
        $('#buyOption').modal('show');
        return;
    }
    $.ajax({
        type: "POST",
        url: "/user/market/optionTrade",
        data: JSON.stringify(newOptionTrade),
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
    let newStockTrade = {
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
                newStockTrade.quantity = quantity;
                if(quantity * newStockTrade.stockPrice > data) {
                    console.log("You don't have sufficient money left.");
                    $('#errorMsg').html('You don\'t have sufficient money left.');
                    setTimeout(function () {
                        $('#buyStock').modal('hide');
                        $('#errorMsg').html("");
                    }, 2000);
                    return;
                }
            } else {
                newStockTrade.quantity = 0 - $("#quantity").val();
            }
            $.ajax({
                type: "POST",
                url: "/user/market/trade",
                data: JSON.stringify(newStockTrade),
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

function filterOption(obj) {
    let optionName = $('#selectedOption option:selected').val();
    let strHtml = '';
    $.ajax({
        type: "GET",
        url: "/user/history/filterOption",
        data: {"optionName": optionName},
        dataType: 'json',
        contentType:'application/json;charset=UTF-8',
        success: function (data) {
            $.each(data, function (index, item) {
                var datetimeStr = JSON.stringify(item.datetime);
                strHtml += '<tr><td>' + item.optionName + '</td>' + '<td>' + item.underlying + '</td>' + '<td>' + item.strikePrice + '</td>' + '<td>' +
                    datetimeStr.substr(1,10) + ' ' + datetimeStr.substr(12,10)
                    + '</td>' +  '<td>'+item.expire+'</td>' + '<td>'+item.putCall+'</td>' + '<td>'+item.ameEur+'</td></tr>';
            });
            $(".table-content").html(strHtml);
        },
        error: function (error) {
            console.log("Error: ", error);
        }
    });
}

function calculateDays(obj) {
    let exp = moment($(obj).parents("tr").find('#expiration').text());
    var today = moment(moment().format('YYYY-MM-DD'));
    // console.log(today);
    var days = exp.diff(today, 'days');
    if (days < 0) {
        $.ajax({
            type: "GET",
            url: "/user/option/deleteExpired",
            data: { "optionTradeId": $(obj).parents('tr').find('#id').text() },
            contentType:'application/json;charset=UTF-8',
            success: function (data) {
                console.log("Deleted expired option.");
                // location.reload();
                return;
            },
            error: function (error) {
                console.log("Error: ", error);
            }
        });
    }
    if(days < 40) {
        $(obj).addClass('expiring');
        $(obj).html('<b>'+days+'</b>');
    } else {
        $(obj).html(days);
    }

}

function exercise(obj) {
    let putCall = $(obj).parents('tr').find('#putCall').text();
    let ameEur = $(obj).parents('tr').find('#ameEur').text();
    let expire = $(obj).parents('tr').find('#expiration').text();
    let today = moment().format('YYYY-MM-DD');
    let stockName = $(obj).parents('tr').find('#underlying').text();
    console.log(putCall, ameEur, expire, today);
    // console.log(expire === m);
    if (ameEur === 'Eur') {
        if (expire === today) {
            if (putCall === 'Put') {
                $.ajax({
                    type: 'GET',
                    url: '/user/stockportfolio/quantity',
                    data: {'stockName': stockName},
                    dataType: 'json',
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data === 0) {
                            $('#successMsg').html('');
                            $('#errorMsg').html("You don't have stock " + stockName + ", go to Stock Market to buy one.");
                            $('#exercise').modal('show');
                            return;
                        }
                        doExercise(obj);
                    },
                    error: function (error) {
                        console.log("Error: ", error);

                    }
                });
            }
            if (putCall === 'Call') doExercise(obj);
        } else {
            $('#successMsg').html('');
            $('#errorMsg').html('Exercise failed! You can only exercise when it"s expiring.');
        }

    }
    if (ameEur === 'Ame') {
        if (putCall === 'Put') {
            $.ajax({
                type: 'GET',
                url: '/user/stockportfolio/quantity',
                data: {'stockName': stockName},
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data === 0) {
                        $('#successMsg').html('');
                        $('#errorMsg').html("You don't have stock " + stockName + ", go to Stock Market to buy one.");
                        $('#exercise').modal('show');
                        return;
                    }
                    doExercise(obj);
                },
                error: function (error) {
                    console.log("Error: ", error);
                }
            });
        }
        if (putCall === 'Call') doExercise(obj);
    }


}

function doExercise(obj){
    let id, strikePrice, stockName, putCall;
    id = $(obj).parents('tr').find('#id').text();
    strikePrice = $(obj).parents('tr').find('#strikePrice').text();
    stockName = $(obj).parents('tr').find('#underlying').text();
    putCall = $(obj).parents('tr').find('#putCall').text();

    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: "/user/market/getStockPrice",
        data: {"stockName": stockName },
        success: function (stockPrice) {
            if (putCall === 'Put') {
                if (strikePrice > stockPrice) {
                    $('#successMsg').html('');
                    $('#errorMsg').html('The strike price is greater than stock price. You cannot Put.');
                    $('#exercise').modal('show');
                    return;
                }
                else doExercise2(id, putCall);
            }
            if (putCall === 'Call') {
                console.log('doExercise ', strikePrice, stockPrice, id)
                if (strikePrice < stockPrice) {
                    $('#successMsg').html('');
                    $('#errorMsg').html('The strike price is less than stock price. You cannot Call');
                    $('#exercise').modal('show');
                    return;
                }
                else if ($('#balance') < strikePrice) {
                    $('#successMsg').html('');
                    $('#errorMsg').html('You don"t have sufficient balance. You cannot Call');
                    $('#exercise').modal('show');
                    return;
                }
                else{
                    doExercise2(id, putCall);
                }
            }
        },
        error: function (error) {
            console.log("Error: ", error);
        }
    });
}

function doExercise2(id, putCall) {
    console.log(id, putCall);
    $.ajax({
        type: "GET",
        url: "/user/exercise",
        contentType: "application/json; charset=utf-8",
        data: { "tradeId": id,
                "putCall": putCall },
        success: function (data) {
            $('#successMsg').html('Success!');
            $('#errorMsg').html('');
            $('#exercise').modal('show');
            getBalance();
            setTimeout(location.reload(), 2000);
        },
        error: function (error) {
            console.log("Exercise failed!", error);
            $('#successMsg').html('');
            $('#errorMsg').html('Exercise failed!');
            $('#exercise').modal('show');
        }
    });
}


function checkQuantity(obj) {
    let optionName = $(obj).parents('tr').find('#optionName').text();
    if (optionNames.includes(optionName))  {
        $(obj).closest('tr').remove();
        return;
    }
    optionNames.push(optionName);
    $.ajax({
        type: "GET",
        url: "/user/optionportfolio/quantity",
        contentType: "application/json; charset=utf-8",
        data: { "optionName": optionName },
        success: function (data) {
            // console.log("Quantity of option ", optionName, " = ", data);
            if (data <= 0) {
                // console.log("Current row is deleted.");
                $(obj).closest('tr').remove();
            }
        },
        error: function (error) {
            console.log("Error: ", error);
        }
    });
}
