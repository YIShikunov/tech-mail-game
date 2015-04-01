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
        tagName: "div",
        className: "scoreView",
        collection: scoreCollection,
        template: tmpl,

        initialize: function () {
            this.$el.appendTo('#page');
            scoreCollection.add({name: "Boris", score: 20});
            scoreCollection.add({name: "Alex", score: 45});
            scoreCollection.add({name: "Loure", score: 15});
            scoreCollection.add({name: "Berih", score: 65});
            scoreCollection.add({name: "Tolet", score: 51});
            this.render();
        },

        render: function () {
            this.$el.html(this.template(this.collection.toJSON()));
        },

        show: function () {
            this.$el.show();
            this.trigger('show',this);
            
        },

        hide: function () {
            this.$el.hide();
        }

    });

    return new ScoreView();
});

