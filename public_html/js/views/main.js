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

            success = function (resp) {
                if (resp.code == 0) {
                    if ( !resp.loggedIn ) {
                        document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                        $("#game").hide();
                        $("#logout").hide();
                        $("#logon").show();
                        $("#login").show();
                    } else {
                        $("#game").show();
                        $("#logout").show();
                        $("#logon").hide();
                        $("#login").hide();
                    }
                }
            };

            var xhr = $.ajax({
                type: "GET",
                url: "/api/v1/auth/isloggedin",
                dataType: 'json'
            }).done(success);
        },

        render: function () {
            this.$el.html(this.template());
        },

        show: function () {
            if ( document.cookie.length ) {
                $("#game").show();
                $("#logout").show();
                $("#logon").hide();
                $("#login").hide();
            } else {
                $("#game").hide();
                $("#logout").hide();
                $("#logon").show();
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