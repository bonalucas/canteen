var path = "http://localhost:8888"

$(document).ready(function () {
    showcart();
});

function deleteCartGoods(scId) {
    $.ajax({
        url: path + "/shopCart/delete/" + scId,
        dataType: 'json',
        type: "DELETE",
        success: function (res) {
            if (res.code === 200) {
                showcart();
            }else {
                swal(res.msg);
            }
        },
        error:function () {
            swal("删除失败");
        }
    })
}

function showcart() {
    $.ajax({
        url: path + "/shopCart/get",
        type: "get",
        dataType: 'json',
        success: function (result) {
            if (result.code === 200){
                //显示购物车
                build_cart_table(result);
            }else{
                swal(result.msg);
            }
        },
        error: function (result) {
            swal(result.msg);
        }
    });
}

function build_cart_table(result) {
    $("#cart-table tbody").empty();
    var goods = result.data;
    var totalnum = 0;
    var totalMoney = 0;
    if(goods.length === 0) {
        var spareTd = $('<tr> <td colspan="6"> <div class="coupon" style="margin-left:37%;">购物车还是空的，快去<a href="/front/toMain" style="color:red;">首页</a>看看吧！ </div> </td> </tr>');
        spareTd.appendTo("#cart-table tbody");
    } else {
        $.each(goods, function (index,item) {
            var delA = $("<button></button>").addClass("delete-goods").attr("data-goodsid",item.scId).append("删除");
            var deleteCart = $("<td></td>").addClass("product-remove product-remove_2")
                .append(delA);
            delA.click(function () {
                deleteCartGoods(item.scId);
            });
            var shopimage = $("<td></td>").addClass("product-thumbnail product-thumbnail-2")
                    .append($("<img/>").attr("src",path + "/picture/" + item.picture));
            var goodsname = $("<td></td>").addClass("product-name product-name_2").append(item.name);
            var goodsprice = $("<td></td>").addClass("product-price")
                .append($("<span></span>").addClass("amount-list amount-list-2").append("￥"+item.price));
            var num = $("<td></td>").addClass("product-stock-status")
                .append($("<div></div>").addClass("latest_es_from_2")
                    .append(item.weight));
            var totalPrice = $("<td></td>").addClass("product-price")
                .append($("<span></span>").addClass("amount-list amount-list-2").append("￥"+item.totalPrice));
            var goodsitem = $("<tr></tr>")
                .append(shopimage)
                .append(goodsname)
                .append(goodsprice)
                .append(num)
                .append(totalPrice)
                .append(deleteCart)
                .appendTo("#cart-table tbody");
            totalnum = totalnum + parseInt(item.weight);
            totalMoney = totalMoney + parseInt(item.totalPrice);
        });
    }
    //小计
    $("#total-num").text(totalnum);
    $("#total-price").text("￥"+totalMoney);
}
