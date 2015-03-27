define([
    'backbone',
    'tmpl/auth'
], function(
    Backbone,
    tmpl
){

    var LoginView = Backbone.View.extend({
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
            // TODO
        }

    });
    return new LoginView();
});