define([
    'backbone',
    'views/scoreboard',
    'views/main',
    'views/login',
    'views/game',
    'views/manager',
], function(
    Backbone, scoreView, mainView, loginView, gameView, manager
){
    manager.addViews([
        scoreView, 
        mainView, 
        loginView, 
        gameView]);

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            '*default': 'defaultActions'
        },
        defaultActions: function() {
            mainView.show();
        },
        scoreboardAction: function() {
            scoreView.show();
        },
        gameAction: function() {
            gameView.show();
        },
        loginAction: function() {
            loginView.show();
        }
    });
    return new Router();
});

