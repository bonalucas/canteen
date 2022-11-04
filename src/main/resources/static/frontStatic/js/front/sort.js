$(document).ready(function(){
    var path = $("#path").text();
	//幻灯片左右控制器
	$("#mycarousel").hover(function(){
		$(this).children(".carousel-control").show();
	},function(){
		$(this).children(".carousel-control").hide();
	});
	//收藏按钮
	$(".data-item-li").hover(function(){
		$(this).find(".like-button").show();
	},function(){
		$(this).find(".like-button").hide();
	});
    //用户按钮（账户管理和退出登录）
	$(".info-a").hover(function(){
		$(this).find(".dropdown-menu").show();
	},function(){
		$(this).find(".dropdown-menu").hide();
	});

	$('.like-button').click(function(){
        var goodsId = $(this).attr('data-id');
        var isChangeBtn = true;
        if(!$(this).hasClass('glyphicon-heart')) {
            //收藏
            $.ajax({
                url:path + "/collect",
                type:"POST",
                data:{
                    goodsid:goodsId
                },
                success:function (result) {
                    //收藏失败
                    if(result.code === 200){
                        //跳转登录
                        location.href = path + "/login";
                        isChangeBtn = false;
                    }else {
                        alert("收藏成功！")
                    }
                },
                error:function () {
                    alert("收藏失败！");
                }
            })
        } else {
            //取消收藏
            $.ajax({
                url:path + "/deleteCollect",
                type:"POST",
                data:{
                    goodsid:goodsId
                },
                success:function (result) {
                    //取消收藏成功
                    if(result.code === 200){
                        location.href = path + "/login";
                        isChangeBtn = false;
                    }else{
                        alert("取消收藏成功！")
                    }
                },
                error:function () {
                    alert("取消收藏失败！");
                }
            })
        }
        // 判断是否按钮改变
		if(isChangeBtn) {
            $(this).toggleClass("glyphicon-heart glyphicon-heart-empty");
        }
	});
});