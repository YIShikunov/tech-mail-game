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
    if (ws != null) {
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
        if (type == 3) {
            var data = {
                type : 3,
                posX : action[0],
                posY : action[1],
                play : action[2],
                obj : document.cookie.substring(11)
            }
            ws.send(JSON.stringify(data));
        }
        if (type == 4) {
            var data = {
                type : 4,
                land : action,
                obj : document.cookie.substring(11)
            }
            ws.send(JSON.stringify(data));
        }
    }
}

init();


var canvas = document.getElementById("joystick");
var context = canvas.getContext('2d');
canvas.width = document.body.clientWidth;
canvas.height = document.body.clientHeight;

var initColor = function(){
    context.strokeStyle = "blue";
    context.lineWidth=10;
    context.lineJoin = "round"; 
    context.lineCap = "round";
}

var alpha=0, beta=0, gamma=0;

window.addEventListener('deviceorientation', function(event) {
    Talpha = Math.round((event.alpha+180)/360*255);
    Tbeta = Math.round((event.beta+180)/360*255);
    Tgamma = Math.round((event.gamma+180)/360*255);
    if ( Math.abs(alpha-Talpha) > 5 || Math.abs(beta-Tbeta) > 5 || Math.abs(gamma-Tgamma ) > 5) {
        color = "rgb("+alpha+","+beta+","+gamma+")";
        document.body.style.background = color;
        sendMessage(2,color);
        alpha = Talpha; beta = Tbeta; gamma = Tgamma;
    }
});

var posX, posY;

window.addEventListener('touchstart', function(event){
    touch = event.touches[0];
    posX = touch.pageX; posY = touch.pageY;
    context.beginPath();
    context.moveTo(touch.pageX, touch.pageY);
    window.addEventListener('touchmove', onPaint, false);

}, false);

canvas.addEventListener('touchend', function(event) {
    canvas.removeEventListener('touchmove', onPaint, false);
    setTimeout(function() { context.clearRect(0, 0, canvas.width, canvas.height); }, 400);
    touch = event.changedTouches[0];
    context.lineTo(touch.pageX, touch.pageY);
    context.stroke();
    dx = Math.round(touch.pageX - posX);
    dy = Math.round(touch.pageY - posY);
    sendMessage(3,[dx, dy, true]);
}, false);


var onPaint = function(event){
    touch = event.changedTouches[0];
    context.lineTo(touch.pageX, touch.pageY);
    context.stroke();
    dx = Math.round(touch.pageX - posX);
    dy = Math.round(touch.pageY - posY);
    if ( Math.abs(dx) > 2 || Math.abs(dy) > 2) {
        sendMessage(3,[dx, dy, false]);
        posX = touch.pageX; posY = touch.pageY;
    }
}

var orientationchange = function () {
    if (window.orientation%180===0) {
        canvas.width = document.body.clientHeight;
        canvas.height = document.body.clientWidth;
        sendMessage(4, true);
    } else {
        sendMessage(4, false);
        canvas.width = document.body.clientWidth;
        canvas.height = document.body.clientHeight
    }
    initColor();
}

window.addEventListener('orientationchange', orientationchange);
initColor();