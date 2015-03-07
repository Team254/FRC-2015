var chartData = []
var state = true;
var max = 0
var subscribed = [];
var tick = 0;
var data = new dataAPI(window.location.host, function(){
    generateButtons()
    createValueAxis(Object.keys(data.getSubsystems()))
});

/*Protos!*/
Date.prototype.subTime= function(h,m){
    this.setHours(this.getHours()-h);
    this.setMinutes(this.getMinutes()-m);
    return this;
}
Date.prototype.addTime= function(h,m){
    this.setHours(this.getHours()+h);
    this.setMinutes(this.getMinutes()+m);
    return this;
}


/*$(document).ready(function(){
    $("#slide").change(function(){
        console.log($("#slide").val())
        $("#hz").html("&nbsp; " + 1000/$("#slide").val() + " hz")
    })
})*/

var colors = ["#93E814", "#FFAD22", "#E8204A", "#3F27FF", "#FF553B", "#62FF3B", "#36CAE8", "#8D35FF", "#000", "#DDD"]

/*
setInterval(function(){
    if (state) {
        var newDate = new Date();

        var visits = Math.round(Math.random() * 40) + 100;
        var hits = Math.round(Math.random() * 80) + 500;
        var views = Math.round(Math.random() * 6000);

        chart.dataProvider.push({
            date: newDate,
            visits: visits,
            hits: hits,
            views: views
        });
        chart.validateData();
    }
}, 200);
*/
function createValueAxis(keys) {
    for(var i = 0; i<keys.length;i++) {
        var yAxis = new AmCharts.ValueAxis();
        yAxis.position = "left";
        yAxis.id = keys[i];
        yAxis.title = keys[i];
        yAxis.axisColor = colors[i]
        yAxis.axisThickness = 2
        yAxis.gridAlpha = 0;
        chart.addValueAxis(yAxis);
    }
}

function addChart(stream){
    var graph = new AmCharts.AmGraph();
    var axes = chart.valueAxes;
    for(var i = 0; i < axes.length; i++) {
        if(axes[i].title == stream) {
            graph.valueAxis = axes[i];
            axes[i].offset = max + 80 
            max += 80
        }
    }
    graph.title = stream;
    graph.valueField = stream;
    graph.balloonText = "[[title]]: [[value]]";
    graph.lineThickness = 2;
    graph.lineColor = graph.valueAxis.axisColor;
    /*graph.type = "smoothedLine";*/
    chart.addGraph(graph);
}

function removeChart(stream) {
    for (var i = chart.graphs.length-1; i >= 0; i--) {
        if (chart.graphs[i].valueField != stream) {
    	    chart.graphs[i].valueAxis.offset -= 80;
        } else {
            max -= 80;
            break;
        }
    }
    for(var i=0; i<chart.graphs.length;i++) {
        if(chart.graphs[i].valueField == stream) {
            var graph = chart.graphs[i];
            chart.graphs.splice(i,1);
            chart.removeGraph(graph)
        }
    }
    for (var i = 0; i < chartData.length; i++) {
        delete chartData[i][stream];
    }
}

/*setInterval(function(){
    
    console.log("Update")
}, 1000)*/

var chartCallBack = function(dataset){
    
    if (state) {
        newData = dataset
        chart.dataProvider.push(newData)

        if (chart.dataProvider.length > 100){
            chart.dataProvider.splice(0,1);
        }
        
    }
    

    if (tick % 10 == 0){
        chart.validateData();
    }
    tick++
}

var chart = AmCharts.makeChart("chartdiv", {
    "type": "serial",
    "theme": "none",
    "pathToImages": "js/amcharts_3/amcharts/images/",
    "legend": {
        "useGraphSettings": true
    },
    "dataProvider": chartData,
    "valueAxes": [/*{
        "id":"drive.leftMotorB",
        "axisColor": "#FF6600",
        "axisThickness": 2,
        "gridAlpha": 0,
        "axisAlpha": 1,
        "position": "left"
    }*/],
    "graphs": [/*{
        "valueAxis": "drive.leftMotorB",
        "lineColor": "#FF6600",
        "bullet": "round",
        "bulletBorderThickness": 1,
        "hideBulletsCount": 30,
        "title": "red line",
        "valueField": "drive.leftMotorB",
        "fillAlphas": 0,
        "balloonText": "[[title]]: [[value]]",
        "lineThickness": 2,
    }*/],
    "chartScrollbar": {},
    "chartCursor": {
        "cursorPosition": "mouse",
        "categoryBalloonDateFormat": "JJ:NN:SS:fff, DD MMMM",
    },
    "categoryField": "robot.robotTime",
    /*"categoryAxis": {
        "parseDates": true,
        "axisColor": "#DADADA",
        "minorGridEnabled": true,
        "minPeriod": "fff"
    }*/
});

function pause(){
    if (state === false) {
        chart.dataProvider = [];
    }
    state = !state
    chart.validateData();
}

function access(stream) {
    var hasStream = data.contains(data.getSubscribe(),stream);
    if (hasStream) {
        data.unsubscribe([stream])
        removeChart(stream)
    } else {
        data.subscribe([stream], chartCallBack)
        addChart(stream)
    }
}

function contains(array, stream) {
    for (var i=0; i < array.length; i++) {
        if(array[i] == stream) {
            return true;
        }
    }
    return false
}

function generateButtons() {
    var systems = data.getSubsystems();
    systems = Object.keys(systems);
    for(var i = 0; i < systems.length; i++) {
        var currSystem = systems[i]
        $("#buttons").append("<label onclick='access(\""+currSystem+"\")' class='btn btn-primary'><input type='checkbox' autocomplete='off'>"+currSystem+"</label>");
    }
    
}

//A model API
function dataAPI(socket, subsystemCallback){
    var robotTime = -1
    var ws = new WebSocket("ws://" + socket + "/state");
    var subsystems = {"default": "data"}
    var subscribed = []
    var robotTime = 0

    this.getSubsystems = function(){
        return subsystems
    }

    //Get KEYLIST
    $.getJSON("http://" + socket + "/keys", function(data){
        subsystems = data;
        internalNetworkRequestCallBack();
        subsystemCallback();
        
    });

    var internalNetworkRequestCallBack = function(){
        setTimeout(function(){
            ws.send(JSON.stringify({"action": "subscribe", "keys": ["robot.robotTime"]}))
        }, 500)
    }

    this.contains = function(array, stream) {
        for (var i=0; i < array.length; i++) {
            if(array[i] == stream) {
                return true;
            }
        }
        return false
    }
    
    var callback = function(dataArray){};

    ws.onmessage = function(rawmessage){

        message = JSON.parse(rawmessage.data)
        if (message["robot.robotTime"] != null){
            robotTime = message["robot.robotTime"]
        }
        var dat = message

        
        if (Object.keys(subscribed).length > 0){
            dat["date"] = robotTime
            console.log(dat.robotTime)
            callback(dat)
        } else {
            //console.log("err")
        }
    }
    
    this.subscribe = function(keylist, anonymousFunction){
        //takes in an array of subsystems and a callback with an array as a parameter to subscribe to
        //array of subscriptions?
        if (!contains(subscribed, "robot.robotTime") && contains(keylist, "robot.robotTime")){
            subscribed.push("robot.robotTime")
            
        }
        for (var i = 0; i < keylist.length; i++){
            if (keylist[i] == "robot.robotTime"){
                keylist.splice(i,0);
            }
        }
        if (anonymousFunction != null){
            callback = anonymousFunction;
        }
        for (i = 0; i < keylist.length; i++){
            if (keylist[i] != "robot.robotTime"){
                subscribed.push(keylist[i])
            }
        }
        ws.send(JSON.stringify({"action": "subscribe", "keys": keylist}))
    }
    this.clearsubscribe = function(){
        subscribed = []
        callback = function(dataArray){};
    }
    this.unsubscribe = function(removalarray){

        for (var i = 0; i < removalarray.length; i++) {
            for (var j = 0; j < subscribed.length; j ++){
                if (removalarray[i] == subscribed[j]){
                    subscribed.splice(j, 1);
                }
            }
            if (removalarray[i] == "robot.robotTime"){
                removalarray.splice(i,1);
            }
        }
        if (removalarray.length > 0){
            ws.send(JSON.stringify({"action": "unsubscribe", "keys": removalarray}))
        }
        console.log(removalarray)
    }
    this.getSubscribe = function(){
        return subscribed;
    }
    this.changeCallback = function(newCallBack){
        callback = newCallBack
    }
    
    var genRand = function(){
        return (Math.random()-0.5)*100;
    }

    //


}