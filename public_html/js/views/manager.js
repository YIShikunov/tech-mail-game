define([
    'backbone',
    'views/game',
], function(
    Backbone, gameView
){

    var Manager = Backbone.View.extend({
        views : [],
        initialize: function () {
        },
        render: function () {
        },
        show: function () {
        },
        hide: function () {
            console.info("hide");
        },
        addViews: function(listViews) {
            manager = this;
            _.each(listViews, function(view) {
                manager.views.push(view);
                manager.listenTo(view, 'show', manager.hideAll);
            });
        },
        hideAll: function(obj) {
            _.each(this.views, function(view) {
                if ( view != obj ) view.hide();
            });
        }

    });

    return new Manager();
});

