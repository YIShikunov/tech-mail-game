define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var GameView = Backbone.View.extend({
        tagName: "div",
        className: "gameView",
        template: tmpl,

        initialize: function () {
            this.$el.appendTo('#page');
            this.render();
        },

        render: function () {
            this.$el.html(this.template());
        },

        show: function () {
            this.$el.show();
            this.trigger('show',this);
        },

        hide: function () {
            this.$el.hide();
        }

    });

    return new GameView();
});