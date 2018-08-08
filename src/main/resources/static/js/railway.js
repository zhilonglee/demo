var RAILWAY = {
    params : {
        basic_url : "http://127.0.0.1:8099/",
        province_list_url : "v1/railway/province/list",
        province_cities_url : "v1/railway/station/",
        find_train_depar_dest_url : "v1/train/depAndDest"
    },
    header : {
        csrf_token : "",
        csrf_header : ""
    },
    depAndDest : {},
    InitSecurityAuth:function(){
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        console.log("token : " + token );
        console.log("header : " + header);
        RAILWAY.header.csrf_token = token;
        RAILWAY.header.csrf_header = header;
    },
    getAllProvince : function (selectId) {
        $.ajax({
            url : RAILWAY.params.province_list_url,
            type : "GET",
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                $.each(data,function () {
                    $("#"+selectId).append("<option>"+this.name+"</option>");
                })
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
    },
    getProvinceCitys : function(selectId,name){
        $.ajax({
            url : RAILWAY.params.province_cities_url + name,
            type : "GET",
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                $("#"+selectId).html("<option>Please select a city</option>");
                $.each(data,function () {
                    $("#"+selectId).append("<option>"+this.name+"</option>");
                })
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
    },
    findTrainByDepAndDest : function (departureStationId,destinationStationId,tableId) {
        RAILWAY.depAndDest.departureName = $("#"+departureStationId).val();
        RAILWAY.depAndDest.destinationName = $("#"+destinationStationId).val();
        $.ajax({
            url : RAILWAY.params.find_train_depar_dest_url,
            type : "GET",
            dataType:'json',
            contentType :'application/json;charset=UTF-8',
            data:RAILWAY.depAndDest,
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                $("#"+tableId).html('');
                $.each(data,function () {
                    $("#"+tableId).append("<tr><td>"+this+"</td></tr>");
                })
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