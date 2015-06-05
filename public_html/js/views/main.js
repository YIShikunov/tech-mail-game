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
            this.render(); 
            this.$el.hide();
        },

        render: function () {
            this.$el.html(this.template());
        },

        show: function () {
            if ( document.cookie.length ) {
                $("#game").show();
                $("#logout").show();
                $("#auth").hide();
                $("#login").hide();
            } else {
                $("#game").hide();
                $("#logout").hide();
                $("#auth").show();
                $("#login").show();
            }
            this.$el.show();
            this.trigger('show',this);
        },
        
        hide: function () {
            this.$el.hide();
        }

    });

    return new MainView();
});