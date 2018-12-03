


function returnCoupon() {
    let today = moment();
    let balance = $('#balance').text();
    $.ajax({
        type: "GET",
        url: "/user/bondTrades",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            data.forEach(function (bondTrade) {
                console.log(bondTrade);
                let issuedOn = moment(bondTrade.issuedOn);
                let boughtOn = moment(bondTrade.datetime);
                let monthsFromBought = boughtOn.diff(issuedOn, 'months');
                let monthsFromNow = today.diff(issuedOn, 'months');
                console.log(bondTrade);
                // console.log(boughtOn, today, monthsFromBought, monthsFromNow, bondTrade.returned);
                if (Math.floor(monthsFromNow / 6) - Math.floor(monthsFromBought / 6) > bondTrade.returned) {

                    let couponDates = [];
                    let couponTimes = bondTrade.maturityLength * 2;
                    for (var i=1; i<=couponTimes; i++) {
                        couponDates.push(moment(issuedOn).add(6*i, 'M').format('MM-DD-YYYY'));
                    }
                    couponDates.forEach(function (dateItem, index) {
                        // console.log(dateItem, monthsFromNow, monthsFromBought, bondTrade.returned);
                        if (dateItem === moment(today).format('MM-DD-YYYY')) {
                            if (index === couponDates.length-1) {
                                redeemCoupon(bondTrade.id, true);
                            }
                            else {
                                redeemCoupon(bondTrade.id, false);
                            }
                        }
                    });
                }
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}


function redeemCoupon(bondTradeId, expire) {
    console.log('Redeeming coupon at ', bondTradeId, expire);
    $.ajax({
        type: "GET",
        url: "/user/redeemCoupon",
        data: { "bondTradeId": bondTradeId, "expire": expire },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log("Redeem successfully!");
            getBalance();
        },
        error: function (error) {
            console.log("Error: ", error);
        }
    });
}

