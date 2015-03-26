define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
){

    var MainView = Backbone.View.extend({
        el: $("#page"),
        template: tmpl,
        initialize: function () {
            
        },
        render: function () {
        },

        show: function () {
            this.$el.html(this.template());
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }

    });

    return new MainView();
});