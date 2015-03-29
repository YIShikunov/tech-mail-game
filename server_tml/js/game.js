var ws;
var started = false;
var finished = false;

var myName = "${myName}";
var enemyName = "";

init = function () {
    ws = new WebSocket("ws://localhost:8080/gameplay");

    ws.onopen = function (event) {

    }

    ws.onmessage = function (event) {
        var data = JSON.parse(event.data);
        if(data.status == "start"){
            document.getElementById("wait").style.display = "none";
            document.getElementById("gameplay").style.display = "block";
            document.getElementById("enemyName").innerHTML = data.enemyName;
            document.getElementById("state").innerHTML = "The pieces are in their starting positions.";
        }

        if(data.status == "finish"){
            document.getElementById("gameOver").style.display = "block";
            document.getElementById("gameplay").style.display = "none";
            document.getElementById("state").innerHTML = "A king is captured!";

            if(data.win)
                document.getElementById("win").innerHTML = "winner!";
            else
                document.getElementById("win").innerHTML = "loser!";
        }

        if(data.status == "turn"){
            document.getElementById("state").innerHTML = data.state;
            document.getElementById("message").innerHTML = "A turn is made!";
        }
        if(data.status == "rejected"){
            document.getElementById("message").innerHTML = data.errorMsg;
        }
    }

    ws.onclose = function (event) {

    }

};

function sendMessage(action) {
    ws.send(action);
}
