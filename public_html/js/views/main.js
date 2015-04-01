define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
){

    var MainView = Backbone.View.extend({
        tag: 'div',
        template: tmpl,
        initialize: function () {
            this.$el.addClass("mainView");
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

    return new MainView();
});