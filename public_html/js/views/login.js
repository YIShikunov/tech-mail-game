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
            "submit": "formSubmit"
        },

        initialize: function () {
            this.$el.addClass("gameView__loginView");
            this.$el.appendTo('.gameView');
            this.render();

            if ( $("input[name=login]").value != "" )
                $("input[name=login]").val(localStorage['login'])
            if ( $("input[name=email]").value != "" )
                $("input[name=email]").val(localStorage['email'])

            $("input[name=login]").change(function() {
                localStorage['login'] = this.value});
            $("input[name=email]").change(function() {
                localStorage['email'] = this.value});

        },

        formSubmit: function(e){
            e.preventDefault();
            var data = $("form").serializeArray();
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
            this.$el.hide();
        },

        storage: function (name) {
            alert(name);
        },

    });
    return new LoginView();
});