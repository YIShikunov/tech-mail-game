define([
    'backbone',
    'tmpl/scoreboard',
    'models/score'
], function(
    Backbone,
    tmpl,
    ScoreModel
){

    var ScoreView = Backbone.View.extend({

        model: ScoreModel,
        template: tmpl,
        initialize: function () {
            // TODO
        },

        render: function () {
            return this.template(this.model.toJSON());
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

