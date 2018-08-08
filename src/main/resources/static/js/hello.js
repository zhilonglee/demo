var REGISTER={
    params:{
        base_url:"http://127.0.0.1:8099/",
        cur_url:"register",
        csrf_token:"",
        csrf_header:""
    },
    InitSecurityAuth:function(){
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        console.log("token : " + token );
        console.log("header : " + header);
        REGISTER.params.csrf_token = token;
        REGISTER.params.csrf_header = header;
    },
    POST:function(){
        console.log(REGISTER.params.base_url + REGISTER.params.cur_url);
        /*
            $.get(REGISTER.params.base_url + REGISTER.params.cur_url,function(data,status) {
            console.log(status);
                    $("#animateTest").html(data.message);
            },);
        */
        $.ajax({
            url: REGISTER.params.cur_url,
            dataType:'jsonp',
            jsonp: "jsonpCallback",
            type : 'GET',
            crossDomain: true,
            beforeSend:function(xhr){
                xhr.setRequestHeader(REGISTER.params.csrf_header,REGISTER.params.csrf_token);
                console.log('Before send');
            },
            success:function(result){
                $("#name").val(result.message);
            },
            error:function(xhr,status,error){
                console.log("status : "+status+" error : "+error);
            }
        });


    },
    SavePerson:function(){
        var suburl = 'v1/person/save';
        var person ={};
        person.name = $("#name").val();
        person.age = $("#age").val();
        person.address = $("#address").val();
        person.sessionCSRF = $("csrf").val();
        $.ajax({
            url: suburl,
            dataType:'json',
            type:'POST',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(person),
            beforeSend:function(xhr){
                xhr.setRequestHeader(REGISTER.params.csrf_header,REGISTER.params.csrf_token);
                console.log(REGISTER.params.base_url + suburl);
                console.log(JSON.stringify(person));
            },
            success:function(result){
                console.log(result.message);
            },
            error:function(xhr){
                console.log("status : "+xhr.status);
            }
        });
    },
    deletePerson:function(){
        var suburl = 'v1/person/delete'
        var person ={};
        person.name = $("#name2").val();
        suburl = suburl + "/" + person.name;
        $.ajax({
            url: suburl,
            dataType:'json',
            type:'DELETE',
            contentType :'application/json;charse=UTF-8',
            data:JSON.stringify(person),
            beforeSend:function(xhr){
                xhr.setRequestHeader(REGISTER.params.csrf_header,REGISTER.params.csrf_token);
                console.log(JSON.stringify(person));
            },
            success:function(result){
                console.log(result.message);
            },
            error:function(xhr){
                console.log("status : "+xhr.status+" error : "+xhr.error);
            }
        });
    },
    updatePersonAge:function(){
        var suburl = 'v1/person/patch'
        var person ={};
        person.age = $("#age3").val();
        suburl =  suburl + "/" + $("#name3").val();
        $.ajax({
            url:suburl,
            dataType:'json',
            type:'PATCH',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(person),
            beforeSend:function(xhr){
                xhr.setRequestHeader(REGISTER.params.csrf_header,REGISTER.params.csrf_token);
                console.log(JSON.stringify(person));
            },
            success:function(result){
                console.log(result.message);
            },
            error:function(xhr){
                console.log("status : "+xhr.status+" error : "+xhr.error);
            }
        });
    },
    findAllByConditions:function(){
        var suburl = 'v1/person/search';
        var person ={};
        person.age = $("#age4").val();
        person.address = $("#address4").val();
        person.name = $("#name4").val();
        person.birthDay = $("#birthDay4").val();
        $.ajax({
            url: suburl,
            dataType:'json',
            type:'GET',
            data:$("#form4").serialize(),
            beforeSend:function(xhr){
                xhr.setRequestHeader(REGISTER.params.csrf_header,REGISTER.params.csrf_token);
                console.log($("#form4").serialize());
                $("#searchContent").html("");
            },
            success:function(result){
                $.each(result.data.content,function(){
                    if(this != 'undefined'){
                        $("#searchContent").append("<p>----------------------------</p>");
                        $("#searchContent").append("<p>Name : " + this.name + "</p>");
                        $("#searchContent").append("<p>Age : " + (this.age == null ? '': this.age) + "</p>");
                        $("#searchContent").append("<p>Address : " + (this.address == null ? '': this.address) + "</p>");
                        $("#searchContent").append("<p>Birth Day : " + (this.birthDay == null ? '': this.birthDay) + "</p>");
                        $("#searchContent").append("<p>UPDATE : </p><input type='button' value='update' onclick='REGISTER.updatePerson("+this.id+")' />");
                    }

                });
            },
            error:function(xhr){
                console.log("status : "+xhr.status+" error : "+xhr.error);
            }
        });
    },
    updatePerson:function(id){
//	var suburl = 'v1/person/update/'+id;
        $("#searchContent").html("");

        $("#searchContent").append("<p>----------update-------------</p>");
        $("#searchContent").append("<p>Name :</p>"+"<input type='text' id='name5' name='name' />");
        $("#searchContent").append("<p>Age :</p>"+"<input type='text' id='age5' name='age' />");
        $("#searchContent").append("<p>Address :</p>"+"<input type='text' id='address5' name='address' />");
        $("#searchContent").append("<p>Birth Day :</p>"+"<input type='text' id='birthDay5' name='birthDay' />");
        $("#searchContent").append("<p>SUMBIT : </p><input type='button' value='submit'  onclick='REGISTER.submitUpdatePerson(" + id + ")' />");
    },
    submitUpdatePerson: function(id){

        var suburl = 'v1/person/update/' + id;
        var person ={};
        person.age = $("#age5").val();
        person.address = $("#address5").val();
        person.name = $("#name5").val();
        person.birthDay = $("#birthDay5").val();
        person.sessionCSRF = $("#csrf5").val();
        $.ajax({
            url:REGISTER.params.base_url + suburl,
            dataType:'json',
            type:'PUT',
            data:person,
            beforeSend:function(xhr){
                xhr.setRequestHeader(REGISTER.params.csrf_header,REGISTER.params.csrf_token);
                console.log(suburl);
                console.log(person);
            },
            success:function(result){
                console.log("Response message : " + result.message);
            },
            error:function(xhr){
                console.log("status : "+xhr.status+" error : "+xhr.error);
            }
        });
    }
};
var stompClient = null;
var WEBSOCKET = {
    data:{
        //state
        connected : false,
        //message
        message
            :
            '',
        rows
            :
            []
    },

    //connection
    connect : function () {
        var socket = new SockJS('/endpoint');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            // register and send message
            stompClient.subscribe('/topic/send', function (msg) {
                WEBSOCKET.data.rows.push(JSON.parse(msg.body));
                WEBSOCKET.data.connected = true;
                $("#socket-content").html("");
                $.each(WEBSOCKET.data.rows,function (i,e) {
                    $("#socket-content").append("<tr>"+
                        "<td>" + e.message + "</td>"+
                        "<td>" + e.date + "</td>"
                        +"</tr>")

                })
            });
            // callback method to get published time
            stompClient.subscribe('/topic/callback', function (r) {
                WEBSOCKET.data.time = 'Current Server Time : ' + r.body;
                WEBSOCKET.data.connected = true;
                $("#current-date").html(WEBSOCKET.data.time);
            });

            //with rabbitMQ
            stompClient.subscribe('/topic/rabbitMQ', function (msg) {
                WEBSOCKET.data.rows.push(JSON.parse(msg.body));
                WEBSOCKET.data.connected = true;
                $("#socket-content").html("");
                $.each(WEBSOCKET.data.rows,function (i,e) {
                    $("#socket-content").append("<tr>"+
                        "<td>" + e.message + "</td>"+
                        "<td>" + e.date + "</td>"
                        +"</tr>")

                })
            });
            WEBSOCKET.data.connected = true;
            $("#connState").html("WebSocket connection has been established ! ");
        });
    },

    disconnect : function () {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        WEBSOCKET.data.connected = false;
        $("#connState").html("WebSocket connection has been closed ! ");
    },

    send : function () {
        WEBSOCKET.data.message = $("#sendMessage").val();
        stompClient.send("/send", {}, JSON.stringify({
            'message': WEBSOCKET.data.message
        }));
    }
};