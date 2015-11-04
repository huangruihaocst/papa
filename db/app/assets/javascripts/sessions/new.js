function checkFields() {
    if($("#user_login").val()==""){
        $(".alert").html("请输入登录的手机或email");
        return false;
    }
    if($("#user_password").val()==""){
        $(".alert").html("请输入密码");
        return false;
    }
    return true;
}