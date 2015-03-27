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
        el: $("#page"),
        collection: scoreCollection,
        template: tmpl,
        initialize: function () {   
            //alert("scire");  
            scoreCollection.add({name: "Boris", score: 20});
            scoreCollection.add({name: "Alex", score: 45});
            scoreCollection.add({name: "Loure", score: 15});
            scoreCollection.add({name: "Berih", score: 65});
            scoreCollection.add({name: "Tolet", score: 51});
        },

        render: function () {
            //return this.template(this.collection.toJSON());
        },
        show: function () {
            this.$el.html(this.template(this.collection.toJSON()));
            this.$el.show();
        },
        hide: function () {
            // TODO
        }

    });

    return new ScoreView();
});

