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
            this.$el.addClass("gameView__playView");
            this.$el.appendTo('.gameView');
        },
        render: function () {
            this.$el.html(this.template());
        },

        show: function () {
            this.render();
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }

    });

    return new GameView();
});