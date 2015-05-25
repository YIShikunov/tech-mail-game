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
        cloud: null,

        events: {
            'click canvas' : 'gameClick',
            'resize' : 'draw',
            'storage' : 'moving',
            'mousedown' : 'test',
        },

        initialize: function () {
            this.$el.addClass("gameView__playView");
            this.$el.appendTo('.gameView');
            this.render();
            this.$el.hide();
            this.listenTo(this.socket, 'move', this.moving);
            this.drawEnemy();
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

                for (i=0; i<obj.elements.length; i++) {
                    elem = document.createElement('img');
                    elem.src = 'images/piece/'+obj.elements[i].name+'.png';
                    elem.index = i;
                    obj.elements[i].img = elem;
                    elem.onload = function() {
                        panel = obj.panel;
                        obj.drawElemInPanel(this.index);
                        if (obj.elements[obj.index].index == 0)
                            obj.drawSelect(obj.context, panel.x+panel.width/2, obj.elements[obj.index].index*90+60);
                    };
                }
            };
            img.src = 'images/ptrn.jpg';
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
            if (x > panel.x && x < panel.x+panel.width && y > panel.y && y < panel.y+panel.height)
                for (i=0; i<this.elements.length; i++) {
                    if (y < this.elements[i].index*90+140) {
                        this.drawElemInPanel(this.index);
                        this.drawSelect(this.context, panel.x+panel.width/2, this.elements[i].index*90+60)
                        this.index = i;
                        break;
                    }
            } else {
                index = this.field.checkField(x-TRx,y-TRy);
                if (index >= 0) {
                    // Place of elements
                    if ( this.state =="place" && this.field.baseField.indexOf(index) >= 0 && this.field.map[index] == -1) {
                        this.drawField(this.context,coords[index]);
                        // draw element
                        this.drawElemInField(this.field.coords[index][0],this.field.coords[index][1])
                        // change count
                        this.elements[this.index].count--;
                        // save place of element
                        idField = index+1;
                        if (localStorage['youStart'] == "true") idField = this.field.inv[index]+1;
                        this.elements[this.index].place.push(idField);
                        this.field.map[index] = this.index;
                        // change element
                        if (this.elements[this.index].count == 0) {
                            this.drawElemInPanel(this.index);

                            for (z=0; z<this.elements.length; z++)
                                if (this.elements[z].count > 0) {
                                    this.index = z;
                                    break;
                                }
                            if (z==5) {
                                this.state = "game";
                                this.socket.sendMessage(this.elements, 1);
                            }
                        }
                        this.drawElemInPanel(this.index);
                        this.drawSelect(this.context, panel.x+panel.width/2, this.elements[this.index].index*90+60)

                    } else 
                    if (this.field.map[index] != -1 && this.state == "game" ) {
                        this.move = this.field.map[index];
                        this.state = "move";
                        this.from = index;
                        this.index = this.field.map[index];
                    } else 
                    if (this.field.map[index] == -1 && this.state == "move" ) {
                        this.move = this.field.map[this.from];

                        this.state = "game";
                        this.field.map[index] = this.index;

                        data = [];
                        if (localStorage['youStart'] == "false") {
                            data.push(this.from+1);
                            data.push(index+1);
                        } else {
                            data.push(this.field.inv[this.from]+1);
                            data.push(this.field.inv[index]+1);
                        }
                        this.socket.sendMessage(data, 3);
                        this.index = this.field.map[this.from];
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

        drawElemInPanel : function(index) {
            TRx = this.$el.find("canvas")[0].width/2;
            TRy = this.$el.find("canvas")[0].height/2;
            this.context.fillRect(panel.x+10, this.elements[index].index*90+60-8, panel.width-20,  90);
            this.context.drawImage(this.elements[index].img,
                panel.x+panel.width/2-this.elements[index].img.width/2, this.elements[index].index*90+60);
            this.drawCount(this.context, panel.x+panel.width/2, 
                this.elements[index].index*90+135, this.elements[index].count);
        },

        drawElemInField: function(p1,p2) {
            img = this.elements[this.index].img;
            scale = Math.sqrt(3)/3;
            cor = Math.PI/180*30;
            x = (p2[0]-p1[0])*scale*Math.cos(cor) - (p2[1]-p1[1])*scale*Math.sin(cor) + TRx + p1[0];
            y = (p2[1]-p1[1])*scale*Math.cos(cor) + (p2[0]-p1[0])*scale*Math.sin(cor) + TRy + p1[1];
            this.context.drawImage(img,x-img.width/2*30/img.width,y-img.height/2*30/img.height,30,30);
        },

        drawEnemy: function() {
            TRx = this.$el.find("canvas")[0].width/2;
            TRy = this.$el.find("canvas")[0].height/2;
            var cloud = document.createElement('img');
            cloud.onload = function() {
                for (l=0; l<obj.field.baseField.length; l++) {
                    p1 = obj.field.coords[obj.field.inv[obj.field.baseField[l]]][0];
                    p2 = obj.field.coords[obj.field.inv[obj.field.baseField[l]]][1];
                    scale = Math.sqrt(3)/3;
                    cor = Math.PI/180*30;
                    x = (p2[0]-p1[0])*scale*Math.cos(cor) - (p2[1]-p1[1])*scale*Math.sin(cor) + TRx + p1[0];
                    y = (p2[1]-p1[1])*scale*Math.cos(cor) + (p2[0]-p1[0])*scale*Math.sin(cor) + TRy + p1[1];
                    obj.context.drawImage(this,x-this.width/2*30/this.width,y-this.height/2*30/this.height,30,30);
                    obj.field.map[obj.field.inv[obj.field.baseField[l]]] = 5;
                }
            };
            cloud.src = 'images/piece/cloud.png';
            obj.cloud = cloud;
        },

        drawSelect: function(context,x,y) {
            ptrn = context.fillStyle;
            context.beginPath();
            context.fillStyle = ptrn;
            context.arc(x,y+38  ,42,0,2*Math.PI);
            context.stroke();
        },

        moving: function() {
            from = localStorage['from'];
            to = localStorage['to'];
            if (localStorage['youStart'] == "true") {
                from = this.field.inv[localStorage['from']];
                to = this.field.inv[localStorage['to']];
            }

            localStorage.removeItem("from");
            localStorage.removeItem("to");

            this.index = this.field.map[from];
            this.drawField(this.context,coords[from]);
            if (this.index != 5) {
                this.drawElemInField(this.field.coords[to][0],this.field.coords[to][1])
            } else {
                debugger;
                p1 = obj.field.coords[to][0];
                p2 = obj.field.coords[to][1];
                img = this.cloud;
                scale = Math.sqrt(3)/3;
                cor = Math.PI/180*30;
                x = (p2[0]-p1[0])*scale*Math.cos(cor) - (p2[1]-p1[1])*scale*Math.sin(cor) + TRx + p1[0];
                y = (p2[1]-p1[1])*scale*Math.cos(cor) + (p2[0]-p1[0])*scale*Math.sin(cor) + TRy + p1[1];
                this.context.drawImage(img,x-img.width/2*30/img.width,y-img.height/2*30/img.height,30,30);
            }

            st = this.field.map[from];
            this.field.map[from] = -1;
            this.field.map[to] = st;
        },

        test: function(event) {
            if ( event.which == 2 ) {
                if (localStorage['youStart'] == "true") {
                    this.elements[0].place = [12,5,11];
                    this.elements[1].place = [10,4,9];
                    this.elements[2].place = [8,3,17];
                    this.elements[3].place = [16,7,15];
                    this.elements[4].place = [14,6,13];
                } else {
                    this.elements[0].place = [27,20,26];
                    this.elements[1].place = [25,19,24];
                    this.elements[2].place = [23,18,32];
                    this.elements[3].place = [31,22,30];
                    this.elements[4].place = [29,21,28];
                }
                for (i=0; i<this.elements.length; i++) {
                    this.index = i;    
                    for (k=0; k<this.elements[i].place.length; k++) {
                        pos = this.elements[i].place[k]-1;
                        if (localStorage['youStart'] == "true") pos = this.field.inv[pos];
                        this.drawElemInField(this.field.coords[pos][0],this.field.coords[pos][1])
                        this.field.map[pos] = i;
                    }
                }
                this.state = "game";
                this.socket.sendMessage(this.elements, 1);
            }
        },


    });



    return new GameView();
});