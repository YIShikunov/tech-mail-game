define([
    'backbone',
    'models/user',
    'tmpl/auth',
], function(
    Backbone,
    UserModel,
    tmpl
){

    var LoginView = Backbone.View.extend({
        template: tmpl,
        user: new UserModel(),

        events: {
            "submit": "formSubmit",
            "keyup" : "storage",
            "change" : "storage", 
        },

        initialize: function () {
            this.$el.addClass("gameView__loginView");
            this.$el.appendTo('.gameView');
            this.render();

            $("input[name=login]").val(localStorage['login']);
            $("input[name=email]").val(localStorage['email']);
            $("input[name=password]").val(localStorage['password']);
        },

        formSubmit: function(e){
            e.preventDefault();
            data = this.$el.find("form").serializeArray();
            debugger;   
            this.user.signup(data);
        },

        render: function () {
            this.$el.html(this.template());
        },

        show: function () {

            this.$el.show();
            this.trigger('show',this);
        },

        hide: function () {
            $("input[name=login]").val("");
            $("input[name=email]").val("");
            $("input[name=password]").val("");
            localStorage.removeItem("login");
            localStorage.removeItem("email");
            localStorage.removeItem("password");
            this.$el.hide();
        },

        storage: function (e) {
            localStorage[e.target.name] = e.target.value;
        },


    });
    return new LoginView();
});