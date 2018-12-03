
function refreshStock() {
    setInterval(function () {
        let rowStr = '';
        $.ajax({
            type: "GET",
            url: "/user/market/stocks",
            contentType: "application/json; charset=utf-8",
            success: function (stocks) {
                $.each(stocks, function (index, stock) {
                    console.log(stock);
                    rowStr += '<tr> <td id="index" style="display: none">'+index+'</td>'
                            + '<td id="stockName">'+stock.stockName+'</td>'
                            + '<td>' + stock.price + '</td>'
                            + '<td>' + stock.stockField + '</td>'
                            + '<td> <button class="btn btn-primary" onclick="getCurrentPrice(this)" data-toggle="modal" data-target="#buyStock">Buy/Sell</button></td>';
                });
                $('#stockBody').html(rowStr);
            }
        });
    }, 5000);
}