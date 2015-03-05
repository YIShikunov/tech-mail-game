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

    return new MainView();
});