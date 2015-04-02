define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var GameView = Backbone.View.extend({
        tagName: "div",
        className: "gameView",
        template: tmpl,

        initialize: function () {
            this.$el.appendTo('#page');
            this.render();
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
            var canvas = document.getElementsByClassName("game__field");
            var ctx = canvas[0].getContext('2d');

            ctx.fillStyle = "green";
            ctx.strokeStyle = "white";

            size=30;
            ctr = { x: 800, y: 600}
            pos = [ {x:  -33.45, y:  -212.3, angle:    -117, color:    "red"},
                    {x:   -67.2, y: -278.15, angle:  -54.45, color: "yellow"},
                    {x: -140.35, y:  -290.1, angle:   11.09, color:    "red"},
                    {x:  -159.6, y:  -358.8, angle:   -43.9, color:    "red"},
                    {x: -109.35, y:    -407, angle:    17.8, color: "yellow"},
                    {x:  -120.1, y: -481.55, angle:    82.7, color:    "red"},
                    {x:   -60.5, y: -524.55, angle:   28.44, color:    "red"},
                    {x:   -0.45, y: -488.55, angle:      90, color: "yellow"},
                    {x:   65.75, y:  -520.3, angle:     155, color:    "red"},
                    {x:  120.45, y: -475.75, angle:  100.69, color:    "red"},
                    {x:   110.9, y:  -407.4, angle:  162.09, color: "yellow"},
                    {x:  162.55, y:  -358.3, angle: -134.95, color:    "red"},
                    {x:   139.4, y: -288.75, angle:   170.7, color:    "red"},
                    {x:   67.15, y: -278.95, angle:   -5.74, color: "yellow"},
                    {x:    39.1, y: -212.95, angle:  -62.70, color:    "red"}];

            for (i=0; i<pos.length; i++) 
                this.draw_base(ctx,ctr.x+pos[i].x,ctr.y+pos[i].y,pos[i].angle, size, pos[i].color);
        },  
        draw_base: function(context,x,y,angle, size, color) {
            x *=0.5;
            y *=0.5;
            context.fillStyle = color;
            angle *=-1;
            x1 = x+size*Math.cos(Math.PI*(angle+60)/180);
            y1 = y-size*Math.sin(Math.PI*(angle+60)    /180);
            x2 = x+size*Math.cos(Math.PI*(angle-180)/180);
            y2 = y-size*Math.sin(Math.PI*(angle-180)/180);
            x3 = x+size*Math.cos(Math.PI*(angle-60)/180);
            y3 = y-size*Math.sin(Math.PI*(angle-60)/180);
            context.moveTo( x1, y1);
            context.lineTo( x2, y2);
            context.lineTo( x3, y3);
            context.fill();
            context.stroke();
            context.closePath();
        }

    });

    return new GameView();
});