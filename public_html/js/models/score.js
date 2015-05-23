define([
    'backbone'
], function(
    Backbone
){

    var ScoreModel = Backbone.Model.extend({
        
        defaults: {
            name : "",
            score: 0
        }
        
    });

    return ScoreModel;
});