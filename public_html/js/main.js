var $page = $('#page'),
    currentScreen = 'main';

/* Конструктор экрана "Лучшие игроки" */
function showScoreboardScreen() {
    hideMainScreen(); // Убиваем экран "Главный"
    currentScreen = 'scoreboard';
    $page.html(scoreboardTmpl()); // Рендерим шаблон
    // Инициализируем обработчики событий
    $page.find('.js-back').on('click', showMainScreen);
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
    $page.html(gameTmpl()); // Рендерим шаблон
    // Инициализируем обработчики событий
    $page.find('.js-back').on('click', showMainScreen);
}

/* Деструктор экрана "Игра" */
function hideGameScreen() {
    // Удаляем установленные обработчики событий
    $page.find('.js-back').off('click', showMainScreen);
}

/* Конструктор экрана "Авторизация" */
function showAuthScreen() {
    hideMainScreen(); // Убиваем экран "Главный"
    currentScreen = 'auth';
    $page.html(authTmpl()); // Рендерим шаблон
    // Инициализируем обработчики событий
    $page.find('.js-back').on('click', showMainScreen);
}

/* Деструктор экрана "Авторизация" */
function hideAuthScreen() {
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
    } else if (currentScreen === 'auth') {
        hideAuthScreen();
    }
    currentScreen = 'main';
    $page.html(mainTmpl()); // Рендерим шаблон
    // Инициализируем обработчики событий
    $page.find('.js-scoreboard').on('click', showScoreboardScreen);
    $page.find('.js-game').on('click', showGameScreen);
    $page.find('.js-auth').on('click', showAuthScreen);
}

/* Деструктор экрана "Главный" */
function hideMainScreen() {
    // Удаляем установленные обработчики событий
    $page.find('.js-scoreboard').off('click', showScoreboardScreen);
    $page.find('.js-game').off('click', showGameScreen);
    $page.find('.js-auth').off('click', showAuthScreen);
}

showMainScreen();
window.onhashchange = function() {
    if (currentScreen === 'scoreboard') {
        hideScoreboardScreen();
    } else if (currentScreen === 'game') {
        hideGameScreen();
    } else if (currentScreen === 'auth') {
        hideAuthScreen();
    } else if (currentScreen === 'main') {
	hideMainScreen();
    }
    if (location.hash === '#scoreboard') {
        showScoreboardScreen();
    } else if (location.hash === '#game') {
        showGameScreen();
    } else if (location.hash === '#auth') {
        showAuthScreen();
    } else if (location.hash === '') {
	showMainScreen();
    }
}

