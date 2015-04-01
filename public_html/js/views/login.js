define([
    'backbone',
    'tmpl/auth'
], function(
    Backbone,
    tmpl
){

    var LoginView = Backbone.View.extend({
        tag: 'div',
        template: tmpl,
        initialize: function () {
            this.$el.addClass("loginView");
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
    return new LoginView();
});