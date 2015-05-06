define([
    'backbone',
    'models/field',
    'tmpl/game'
], function(
    Backbone,
    FieldModel,
    tmpl
){

    var GameView = Backbone.View.extend({
        template: tmpl,
        field: new FieldModel(),
        context: null,

        events: {
            'click canvas' : 'gameClick',
            'resize' : 'draw'
        },

        initialize: function () {
            this.$el.addClass("gameView__playView");
            this.$el.appendTo('.gameView');
            this.render();
            this.$el.hide();
        },

        render: function () {
            this.$el.html(this.template());
            this.draw();
        },

        show: function () {
            this.$el.show();
            this.trigger('show',this);
        },

        hide: function () {
            this.$el.hide();
        },
        
        draw: function() {
            var canvas = this.$el.find("canvas")[0];
            canvas.width = window.innerWidth;
            var ctx = canvas.getContext('2d');
            this.context = ctx;
            ctx.strokeStyle = "#09034A";
            ctx.lineWidth = 4;
            
            obj = this;
            coords = this.field.get('coords');

            var subField = document.createElement('img');
            subField.src = 'images/water.png';
            subField.onload = function() {
                var ptrn = ctx.createPattern(subField,"repeat");
                ctx.fillStyle=ptrn;
                //ctx.transport(0,-150);

                for (i=0; i<coords.length; i++) {
                    obj.drawField(ctx,coords[i]);
                }
            };

            var img = document.createElement('img');
            img.src = 'images/ptrn.jpg';
            img.onload = function() {
                var ptrn = ctx.createPattern(img,"repeat");
                ctx.fillStyle=ptrn;
            };
        },

        drawField: function(context,points) {
            cX = this.$el.find("canvas")[0].width/2;
            cY = this.$el.find("canvas")[0].height/2;
            scale = 1;
            context.beginPath();
            context.moveTo(scale*points[0][0]+cX, scale*points[0][1]+cY);
            for (k=1; k<points.length; k++) {
                context.lineTo(scale*points[k][0]+cX, scale*points[k][1]+cY);
            }
            context.closePath();
            context.fill();
            context.stroke();
        },

        gameClick: function(event) {
            coords = this.$el.find("canvas").offset()
            x = event.pageX - coords.left-this.$el.find("canvas")[0].width/2;
            y = event.pageY - coords.top-this.$el.find("canvas")[0].height/2;
            index = this.field.checkField(x,y);
            if (index >= 0) 
                this.drawField(this.context,coords[i]);
        }

    });

    return new GameView();
});