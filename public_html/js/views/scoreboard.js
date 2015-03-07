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
    //ScoreCollection.add({name: "LOL"});
    
    var ScoreView = Backbone.View.extend({

        collection: ScoreCollection,
        template: tmpl,
        initialize: function () {               
        },

        render: function () {
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

