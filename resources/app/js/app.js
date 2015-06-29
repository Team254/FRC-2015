var chartData = []
var state = true;
var max = 0
var subscribed = [];
var tick = 0;

_.mixin({
  capitalize: function(string) {
    return string.charAt(0).toUpperCase() + string.substring(1).toLowerCase();
  }
});

var data = new dataAPI(window.location.host, function(){
    generateButtons()
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




var colors = ["#93E814", "#FFAD22", "#E8204A", "#3F27FF", "#FF553B", "#62FF3B", "#36CAE8", "#8D35FF", "#000", "#DDD"]
var usedColors = []

function axesSet(){
    var max = $("#max").val()
    var min = $("#min").val()

    if (max != null && max != ""){
        chart.valueAxes[0].maximum = parseInt(max);
    } else {
        delete chart.valueAxes[0].maximum
    }

    if (min != null && min != ""){
        chart.valueAxes[0].minimum = parseInt(min);
    } else {
        delete chart.valueAxes[0].minimum
    }
}

function addChart(stream){
    var graph = new AmCharts.AmGraph();
    var axes = chart.valueAxes;
    for(var i = 0; i < axes.length; i++) {
        if(axes[i].title == stream) {
            graph.valueAxis = "Data";
            axes[i].offset = max + 80 
            max += 80
        }
    }
    graph.title = stream;
    graph.valueField = stream;
    graph.balloonText = "[[title]]: [[value]]";
    graph.lineThickness = 2;
    if (usedColors.length <= colors.length){
    	for (i = 0; i < colors.length; i++){
    		if (!contains(usedColors, colors[i])){
    			graph.lineColor = colors[i];
    			usedColors.push(colors[i])
    			break;
    		}
    	}
    } else {
    	graph.lineColor = "#0070FF";
    }
    //Uncomment to smooth data
    /*graph.type = "smoothedLine";*/
    chart.addGraph(graph);
}

function removeChart(stream) {

    for(var i=0; i<chart.graphs.length;i++) {
        if(chart.graphs[i].valueField == stream) {
            var graph = chart.graphs[i];
            chart.graphs.splice(i,1);
            chart.removeGraph(graph);
            for (var j =0; j < usedColors.length; j++){
            	console.log("does " + graph.lineColor + " equal " + " " + usedColors[j] + "?")
            	if (graph.lineColor == usedColors[j]){
            		usedColors.splice(j,1);
            	}
            }
        }
    }
    for (var i = 0; i < chartData.length; i++) {
        delete chartData[i][stream];
    }
}


var chartCallBack = function(dataset){
    
    if (state) {
        newData = dataset
        chart.dataProvider.push(newData)

        for (var key in newData){
            $("#btnvalue-" + key.replace(".", "\\.")).html(Math.round(newData[key] * 1000)/1000)
        }

        if (chart.dataProvider.length > 200){
            chart.dataProvider.splice(0,1);
        }
        tick++
        
    }
    

    if (tick % 11 == 0){
        chart.validateData();
        tick++
    }
    
}

var chart = AmCharts.makeChart("chartdiv", {
    "type": "serial",
    "theme": "none",
    "pathToImages": "js/amcharts_3/amcharts/images/",
    "legend": {
        "useGraphSettings": true
    },
    "dataProvider": chartData,
    "valueAxes": [{
        "id":"Data",
        "title":"Data",
        "axisColor": "#0070FF",
        "axisThickness": 2,
        "gridAlpha": 0,
        "axisAlpha": 1,
        "position": "left"
    }],
    "graphs": [],
    "chartScrollbar": {},
    "chartCursor": {
        "cursorPosition": "mouse",
        "categoryBalloonDateFormat": "JJ:NN:SS:fff, DD MMMM",
    },
    "categoryField": "robot.robotTime",
    
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
    var systems = Object.keys(data.getSubsystems());
    var grouped_systems = {};

    var html = "";
    _.each(systems, function(currSystem){
        var subsytem_name = currSystem.split(".")[0];
        var signal_name = currSystem.split(".")[1];
        var signals = grouped_systems[subsytem_name] || [];
        signals.push(signal_name);
        grouped_systems[subsytem_name] = signals;
    });

    _.each(Object.keys(grouped_systems), function(subsystem) {
        var signals = grouped_systems[subsystem];
        html += "<div><h3>" + _(subsystem).capitalize() + "</h3>";
        html += "<table><tr>";
        _.each(signals, function(signal) {
            var currSystem = subsystem + "." + signal;
            html += "<td class='button_holder'><div class='btn-group'  data-toggle='buttons'><label onclick='access(\""+currSystem+"\")' class='btn btn-primary'><input type='checkbox' autocomplete='off'>"+signal+"</label></div></td>";
        });
        html += "</tr><tr>";
        _.each(signals, function(signal) {
            var currSystem = subsystem + "." + signal;
            html += "<td class='button_holder'><center><p id='btnvalue-" + currSystem + "'></p></center></td>";
        });
        html += "</tr></table></div>";

    });

    $("#button_area").html(html);
}


//An API to connect to the robot
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
            callback(dat)
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

}