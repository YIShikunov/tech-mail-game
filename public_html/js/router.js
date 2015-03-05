define([
    'backbone',
    'views/scoreboard',
    'views/main',
    'views/login',
    'views/game'
], function(
    Backbone, ScoreView, MainView, LoginView, GameView
){
    var $page = $('#page'),
    currentScreen = 'main';

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            '*default': 'defaultActions'
        },
        defaultActions: function () {
            showMainScreen();
        },
        scoreboardAction: function () {
            showScoreboardScreen();
        },
        gameAction: function () {
            showGameScreen();
            test();

        },
        loginAction: function () {
            showLoginScreen();
        }
    });

    /* Конструктор экрана "Лучшие игроки" */
    function showScoreboardScreen() {
        hideMainScreen(); // Убиваем экран "Главный"
        currentScreen = 'scoreboard';
        $page.html(ScoreView.show()); // Рендерим шаблон
    }

    /* Деструктор экрана "Лучшие игроки" */
    function hideScoreboardScreen() {
        // Удаляем установленные обработчики событий
        $page.find('.js-back').off('click', showMainScreen);
    }

    /* Конструктор экрана "Игра" */
    function showGameScreen() {
        hideMainScreen(); // Убиваем экран "Главный"
        currentScreen = 'game';
        $page.html(GameView.show()); // Рендерим шаблон
    }

    /* Деструктор экрана "Игра" */
    function hideGameScreen() {
        // Удаляем установленные обработчики событий
        $page.find('.js-back').off('click', showMainScreen);
    }

    /* Конструктор экрана "Авторизация" */
    function showLoginScreen() {
        //hideMainScreen(); // Убиваем экран "Главный"
        currentScreen = 'login';
        $page.html(LoginView.show()); // Рендерим шаблон
    }

    /* Деструктор экрана "Авторизация" */
    function hideLoginScreen() {
        // Удаляем установленные обработчики событий
        $page.find('.js-back').off('click', showMainScreen);
    }

    /* Конструктор экрана "Главный" */
    function showMainScreen() {
         // Убиваем текущий экран
        if (currentScreen === 'scoreboard') {
            hideScoreboardScreen();
        } else if (currentScreen === 'game') {
            hideGameScreen();
        } else if (currentScreen === 'login') {
            hideLoginScreen();
        }
        currentScreen = 'main';
        $page.html(MainView.show()); // Рендерим шаблон
    }

    /* Деструктор экрана "Главный" */
    function hideMainScreen() {
        // Удаляем установленные обработчики событий
        $page.find('.js-scoreboard').off('click', showScoreboardScreen);
        $page.find('.js-game').off('click', showGameScreen);
        $page.find('.js-login').off('click', showLoginScreen);
    }
    return new Router();
});

