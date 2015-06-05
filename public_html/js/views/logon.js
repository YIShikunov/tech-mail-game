define([
    'backbone',
    'models/user',
    'tmpl/logon',
], function(
    Backbone,
    UserModel,
    tmpl
){

    var LogonView = Backbone.View.extend({
        template: tmpl,
        user: new UserModel(),

        events: {
            "submit": "formSubmit",
            "keyup" : "storage",
            "change" : "storage", 
        },

        initialize: function () {
            this.$el.addClass("gameView__LogonView");
            this.$el.appendTo('.gameView');
            this.render();

            $(".form_signup input[name=Logon]").val(localStorage['Logon_up']);
            $(".form_signup input[name=email]").val(localStorage['email_up']);
            $(".form_signup input[name=password]").val(localStorage['password_up']);
            $(".form_signin input[name=Logon]").val(localStorage['Logon_in']);
            $(".form_signin input[name=email]").val(localStorage['email_in']);
            $(".form_signin input[name=password]").val(localStorage['password_in']);
            this.$el.hide();
        },

        formSubmit: function(e){
            e.preventDefault();
            if (e.target.className=="form_signup") {
                data = this.$el.find(".form_signup").serializeArray();
                this.user.signup(data);
            } else {
                data = this.$el.find(".form_signin").serializeArray();
                this.user.Logon(data);
            }
        },

        render: function () {
            this.$el.html(this.template());
        },

        show: function () {
            this.$el.show();
            this.trigger('show',this);
        },

        hide: function () {
            this.clearFroms();
            this.$el.hide();
        },

        storage: function (e) {
            var postfix;
            if (e.target.parentElement.parentElement.parentElement.className=="form_signup") {
                postfix = "_up"
            } else {
                postfix = "_in"
            }
            localStorage[e.target.name+postfix] = e.target.value;
        },

        clearFroms: function () {
            $("input[name=Logon]").val("");
            $("input[name=email]").val("");
            $("input[name=password]").val("");
            localStorage.removeItem("Logon_in");
            localStorage.removeItem("email_in");
            localStorage.removeItem("password_in");
            localStorage.removeItem("Logon_up");
            localStorage.removeItem("email_up");
            localStorage.removeItem("password_up");
        },


    });
    return new LogonView();
});