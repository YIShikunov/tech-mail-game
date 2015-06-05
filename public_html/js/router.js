define([
    'backbone',
    'views/scoreboard',
    'views/main',
    'views/logon',
    'views/login',
    'views/game',
    'views/manager',
], function(
    Backbone, scoreView, mainView, logonView, loginView, gameView, manager
){
    manager.addViews([
        scoreView, 
        mainView,
        logonView,
        loginView, 
        gameView]);

    var Router = Backbone.Router.extend({
        routes: {
            'game': 'gameAction',
            'logon': 'logonAction',
            'login': 'loginAction',
            'logout': 'logoutAction',
            'scoreboard': 'scoreboardAction',
            '*default': 'defaultActions'
        },
        gameAction: function() {
            gameView.show();
        },
        logonAction: function() {
            logonView.show();
        },
        loginAction: function() {
            loginView.show();
        },
        logoutAction: function() {
            debugger;
            var xmlHttp = new XMLHttpRequest();
            url = "http://" + location.host + "/api/v1/auth/signout";
            xmlHttp.open( "GET", url, false );
            xmlHttp.send( null );
            document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
            window.location = "#";
        },
        defaultActions: function() {
            mainView.show();
        },
        scoreboardAction: function() {
            scoreView.show();
        },
    });
    return new Router();
});

