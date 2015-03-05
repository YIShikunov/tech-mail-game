define([
    //'jquery',
    'backbone',
    'tmpl/scoreboard',
    'models/score',
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
            //this.collection.add(ScoreModel);
            //console.log(this.collection.toJSON());
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

