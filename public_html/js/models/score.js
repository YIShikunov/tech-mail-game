define([
    'backbone'
], function(
    Backbone
){

    var ScoreModel = Backbone.Model.extend({
    	initialize: function () {

    	},
    	defaults: {
    		name : "Mikhail",
    		score: 255
    	}
    });

    return new ScoreModel();
});