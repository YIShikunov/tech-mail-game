define([
    'backbone',
    'views/scoreboard',
    'views/main',
    'views/login',
    'views/game'
], function(
    Backbone, scoreView, mainView, loginView, gameView
){

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            '*default': 'defaultActions'
        },
        defaultActions: function () {
            scoreView.hide();
            loginView.hide();
            gameView.hide();
            mainView.show();
        },
        scoreboardAction: function () {
            mainView.hide();
            scoreView.show();
            loginView.hide();
            gameView.hide();
        },
        gameAction: function () {
            mainView.hide();
            scoreView.hide()
            loginView.hide();
            gameView.show();
        },
        loginAction: function () {
            mainView.hide();
            scoreView.hide();
            loginView.show();
            gameView.hide();
        }
    });
    return new Router();
});

