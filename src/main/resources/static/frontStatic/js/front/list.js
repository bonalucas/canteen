$(document).ready(function (){
    $("[name='finishList']").click(function (){
        // 创建get请求路径
        let downloadUrl = "http://localhost:8888/front/print";
        // 创建a标签
        let label = $("<a>");
        // 添加属性
        label.prop("href",downloadUrl);
        // 追加标签
        $("body").append(label);
        // 点击a标签，为什么要加[0]不懂，不加点击函数不生效
        label[0].click();
        // 点击后移除标签
        label.remove();
    })
});