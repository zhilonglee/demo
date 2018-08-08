var TRAIN = {
    params : {
        basic_url : "http://127.0.0.1:8099/",
        save_url : "v1/train/save",
        save_hodometer_url : "v1/train/hodometer/save",
        count_train_hodometer_url : "v1/train/hodometer/count/"
    },
    train : {},
    hodometer : {
        train : {},
        interStation : {
            priStation : {},
            deputyStation : {}
        }
    },
    hodometerCount : 0,
    header : {
        csrf_token : "",
        csrf_header : ""
    },
    InitSecurityAuth:function(){
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        console.log("token : " + token );
        console.log("header : " + header);
        TRAIN.header.csrf_token = token;
        TRAIN.header.csrf_header = header;
    },
    save : function () {
        TRAIN.train.name = $("#name").val();
        TRAIN.train.info = $("#info").val();
        TRAIN.train.type = $("#type").val();
        $.ajax({
            url : TRAIN.params.save_url,
            dataType:'json',
            type:'POST',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(TRAIN.train),
            beforeSend:function (xhr) {
                xhr.setRequestHeader(TRAIN.header.csrf_header,TRAIN.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                console.log("save train info successfully.")
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
    saveTrainHodometer : function(trainNameId,costTimeId,stationNameId,destStationNameId){
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var ymd = year + "-" + (month < 9 ? "0" + month :month) + "-" + (day < 9 ? "0" + day :day);
        console.log(ymd);
        TRAIN.hodometer.train.name = $("#" + trainNameId).val();
        TRAIN.hodometer.costTime = ymd + " " + $("#" + costTimeId).val();
        TRAIN.hodometer.interStation.priStation.name = $("#" + stationNameId).val();
        TRAIN.hodometer.interStation.deputyStation.name = $("#" + destStationNameId).val();
            $.ajax({
            url : TRAIN.params.save_hodometer_url,
            dataType:'json',
            type:'POST',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(TRAIN.hodometer),
            beforeSend:function (xhr) {
                xhr.setRequestHeader(TRAIN.header.csrf_header,TRAIN.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                console.log("save train hodometer info successfully.")
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
    countTrainHodometer : function (trainName) {
        $.ajax({
            url : TRAIN.params.count_train_hodometer_url + trainName,
            dataType:'json',
            type:'GET',
            contentType :'application/json;charset=UTF-8',
            beforeSend:function (xhr) {
                xhr.setRequestHeader(TRAIN.header.csrf_header,TRAIN.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (data) {
                TRAIN.hodometerCount = data;
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