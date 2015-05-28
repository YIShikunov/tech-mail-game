var ws;

init = function () {
    ws = new WebSocket('ws://' + location.host + '/gameplay');

    ws.onopen = function (event) {
        //
        var data = {
            type : 0,
            obj : document.cookie.substring(11)
        }
        sendMessage(0,data);
    }

    ws.onmessage = function (event) {
        //
        console.log(event.data);
    }

    ws.onclose = function (event) {
        //
    }

};

sendMessage = function (type, action) {
    if (type == 0) {
        ws.send(JSON.stringify(action));
    } else
    if (type == 1) {
        var data = {
            type : 1,
            move : action,
            obj : document.cookie.substring(11)
        }
        ws.send(JSON.stringify(data));
    }
    if (type == 2) {
        var data = {
            type : 2,
            color : color,
            obj : document.cookie.substring(11)
        }
        ws.send(JSON.stringify(data));
    }
}

init();

var alpha=0, beta=0, gamma=0;

window.addEventListener('deviceorientation', function(event) {
    Talpha = Math.round((event.alpha+180)/360*255);
    Tbeta = Math.round((event.beta+180)/360*255);
    Tgamma = Math.round((event.gamma+180)/360*255);
    if ( Math.abs(alpha-Talpha) > 5 || Math.abs(beta-Tbeta) > 5 || Math.abs(gamma-Tgamma ) > 5) {
        color = "rgb("+alpha+","+beta+","+gamma+")";
        document.getElementById("value").innerHTML = color;
        document.body.style.background = color;
        sendMessage(2,color);
        alpha = Talpha; beta = Tbeta; gamma = Tgamma;
    }

});
