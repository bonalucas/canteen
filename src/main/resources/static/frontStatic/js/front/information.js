var path = "http://localhost:8888"

$(document).ready(function(){
    var oldPswflag = 0;

    // 弹出修改密码弹窗
    $("#changePsw").click(function (){
        $("#update-Psw").modal({
            backdrop:'static'
        });
    });

    // 旧密码失去焦点事件
    $("#oldPsw").blur(function (){
        // 判断旧密码是否一致
        if ($("#oldPsw").val() !== $("#Psw").val()) {
            $("#oldPswError").show();
        } else {
            $("#oldPswError").hide();
            oldPswflag=1;
        }
    })

    // 修改密码单击事件
    $("#savePsw").click(function (){
        if (oldPswflag === 1) {
            var pwd = $("#newPsw").val();
            $.ajax({
                type: "POST",
                url: path + "/user/updatePwd",
                data: {"password": pwd},
                dateType:"json",
                success: function(result){
                    if (result.code === 500)
                    {
                        swal(result.msg);
                    }
                    else {
                        $("#update-info").modal('hide');
                        swal(result.data);
                        $("button").click(function (){
                            location.reload();
                        });
                    }
                },
                error:function (){
                    alert("更新失败");
                }
            });
        }
    })

});
