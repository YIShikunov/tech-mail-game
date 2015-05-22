define([
    'backbone',
    'models/field',
    'models/socket',
    'tmpl/game',
], function(
    Backbone,
    FieldModel,
    Socket,
    tmpl
){

    var GameView = Backbone.View.extend({
        template: tmpl,
        field: new FieldModel(),
        context: null,
        panel: {x: 100, y: 50, width: 150, height: 460},
        elements:[  {name: "fire",  index: 0, count: 3, place: []},
                    {name: "metal", index: 1, count: 3, place: []},
                    {name: "wood",  index: 2, count: 3, place: []},
                    {name: "earth", index: 3, count: 3, place: []},
                    {name: "water", index: 4, count: 3, place: []}],
        index: 0,
        state: "place",
        move: -1,
        from: -1,
        socket: new Socket(),

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
            this.socket.connect();
        },

        hide: function () {
            this.$el.hide();
        },
        
        draw: function() {
            var canvas = this.$el.find("canvas")[0];
            canvas.width = window.innerWidth;
            var context = canvas.getContext('2d');
            this.context = context;
            context.strokeStyle = "#09034A";
            context.lineWidth = 4;
            
            obj = this;
            coords = this.field.coords;

            var img = document.createElement('img');
            img.src = 'images/ptrn.jpg';
            img.onload = function() {
                var ptrn = context.createPattern(img,"repeat");
                context.fillStyle=ptrn;
                panel = obj.panel;
                context.beginPath();
                context.rect( panel.x, panel.y, panel.width, panel.height);
                context.closePath();
                context.fill();
                context.stroke();
                context.textAlign = 'center';
                context.font = "bold 16pt Calibri";
                context.fillText("Элементы", panel.x+panel.width/2, panel.y-10);

                for (i=0; i<coords.length; i++) {
                    obj.drawField(context,coords[i]);
                }
            };

            for (i=0; i<this.elements.length; i++) {
                elem = document.createElement('img');
                elem.src = 'images/piece/'+this.elements[i].name+'.png';
                elem.index = i;
                this.elements[i].img = elem;
                elem.onload = function() {
                    panel = obj.panel;
                    context.drawImage(this,panel.x+panel.width/2-this.width/2, obj.elements[this.index].index*90+60);
                    obj.drawCount(context, panel.x+panel.width/2, 
                        obj.elements[this.index].index*90+135, obj.elements[this.index].count);
                    if (obj.elements[obj.index].index == 0)
                        obj.drawSelect(obj.context, panel.x+panel.width/2, obj.elements[obj.index].index*90+60);
                };
            }


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
            x = event.pageX - coords.left;
            y = event.pageY - coords.top;
            panel = panel;
            if (x > panel.x && x < panel.x+panel.width && 
                y > panel.y && y < panel.y+panel.height)
                for (i=0; i<this.elements.length; i++) {
                    if (y < this.elements[i].index*90+60+80) {
                        this.context.fillRect(panel.x+10, this.elements[this.index].index*90+60-8, 
                            panel.width-20,  90);
                        this.context.drawImage(this.elements[this.index].img,
                            panel.x+panel.width/2-this.elements[this.index].img.width/2, obj.elements[this.index].index*90+60);
                        this.drawCount(this.context, panel.x+panel.width/2, 
                            obj.elements[this.index].index*90+135, obj.elements[this.index].count);
                        this.drawSelect(this.context, 
                            panel.x+panel.width/2, this.elements[i].index*90+60)
                        this.index = i;
                        break;
                    }
            } else {
                TRx = this.$el.find("canvas")[0].width/2;
                TRy = this.$el.find("canvas")[0].height/2;
                index = this.field.checkField(x-TRx,y-TRy);
                if (index >= 0) {
                    if (this.field.baseField.indexOf(index) >= 0 && this.field.map[index] == -1 && this.state =="place" ) {
                        this.drawField(this.context,coords[index]);
                        coords = this.field.coords;
                        p1 = coords[index][0];
                        p2 = coords[index][1];
                        img = this.elements[this.index].img;
                        xxx = Math.sqrt(3)/3;
                        cor = Math.PI/180*30;
                        x = (p2[0]-p1[0])*xxx*Math.cos(cor) - (p2[1]-p1[1])*xxx*Math.sin(cor) + TRx + p1[0];
                        y = (p2[1]-p1[1])*xxx*Math.cos(cor) + (p2[0]-p1[0])*xxx*Math.sin(cor) + TRy + p1[1];

                        this.context.drawImage(img,x-img.width/2*30/img.width,y-img.height/2*30/img.height,30,30);
                        this.elements[this.index].count--;
                        // for opponent this.field.inv[index]
                        this.elements[this.index].place.push(index);
                        this.field.map[index] = this.index;

                        if (this.elements[this.index].count == 0) {
                            this.context.fillRect(panel.x+10, this.elements[this.index].index*90+60-8, 
                            panel.width-20,  90);
                        this.context.drawImage(this.elements[this.index].img,
                            panel.x+panel.width/2-this.elements[this.index].img.width/2, obj.elements[this.index].index*90+60);
                        this.drawCount(this.context, panel.x+panel.width/2, 
                            obj.elements[this.index].index*90+135, obj.elements[this.index].count);

                            for (z=0; z<5; z++)
                                if (this.elements[z].count > 0) {
                                    this.index = z;
                                    break;
                                }
                            if (z==5) {
                                this.state = "game";
                                alert("SEND TO SERVER");
                                for (k=0; k<this.elements.length; k++) 
                                    console.info(this.elements[k].place.sort());
                                this.socket.sendMessage(0);
                            }
                            
                        }
                        
                        this.context.fillRect(panel.x+10, this.elements[this.index].index*90+60-8, 
                            panel.width-20,  90);
                        this.context.drawImage(this.elements[this.index].img,
                            panel.x+panel.width/2-this.elements[this.index].img.width/2, obj.elements[this.index].index*90+60);
                        this.drawCount(this.context, panel.x+panel.width/2, 
                            obj.elements[this.index].index*90+135, obj.elements[this.index].count);
                        this.drawSelect(this.context, 
                            panel.x+panel.width/2, this.elements[this.index].index*90+60)
                    } else if (this.field.map[index] != -1 && this.state == "game" ) {
                        this.move = this.field.map[index];
                        this.state = "move";
                        this.from = index;
                        this.index = this.field.map[index];
                        // alert("move");
                    } else if (this.field.map[index] == -1 && this.state == "move" ) {
                        this.move = this.field.map[index];
                        this.state = "game";
                        this.field.map[this.from] = -1;
                        this.field.map[index] = this.index; 
                        // alert("place");
                        this.drawField(this.context,coords[this.from]);
                        coords = this.field.coords;
                        p1 = coords[index][0];
                        p2 = coords[index][1];
                        img = this.elements[this.index].img;
                        xxx = Math.sqrt(3)/3;
                        cor = Math.PI/180*30;
                        x = (p2[0]-p1[0])*xxx*Math.cos(cor) - (p2[1]-p1[1])*xxx*Math.sin(cor) + TRx + p1[0];
                        y = (p2[1]-p1[1])*xxx*Math.cos(cor) + (p2[0]-p1[0])*xxx*Math.sin(cor) + TRy + p1[1];

                        this.context.drawImage(img,x-img.width/2*30/img.width,y-img.height/2*30/img.height,30,30);
                        this.elements[this.index].count--;
                        // for opponent this.field.inv[index]
                        this.elements[this.index].place.push(index);
                        this.field.map[index] = this.index;

                        if (this.elements[this.index].count == 0) {
                            this.context.fillRect(panel.x+10, this.elements[this.index].index*90+60-8, 
                            panel.width-20,  90);
                        this.context.drawImage(this.elements[this.index].img,
                            panel.x+panel.width/2-this.elements[this.index].img.width/2, obj.elements[this.index].index*90+60);
                        this.drawCount(this.context, panel.x+panel.width/2, 
                            obj.elements[this.index].index*90+135, obj.elements[this.index].count);
                        }

                    } 
                }
            }
        },

        drawCount: function(context,x,y,count) {
            context.font = "bold 16pt Calibri";
            ptrn = context.fillStyle;
            context.fillStyle = "#000";
            context.fillText(count, x, y);
            context.fillStyle = ptrn;
        },

        drawSelect: function(context,x,y) {
            ptrn = context.fillStyle;
            context.beginPath();
            context.fillStyle = ptrn;
            context.arc(x,y+38  ,42,0,2*Math.PI);
            context.stroke();
        },

    });



    return new GameView();
});