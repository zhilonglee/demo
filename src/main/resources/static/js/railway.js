var RAILWAY = {
    params : {
        basic_url : "http://127.0.0.1:8099/",
        province_list_url : "v1/railway/province/list",
        province_cities_url : "v1/railway/station/",
        find_train_depar_dest_url : "v1/train/depAndDest",
        buy_ticket_url : "/v1/train/rabbit/train/",
        search_tickets_url : "/v1/train/ticket/list",
        ticket_payment_url : "/v1/train/ticket/payment",
        ticket_refund_url : "/v1/train/ticket/refund"
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
                $("#"+tableId).html('<tr><td>Train Name</td><td>Train Type</td>' +
                    '<td>Detail</td><td>Departure Station</td>' +
                    '<td>Departure Time</td>' +
                    '<td>Destination Station</td><td>Destination Time</td>' +
                    '<td>Cost Time</td></tr>');
                $.each(data,function () {
                    var text = "RAILWAY.buyTicket(" + "\'" + this.trainName + "\',\'" +
                        this.departStatinName + "\',\'" + this.destStationName + "\',\'" +
                        this.departStationdate + "\',\'" + this.destStationdate + "\')";
                    $("#"+tableId).append("<tr>" +
                        "<td>"+this.trainName+"</td>" +
                        "<td>"+this.type+"</td>" +
                        "<td>"+this.detail+"</td>" +
                        "<td>"+this.departStatinName+"</td>" +
                        "<td>"+this.departStationdate+"</td>" +
                        "<td>"+this.destStationName+"</td>" +
                        "<td>"+this.destStationdate+"</td>" +
                        "<td>"+((this.costTime == null || this.costTime == '') ? '' : RAILWAY.convertLong2HMS(this.costTime))+"</td>" +
                        "<td>" + "<input type='button' value='Purchase' onclick=" + text + " />" + "</td>" +
                        "</tr>");
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
    convertLong2HMS : function(timestamp){

/*        var temp = timestamp % (1000 * 60 * 60 * 24);
        var hours = (temp == 0 ? 0 : parseInt(temp / (1000 * 60 * 60)));
        temp = timestamp % (1000 * 60 * 60) ;
        var minutes = (temp == 0 ? 0 : parseInt(temp / (1000 * 60)));
        temp = timestamp % (1000 * 60);
        var seconds = (temp == 0 ? 0 :parseInt((temp % (1000 * 60)) / 1000));*/

        var hours = parseInt((timestamp % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = parseInt((timestamp % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = (timestamp % (1000 * 60)) / 1000;

        var time =  (hours < 10 ? ("0" + hours):hours) + ":" +
            (minutes < 10 ? ("0" + minutes) : minutes) + ":" +
            (seconds < 10 ? ("0" + seconds) : seconds);

        return time;

    },
    buyTicket : function (trainName,departStatinName,destinationName,departStationdate,destStationdate) {
        var ticket = {};
        ticket.trainName = trainName;
        ticket.departure = departStatinName;
        ticket.destination = destinationName;
        ticket.departureTime = departStationdate;
        ticket.destinationTime = destStationdate;
        $.ajax({
            url : RAILWAY.params.buy_ticket_url + ticket.trainName,
            type : "POST",
            dataType:'json',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(ticket),
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (result) {
                if(result.code == 200){
                    alert("Succeed. Ticket info : " + JSON.stringify(result.data));
                }else if (result.code == 302){
                    window.location.href=result.detail;
                }else {
                    alert("Encounter error : " + JSON.stringify(result.detail))
                }
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
    findTickets : function (tableId) {
        $.ajax({
            url : RAILWAY.params.search_tickets_url,
            type : "GET",
            dataType:'json',
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (result) {
                if(result.code == 200){
                    $("#"+tableId).html("");
                    $("#"+tableId).append("<tr>" +
                        "<td>Date of Ticket</td>" +
                        "<td>Departure</td>" +
                        "<td>Departure Time</td>" +
                        "<td>Destination</td>" +
                        "<td>Destination Time</td>" +
                        "<td>Train Name</td>" +
                        "<td>Seat Type</td>" +
                        "<td>Train Carriage</td>" +
                        "<td>Carriage Row</td>" +
                        "<td>Carriage Column</td>" +
                        "<td>Ticket Price</td>" +
                        "<td>Ticket Status</td>" +
                        "<td>Payment</td>" +
                        "<td>Refund</td>" +
                        "</tr>");
                    $.each(result.data,function () {
                        var text1 = "RAILWAY.payTicket(" + this.id + ",\'" + tableId + "\')";
                        var text2 = "RAILWAY.refundTicket(" + this.id + ",\'" + tableId + "\')";
                        $("#"+tableId).append("<tr>" +
                            "<td>" + this.creationDay + "</td>" +
                            "<td>" + this.departure + "</td>" +
                            "<td>" + this.departureTime + "</td>" +
                            "<td>" + this.destination + "</td>" +
                            "<td>" + this.destinationTime + "</td>" +
                            "<td>" + this.trainName + "</td>" +
                            "<td>" + this.ticketType + "</td>" +
                            "<td>" + this.carriageNo + "</td>" +
                            "<td>" + this.row + "</td>" +
                            "<td>" + this.column + "</td>" +
                            "<td>" + this.price + "</td>" +
                            "<td>" + this.ticketStatus + "</td>" +
                            "<td>" + "<input type='button'  onclick=" + text1 + " value='Pay' />" + "</td>" +
                            "<td>" + "<input type='button'  onclick=" + text2 + " value='Refund' />" + "</td>" +
                            "</tr>");
                    });

                }else if(result.code == 302){
                    window.location.href=result.detail;
                }
                else{
                    alert(result.message + " : " + result.detail)
                }
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
    payTicket : function (tickertId,tableId) {
        var ticket = {};
        ticket.id = tickertId;
        $.ajax({
            url : RAILWAY.params.ticket_payment_url,
            type : "POST",
            dataType:'json',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(ticket),
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (result) {
                if(result.code == 200){
                    alert("Succeed. Ticket info : " + JSON.stringify(result.data));
                    RAILWAY.findTickets(tableId);
                }else {
                    alert("Encounter error : " + JSON.stringify(result.detail))
                }
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
    refundTicket : function (tickertId,tableId) {
        var ticket = {};
        ticket.id = tickertId;
        $.ajax({
            url : RAILWAY.params.ticket_refund_url,
            type : "POST",
            dataType:'json',
            contentType :'application/json;charset=UTF-8',
            data:JSON.stringify(ticket),
            beforeSend:function (xhr) {
                xhr.setRequestHeader(RAILWAY.header.csrf_header,RAILWAY.header.csrf_token);
                console.log(RAILWAY.params.basic_url + RAILWAY.params.province_list_url);
                console.log(RAILWAY.header.csrf_header + " : " + RAILWAY.header.csrf_token);
                console.log("xhr.readyState : " + xhr.readyState);
            },
            success : function (result) {
                if(result.code == 200){
                    alert("Succeed. Ticket info : " + JSON.stringify(result.data));
                    RAILWAY.findTickets(tableId);
                }else {
                    alert("Encounter error : " + JSON.stringify(result.detail))
                }
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