define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var GameView = Backbone.View.extend({

        template: tmpl,
        initialize: function () {
            // TODO
        },
        render: function () {
            return this.template();
        },
        show: function () {
            return this.render();
        },
        hide: function () {
            // TODO
        }

    });

    return new GameView();
});