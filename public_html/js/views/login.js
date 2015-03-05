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
    return new LoginView();
});