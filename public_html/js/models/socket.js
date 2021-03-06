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
        // send: function (data) {
        //     this.connection.send(JSON.stringify(data));
        // },
        onConnect: function () {
        },
        onMessage: function (msg) {
            var data = JSON.parse(msg.data.replace('=',','));
            console.info(msg.data);
            if (data.typeID == 0) {
                localStorage['youStart'] = data.youStart;
                alert("connect");
            }
            if (data.typeID == 2 && data.opponentReady) alert("Поехали играть!!!, "+localStorage['youStart'])
            if (data.typeID == 4 && data.statusOK) {
                localStorage['from'] = data.piecesMoved[0]-1;
                localStorage['to'] = data.piecesMoved[1]-1;
                obj.socket.trigger("move");
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