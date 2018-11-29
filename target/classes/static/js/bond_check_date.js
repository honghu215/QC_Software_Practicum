

function returnCoupon() {
    // let balance = $('#balance').text();
    // $.ajax({
    //     type: "GET",
    //     url: "/user/bonds",
    //     contentType: "application/json; charset=utf-8",
    //     success: function (data) {
    //         data.forEach(function (bond) {
    //             console.log(bond);
    //             let issuedOn = new Date(bond.issuedOn);
    //             let today = new Date();
    //             let months = (today.getFullYear() - issuedOn.getFullYear())*12 +
    //                 (today.getMonth() - issuedOn.getMonth() - 1);
    //             let frequency = Math.round(months/6);
    //             if ( frequency > bond.returned) {
    //                 postBalance((months/6 - bond.returned) * bond.coupon, bond.bondName, frequency);
    //             }
    //         });
    //     },
    //     error: function (error) {
    //         console.log(error);
    //     }
    // });
}

function postBalance(value, bondName ,frequency) {
    $.ajax({
        type: "GET",
        url: "/user/adjustBalance",
        data: { "value": value,
                "frequency": frequency,
                "bondName": bondName
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log('Success! Returned ', value, ' to your account.');
        },
        error: function (error) {
            console.log('Error: ', error);
        }
    });
}