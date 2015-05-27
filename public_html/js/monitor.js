var ws;

init = function () {
    ws = new WebSocket('ws://' + location.host + '/gameplay');

    ws.onopen = function (event) {
        //
        var data = {
            type : 0,
            obj : document.cookie.substring(11)
        }
        sendMessage(JSON.stringify(data));
    }

    ws.onmessage = function (event) {
        //
        var obj = JSON.parse(event.data);
        console.log(event.data);
        if (obj.move) {
            if (obj.move == "вверх") {
                arc.dy= - Math.random()*(50);
                arc.dx = 0;
            }
            if (obj.move == "вправо") {
                arc.dy = - Math.random()*(20);
                arc.dx = Math.random()*(5);
            }
            if (obj.move == "влево") {
                arc.dy= - Math.random()*(20);
                arc.dx = -Math.random()*(5);
            }
            clear_graph();
            play=true;
            action();
        }

    }

    ws.onclose = function (event) {
        //
    }

};

sendMessage = function (action) {
    ws.send(action);
}


// booble
var graph = document.getElementById("graph");
var canvas = document.getElementById("field");
var ctxGraph = graph.getContext('2d');
var context = canvas.getContext('2d');
var width = canvas.width;
var height = canvas.height;
context.fillStyle = "green";

//phisics
var speed = 0.2;
var springX = 0.9;
var springY = 0.6;

//bubble
var arc = {
    radius: 50,
    x: canvas.width / 5, 
    y: canvas.height/2,
    dx: 1,
    dy: -5
};

var thumbImg = document.createElement('img');
thumbImg.src = 'images/ball.png';

thumbImg.onload = function() {
    context.save();
    context.beginPath();
    context.arc(25, 25, 25, 0, Math.PI * 2, true);
    context.closePath();
    context.clip();

    context.drawImage(thumbImg, 0, 0, 50, 50);

    context.beginPath();
    context.arc(0, 0, 25, 0, Math.PI * 2, true);
    context.clip();
    context.closePath();
    context.restore();
};

var drag = false;
var play = true;
var time = 0;

var posX = [0,0];
var posY= [0,0];



function clear_graph() {
    ctxGraph.clearRect(0, 0, width, height);
    ctxGraph.strokeStyle = "gray";
    ctxGraph.beginPath();
    ctxGraph.moveTo(width,200);
    ctxGraph.lineTo(0,200);
    ctxGraph.stroke();
    ctxGraph.beginPath();
    ctxGraph.strokeStyle = "blue";
    time=0;
}

function draw_graph() {
    ctxGraph.lineTo(time+=2,200-Math.sqrt(Math.pow(arc.dx,2)+Math.pow(arc.dy,2))*5);
    ctxGraph.stroke();
}

function clear_field() {
    context.clearRect(0, 0, width, height);
}

function draw_bubble() {
    clear_field();
    context.drawImage(thumbImg, arc.x-arc.radius, arc.y-arc.radius, 100, 100);
}

function in_bubble(x,y) {
    dist = Math.sqrt( Math.pow(x-arc.x,2) + Math.pow(y-arc.y,2) );
    return (dist <= arc.radius)? true:false;
}

function action() {
    anime = requestAnimationFrame(action);
    if (!drag && play) {
        if ( arc.y + arc.radius > height ) {   //BOTTOM
            arc.y = height - arc.radius;
            arc.dy *= -springY;
            arc.dx *= springX;
            //console.log("BOTTOM");
            delta = Math.round((arc.dx+arc.dy)*5);
            console.log(Math.round((arc.dx+arc.dy)*5), arc.y + arc.radius - height );
            if ( (delta == 0 || delta == -1) && arc.y + arc.radius - height == 0) {
                console.log("stop");
                play = false;
            }
        } else if ( arc.y - arc.radius < 0) {     //TOP
            arc.y = arc.radius;
            arc.dy = -arc.dy;
            console.log("TOP");
        } else if (arc.x + arc.radius > width ) {  //RIGHT
            arc.x = width - arc.radius;
            arc.dx = -arc.dx;
            console.log("RIGHT");
        } else if ( arc.x - arc.radius < 0) {     //LEFT  
            arc.x = arc.radius;
            arc.dx = -arc.dx;
            console.log("LEFT");
        } else {
            arc.dy += speed;
            arc.x += arc.dx;
            arc.y += arc.dy;
            // console.log("play");
        }
    } else cancelAnimationFrame(anime);
    draw_bubble();
    draw_graph();
}

window.addEventListener("load", function load() {
    draw_bubble();
    clear_graph();
    action();
    window.removeEventListener("load", load, false);

    document.body.addEventListener("dblclick", function(e) {
        cursorX = e.pageX - (innerWidth-width)/2;
        cursorY = e.pageY;
        dist = Math.sqrt( Math.pow(cursorX-arc.x,2) + Math.pow(cursorY-arc.y,2) );
        if (dist <= arc.radius) {
            arc.dy = - Math.random()*(30);
            arc.dx = 0;
            clear_graph();
            time=0;
            play=true;
            drag=false;
            console.log(arc.dy)
            action();
        } 
    });

    document.body.addEventListener("mousedown", function(e) {
        x = e.pageX - (innerWidth-width)/2;
        y = e.pageY;
        if ( in_bubble(x,y) ) {
            dragX = x-arc.x;
            dragY = y-arc.y;
            drag = true;
        }
    });

    document.body.addEventListener("mousemove", function(e) {
        if (drag) {
            arc.x = e.pageX - dragX - (innerWidth-width)/2;
            arc.y = e.pageY - dragY;
            draw_bubble();
            posX[1] = posX[0];
            posX[0] = e.pageX;
            posY[1] = posY[0];
            posY[0] = e.pageX;
        }
    });

    document.body.addEventListener("mouseup", function(e) {
        if (drag) {
            clear_graph();
            arc.dx=posX[0]-posX[1];
            arc.dy=posY[0]-posY[1];
            drag=false;
            play=true;
            action();
        }
    });

}, false);

init();


