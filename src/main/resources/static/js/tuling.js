var TULING = {
    params : {
        basic_url : "http://127.0.0.1:8099/",
        calling_url : "tuling"
    },
    requestparams : {text : ""},
    content : "",
    header : {
        csrf_token : "",
        csrf_header : ""
    },
    InitSecurityAuth:function(){
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        console.log("token : " + token );
        console.log("header : " + header);
        TULING.header.csrf_token = token;
        TULING.header.csrf_header = header;
    },
    chat : function (text) {
        TULING.requestparams.text = text;
        $.ajax({
            url : TULING.params.calling_url,
            dataType:'json',
            type:'GET',
            async : false,
            contentType :'application/json;charset=UTF-8',
            data:TULING.requestparams,
            beforeSend:function (xhr) {
                xhr.setRequestHeader(TULING.header.csrf_header,TULING.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                TULING.content = data.results[0].values.text;
            },
            error : function (xhr, errormsg, error) {
                console.log("error msg : " + errormsg);
                console.log("xhr.status : " + xhr.status);
                console.log("xhr.statusText : " + xhr.statusText)
            },
            complete : function (xhr, ts) {
                console.log("xhr.readyState : " + xhr.readyState);
                console.log("xhr.status : " + xhr.status);
            }
        });
    }
};