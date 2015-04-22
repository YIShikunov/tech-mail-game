define([
	'backbone',
	'models/score'
], function(
    Backbone, ScoreModel
){
    
    var ScoreCollection = Backbone.Collection.extend({
		model: ScoreModel,
		comparator: function(scoreModel) {
			return -scoreModel.get("score");
		}
    });

    return ScoreCollection;
});