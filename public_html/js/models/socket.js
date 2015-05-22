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
            var data = JSON.parse(msg.data);
            console.info(msg.data);
            // var data = JSON.parse(msg.data);
            // alert("GEET");
            // console.info(data);
            // if (data.type === 'start') {
            // }
            // if (data.type === 'end') {
            //     this.connection.close();
            // }
        },
        sendMessage: function (data) {
            var sendObj = {
                typeID : 1, // 1=pieces init
                element0 : [18, 21, 28],
                element1 : [17, 24, 31],
                element2 : [19, 22, 23],
                element3 : [20, 25, 27],
                element4 : [26, 29, 30],
                statusOK : true,
            };
            this.connection.send(JSON.stringify(sendObj));
        }
    });

    return GameModel;
});