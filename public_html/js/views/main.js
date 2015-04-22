define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
){

    var MainView = Backbone.View.extend({
        template: tmpl,
        initialize: function () {
            this.$el.addClass("gameView__mainView");
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

    return new MainView();
});