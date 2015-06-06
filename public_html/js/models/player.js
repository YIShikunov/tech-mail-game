define([
    'backbone',
    'sync/gameSync',
], function (
    Backbone, 
    gameSync
) {
    var PlayerModel = Backbone.Model.extend({
        sync: gameSync,

        initialize: function () {
        },

        getList: function (data) {
            this.fetch();
            return this.data;
        },

        startGame: function (opponent) {
            this.opponent = opponent;
            this.save();
        }

    });

    return PlayerModel;

});