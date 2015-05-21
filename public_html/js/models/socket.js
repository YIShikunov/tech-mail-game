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
            document.getElementById("prepare").style.display = "none";
            document.getElementById("wait").style.display = "block";
        //     this.status = 1;
        //     var sendObj = {
        //         type: 'gameInfo',
        //         body: {
        //             players: "123"
        //         }
        //     };
        //     this.send(sendObj);
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
        }
    });

    return GameModel;
});