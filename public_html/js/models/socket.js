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
                alert("Играем с " + data.opponent);
                $(".turn").text("РАССТАВЬТЕ СВОИ ФИШКИ");
            }
            if (data.typeID == 2 && data.opponentReady) {
                if (localStorage['youStart'] == "true") {
                    $(".turn").text("ВАШ ХОД");
                } else {
                    $(".turn").text("ЖДИТЕ");
                }
                alert("Поехали играть!!!");
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

                if (data.recolor) {
                    obj.socket.trigger("changeElem");
                    $(".state").text("Укажите элемент для фигуры на базе");
                } else {
                    if ($(".turn").text() == "ВАШ ХОД") {
                        $(".turn").text("ЖДИТЕ...");
                    } else {
                        $(".turn").text("ВАШ ХОД");
                    }
                    $(".state").text();
                }

            }
            if (data.typeID == 4 && !data.statusOK) {
                $(".state").text(data.errorMessage);
            }

            if (data.typeID == 6) {
                debugger;
                if (data.statusOK) {
                    obj.field.map[1] = data.element;
                    obj.index = data.element;
                    obj.state = "game";
                    obj.drawField(obj.context, obj.field.coords[1]);
                    obj.drawElemInField(obj.field.coords[1][0],obj.field.coords[1][1]);
                    if ($(".turn").text() == "ВАШ ХОД") {
                        $(".turn").text("ЖДИТЕ...");
                    } else {
                        $(".turn").text("ВАШ ХОД");
                    }
                    $(".state").text();
                } else {
                    $(".state").text(data.errorMessage);
                }
            }

            if (data.typeID == 8) {
                debugger;
                if (data.statusOK) {
                    obj.king[0].index = obj.king[0].check;
                    obj.field.map[obj.king[0].pos] = obj.king[0].index;
                    obj.recolor(obj.king[0].pos, obj.king[0].index);
                    obj.state = "game";
                    $(".state").text("Король перекрашен в " + obj.elements[obj.king[0].index].name);
                } else {
                    $(".state").text(data.errorMessage);
                }
            }

            if (data.typeID == 10) {
                index = 0;
                if (!data.isYourKing) index = 1;
                obj.stFld[index].have = [0,0,0,0,0];
                if (data.Elements.length != 0) {
                    for (z=0; z< data.Elements.length; z++) {
                        obj.stFld[index].have[data.Elements[z]] = 1;
                    }
                    if (data.isYourKing) {
                        obj.king[0].index = data.element;
                        obj.recolor(obj.king[0].pos, obj.king[0].index)
                    }
                } else {
                    // if (data.isYourKing) alert("Игра окончена. Вы проиграли"); else alert("Игра окончена. ВЫ ВЫИГРАЛИ!!!");
                    // this.connection.close();
                }
                obj.drawStatus();
            }
            if (data.typeID == -1) {
                if (data.iAmWinner) alert("Игра окончена. ВЫ ВЫИГРАЛИ!!!"); else alert("Игра окончена. Вы проиграли");
                this.connection.close();
            }
        },
        sendMessage: function (data, id) {
            var sendObj = null;

            if (id == 1) {
                sendObj = {
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
                sendObj = {
                    typeID : 3,
                    turn : 0,
                    moveFrom : data[0],
                    moveTo : data[1],
                    statusOK : true,
                };
            };

            if (id == 5) {
                sendObj = {
                    typeID : 5,
                    turn : 0,
                    baseRecolor : data,
                    statusOK : true,
                };
            };

            if (id == 7) {
                sendObj = {
                    typeID : 7,
                    turn : 0,
                    kingRecolor : data,
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