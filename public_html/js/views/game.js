define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var GameView = Backbone.View.extend({
        el: $("#page"),
        template: tmpl,
        initialize: function () {
        },
        render: function () {
            //return this.template();
        },
        show: function () {
            this.$el.html(this.template());
            this.$el.show();
        },
        hide: function () {
            //this.$el.hide();
        }

    });

    return new GameView();
});