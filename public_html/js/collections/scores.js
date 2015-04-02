define([
    'backbone',
    'models/score'
], function(
    Backbone, ScoreModel
){

    var ScoreCollection = Backbone.Collection.extend({
        model: ScoreModel,
        comparator: function(ScoreModel) {
            return -ScoreModel.get("score");
        }
    });

    return new ScoreCollection();
});