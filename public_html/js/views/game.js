define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var GameView = Backbone.View.extend({
        tag: 'div',
        template: tmpl,
        initialize: function () {
            this.$el.addClass("gameView");
            this.$el.appendTo('#page');
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