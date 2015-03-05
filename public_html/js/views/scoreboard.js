define([
    //'jquery',
    'backbone',
    'tmpl/scoreboard',
    'collections/scores',
], function(
    Backbone,
    tmpl,
    ScoreCollection,
    ScoreModel
){

    var ScoreView = Backbone.View.extend({

        collection: ScoreCollection,
        template: tmpl,
        initialize: function () {               
        },

        render: function () {
            //ScoreCollection.add({name: "Mikhail"});
            //console.log(this.collection.reset([{name: "tatal", score: 25}]));
            console.log(this.collection.toJSON());
            return this.template(this.collection.toJSON());
        },
        show: function () {
            return this.render();
        },
        hide: function () {
            // TODO
        }

    });

    return new ScoreView();
});

