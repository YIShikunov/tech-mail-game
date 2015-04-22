define([
    'backbone',
    'tmpl/scoreboard',
    'collections/scores',
], function(
    Backbone,
    tmpl,
    ScoreCollection
){
    
    var ScoreView = Backbone.View.extend({
        collection: new ScoreCollection(),
        template: tmpl,
        initialize: function () {
            this.$el.addClass("gameView__scoreView");
            this.$el.appendTo('.gameView');
            this.collection.set([
                {name: "Boris", score: 20},
                {name: "Alex",  score: 45},
                {name: "Loure", score: 15},
                {name: "Berih", score: 65},
                {name: "Tolet", score: 51}
                ]);
        },

        render: function () {
            this.$el.html(this.template(this.collection.toJSON()));
        },
        show: function () {
            this.render();
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }

    });

    return new ScoreView();
});

