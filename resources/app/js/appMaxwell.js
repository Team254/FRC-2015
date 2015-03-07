var chartData = generateChartData();
var state = true;
var subscribed = [];
var dataAPI(1000);

setInterval(function(){
    if (false) {
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

var chart = AmCharts.makeChart("chartdiv", {
    "type": "serial",
    "theme": "none",
    "pathToImages": "/js/amcharts_3/amcharts/images/",
    "legend": {
        "useGraphSettings": true
    },
    "dataProvider": chartData,
    "valueAxes": [{
        "id":"v1",
        "axisColor": "#FF6600",
        "axisThickness": 2,
        "gridAlpha": 0,
        "axisAlpha": 1,
        "position": "left"
    }, {
        "id":"v2",
        "axisColor": "#FCD202",
        "axisThickness": 2,
        "gridAlpha": 0,
        "axisAlpha": 1,
        "position": "right"
    }, {
        "id":"v3",
        "axisColor": "#B0DE09",
        "axisThickness": 2,
        "gridAlpha": 0,
        "offset": 50,
        "axisAlpha": 1,
        "position": "left"
    }],
    "graphs": [{
        "valueAxis": "v1",
        "lineColor": "#FF6600",
        "bullet": "round",
        "bulletBorderThickness": 1,
        "hideBulletsCount": 30,
        "title": "red line",
        "valueField": "visits",
        "fillAlphas": 0
    }, {
        "valueAxis": "v2",
        "lineColor": "#FCD202",
        "bullet": "square",
        "bulletBorderThickness": 1,
        "hideBulletsCount": 30,
        "title": "yellow line",
        "valueField": "hits",
        "fillAlphas": 0
    }, {
        "valueAxis": "v3",
        "lineColor": "#B0DE09",
        "bullet": "triangleUp",
        "bulletBorderThickness": 1,
        "hideBulletsCount": 30,
        "title": "green line",
        "valueField": "views",
        "fillAlphas": 0
    }],
    "chartScrollbar": {},
    "chartCursor": {
        "cursorPosition": "mouse",
        "categoryBalloonDateFormat": "JJ:NN:SS:fff, DD MMMM",
    },
    "categoryField": "date",
    "categoryAxis": {
        "parseDates": true,
        "axisColor": "#DADADA",
        "minorGridEnabled": true,
        "minPeriod": "fff"
    }
});

chart.addListener("dataUpdated", zoomChart);
zoomChart();


// generate some random data, quite different range
function generateChartData() {
    var chartData = [];

        // we create date objects here. In your data, you can have date strings
        // and then set format of your dates using chart.dataDateFormat property,
        // however when possible, use date objects, as this will speed up chart rendering.
        var newDate = new Date();

        var visits = Math.round(Math.random() * 40) + 100;
        var hits = Math.round(Math.random() * 80) + 500;
        var views = Math.round(Math.random() * 6000);

        chartData.push({
            date: newDate,
            visits: visits,
            hits: hits,
            views: views
        });
    return chartData;
}

function zoomChart(){
    if (state){
    chart.zoomToIndexes(chart.dataProvider.length - 20, chart.dataProvider.length - 1);
    }
}

//End of chart things

function pause(){
    if (state === false){
        chart.dataProvider = [];
    }
    state = !state
    chart.validateData();
}

function access(stream) {
    var index = contains(dataAPI.getSubscribe(),stream);
    if (index != -1) {
        subscribed.splice(index,1);
    } else {
        subscribed.push(stream);
    }
    
    console.log(stream + ": " + subscribed + "! " + contains(subscribed, stream));
}

function contains(array, stream) {
    for (var i=0; i < array.length; i++) {
        if(array[i] == stream) {
            return t;
        }
    }
    return -1
}

//A model API to play with
function dataAPI(pollrate){
    this.subsystems = {"drive.leftMotorB":"java.lang.Double","drive.leftEncoder":"java.lang.Integer","drive.rightMotorA":"java.lang.Double","drive.leftMotorA":"java.lang.Double","drive.rightMotorB":"java.lang.Double"};
    var subscribed = []
    var prate = pollrate
    var callback = function(dataArray){};
    setInterval(function(){
        var data = {};
        if (subscribed.length > 0) {
            for (var i = 0; i < subscribed.length; i++) {
                data[subscribed[i]] = genRand();
            }
        }
        callback(data)
    }, prate);
    
    this.subscribe = function(keylist, anonymousFunction){
        //takes in an array of subsystems and a callback with an array as a parameter to subscribe to
        //array of subscriptions?
        callback = anonymousFunction;
        subscribed = keylist;
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
        }
    }
    this.getSubscribe() {
        return subscribed;
    }
    this.changeCallback = function(newCallBack){
        callback = newCallBack
    }
    
    this.changePollRate = function(newPollRate){
        prate = newPollRate
    }
    
    var genRand = function(){
        return Math.random()*100;
    }
}