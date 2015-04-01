define([
    'backbone',
    'tmpl/auth'
], function(
    Backbone,
    tmpl
){

    var LoginView = Backbone.View.extend({
        tagName: "div",
        className: "loginView",
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
    return new LoginView();
});