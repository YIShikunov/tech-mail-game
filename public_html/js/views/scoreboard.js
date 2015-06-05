define([
    'backbone',
    'tmpl/scoreboard',
    'collections/scores',
], function(
    Backbone,
    tmpl,
    ScoreCollection
){
    
    var ScoreView = Backbone.View.extend({
        collection: new ScoreCollection(),
        template: tmpl,

        initialize: function () {
            this.$el.addClass("gameView__scoreView");
            this.$el.appendTo('.gameView');
            this.$el.hide();
            this.render();
        },

        render: function () {
            this.collection.fetch();
            this.$el.html(this.template(this.collection.toJSON()));
        },

        show: function () {
            this.render();
            this.$el.show();
            this.trigger('show',this);
        },

        hide: function () {
            this.$el.hide();
        }

    });

    return new ScoreView();
});

