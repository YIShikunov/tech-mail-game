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
            //alert(this.template());
            console.info("OK");
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }

    });

    return new MainView();
});