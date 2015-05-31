define([
    'backbone',
], function (Backbone) {

    var GameModel = Backbone.Model.extend({
        initialize: function () {
            this.connection = undefined;
            // _.bindAll(this);
        },
        connect: function () {
            if (this.connection === undefined) {
                this.connection = new WebSocket('ws://' + location.host + '/gameplay');
            }
            this.connection.onopen = this.onConnect;
            this.connection.onmessage = this.onMessage;

            this.connection.onclose = function (event) {

            }

        },
        
        onConnect: function () {
        },
        onMessage: function (msg) {
            var data = JSON.parse(msg.data);
            console.info(msg.data);
            if (data.typeID == 0) {
                localStorage['youStart'] = data.youStart;
                alert("connect");
                $(".turn").text("РАССТАВЬТЕ СВОИ ФИШКИ");
            }
            if (data.typeID == 2 && data.opponentReady) {
                if (localStorage['youStart'] == "true") {
                    $(".turn").text("ВАШ ХОД");
                } else {
                    $(".turn").text("ЖДИТЕ");
                }
                alert("Поехали играть!!!")
            }
            if (data.typeID == 4 && data.statusOK) {
                if (data.piecesRevealed.length > 0) {
                    obj.socket.trigger("reveal", data.piecesRevealed);
                }
                if (data.piecesDestroyed.length > 0) {
                    obj.socket.trigger("destroy", data.piecesDestroyed);
                }
                if (data.piecesMoved.length > 0) {
                    obj.socket.trigger("move", {from : data.piecesMoved[0][0]-1, to : data.piecesMoved[0][1]-1});
                }
                if ($(".turn").text() == "ВАШ ХОД") {
                    $(".turn").text("ЖДИТЕ...");
                } else {
                    $(".turn").text("ВАШ ХОД");
                }
                $(".state").text();

            }
            if (data.typeID == 4 && !data.statusOK) {
                $(".state").text(data.errorMessage);

            }
            // if (data.type === 'end') {
            //     this.connection.close();
            // }
        },
        sendMessage: function (data, id) {
            var sendObj = null;

            if (id == 1) {
                var sendObj = {
                    typeID : 1, // 1=pieces init
                    element0 : data[0].place,
                    element1 : data[1].place,
                    element2 : data[2].place,
                    element3 : data[3].place,
                    element4 : data[4].place,
                    statusOK : true,
                };
            };

            if (id == 3) {
                var sendObj = {
                    typeID : 3,
                    turn : 0,
                    moveFrom : data[0],
                    moveTo : data[1],
                    statusOK : true,
                };
            };

            this.connection.send(JSON.stringify(sendObj));
        },
        thisReturn: function () {
            return this;
        },
    });

    return GameModel;
});