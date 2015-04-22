define([
    'backbone',
    'tmpl/auth'
], function(
    Backbone,
    tmpl
){

    var LoginView = Backbone.View.extend({
        template: tmpl,
        initialize: function () {
            this.$el.addClass("gameView__loginView");
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
    return new LoginView();
});