define([
	'backbone',
	'models/score'
], function(
    Backbone, ScoreModel
){

    var ScoreCollection = Backbone.Collection.extend({
		model: ScoreModel
    });

    console.log(ScoreModel);

    return new ScoreCollection(ScoreModel);
});