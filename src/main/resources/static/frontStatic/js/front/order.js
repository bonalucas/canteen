var path = "http://localhost:8888"

$(document).ready(function () {
    $("#confirm-orders").click(function () {
        var totalPrice = $('#total-new').text();
        var data = {
            "orderPrice": totalPrice
        }
        $.ajax({
            url: path + "/order/submit",
            type: "POST",
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (res) {
                if (res.code === 200){
                    swal(res.data);
                    location.href = path + "/front/toOrder"
                }else{
                    swal(res.msg);
                }
            },
            error: function (res) {
                swal(res.msg);
            }
        });
    });
});