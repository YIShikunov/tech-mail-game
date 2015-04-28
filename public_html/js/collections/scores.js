define([
    'backbone',
    'models/score',
    'sync/scoreSync'
], function(
    Backbone, ScoreModel, scoreSync
){
    
    var ScoreCollection = Backbone.Collection.extend({
		model: ScoreModel,
        sync: scoreSync,

		comparator: function(scoreModel) {
			return -scoreModel.get("score");
		}
        
    });

    return ScoreCollection;
});