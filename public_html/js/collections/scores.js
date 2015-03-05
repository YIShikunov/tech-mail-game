define([
	'backbone',
	'models/score'
], function(
    Backbone, ScoreModel
){

    var ScoreCollection = Backbone.Collection.extend({
    	initialize: function () {
		},
		model: ScoreModel,
    });

    return new ScoreCollection();
});