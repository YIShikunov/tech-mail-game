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

            this.connection.onerror=function(event){
                alert("Связь с сервером потеряна. Обновите страницу");
            }

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

                var but = document.createElement("button");
                but.innerHTML ="Расставить в случайной последовательности";
                but.style.background = "deepskyblue";
                but.onclick = function() {
                    plasement = obj.field.baseField;

                    for (i=0; i<5; i++) {
                        obj.index = i;
                        for (k=0; k<3; k++) {
                            random = Math.floor(Math.random() * plasement.length)
                            pos = plasement[random];
                            plasement.splice(random, 1);
                            obj.drawElemInField(obj.field.coords[pos][0],obj.field.coords[pos][1])
                            obj.field.map[pos] = i;
                            if (localStorage['youStart'] == "true") pos = obj.field.inv[pos];
                            obj.elements[i].place.push(pos+1);
                        }
                    }
                    obj.state = "game";
                    obj.socket.sendMessage(obj.elements, 1);
                    obj.index = 0;
                    obj.field.map[1] = 0;
                    obj.drawElemInField(obj.field.coords[1][0],obj.field.coords[1][1]);
                    obj.context.clearRect(obj.panel.x-10, obj.panel.y-50, obj.panel.width+20, obj.panel.height+100);
                    obj.drawStatus();
                    this.remove();
                };
                $(".state").append(but);
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
                    debugger;
                    if (obj.move != 0) {
                        obj.socket.trigger("changeElem");
                        $(".state").text("Укажите элемент для фигуры на базе");
                    } else {
                        el = obj.field.map[0];
                        if ( el < 0 ) el +=5;
                        obj.socket.sendMessage(el,5);
                        if ($(".turn").text() == "ВАШ ХОД") {
                            $(".turn").text("ЖДИТЕ... Соперник думает как сходить");
                        } else {
                            $(".turn").text("ВАШ ХОД");
                        }
                    }
                } else {
                    if ($(".turn").text() == "ВАШ ХОД") {
                        $(".turn").text("ЖДИТЕ... Соперник думает как сходить");
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
                if (data.statusOK && obj.move != 0) {
                    obj.field.map[1] = data.element;
                    obj.index = data.element;
                    obj.state = "game";
                    obj.drawField(obj.context, obj.field.coords[1]);
                    obj.drawElemInField(obj.field.coords[1][0],obj.field.coords[1][1]);
                    if ($(".turn").text() == "ВАШ ХОД") {
                        $(".turn").text("ЖДИТЕ... Соперник думает как сходить");
                    } else {
                        $(".turn").text("ВАШ ХОД");
                    }
                    $(".state").text();
                } else {
                    $(".state").text(data.errorMessage);
                }
            }

            if (data.typeID == 8) {
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
                        obj.field.map[obj.king[0].pos] = obj.king[0].index;
                        obj.recolor(obj.king[0].pos, obj.king[0].index)
                    }
                }
                obj.drawStatus();
            }
            if (data.typeID == -1) {
                if (data.iAmWinner) {
                    obj.stFld[1].have = [0,0,0,0,0];
                    obj.drawStatus();
                    alert("Игра окончена. ВЫ ВЫИГРАЛИ!!!"); 
                }else {
                    obj.stFld[0].have = [0,0,0,0,0];
                    obj.drawStatus();
                    alert("Игра окончена. Вы проиграли");
                }
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
                $(".turn").text("СОПЕРНИК РАССТАВЛЯЕТ ФИШКИ");
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