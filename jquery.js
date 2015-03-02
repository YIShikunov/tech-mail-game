$('.block', document).addClass('block_mod')
      .css('font-size', '100px')
      .height('1000px')
      .show();

$.ajax('/api/user', {
    complete: function(){}
});

function showMainScreen() { // Конструктор экрана "Главный" 
    $page.html(mainTmpl()); // Рендерим шаблон
    // Инициализируем обработчики событий
    $page.find('.js-scoreboard')
        .on('click', showScoreboardScreen);
    $page.find('.js-start-game').on('click', showGameScreen);
}
function hideMainScreen() { // Деструктор экрана "Главный"
    // Удаляем установленные обработчики событий
    $page.find('.js-scoreboard')
        .off('click', showScoreboardScreen);
    $page.find('.js-start-game').off('click', showGameScreen);
}
/* Конструктор экрана "Лучшие игроки" */
function showScoreboardScreen() {
    hideMainScreen();
}
/* Деструктор экрана "Лучшие игроки" */
function hideScoreboardScreen() {}
