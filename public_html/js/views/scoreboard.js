define([
    //'jquery',
    'backbone',
    'tmpl/scoreboard',
    'collections/scores',
], function(
    Backbone,
    tmpl,
    scoreCollection,
    ScoreModel
){
    //ScoreCollection.add({name: "LOL"});
    
    var ScoreView = Backbone.View.extend({
        tag: 'div',
        collection: scoreCollection,
        template: tmpl,
        initialize: function () {
            this.$el.addClass("scoreView");
            this.$el.appendTo('#page');
            scoreCollection.add({name: "Boris", score: 20});
            scoreCollection.add({name: "Alex", score: 45});
            scoreCollection.add({name: "Loure", score: 15});
            scoreCollection.add({name: "Berih", score: 65});
            scoreCollection.add({name: "Tolet", score: 51});
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

