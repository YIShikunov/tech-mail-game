define([
    'backbone',
    'models/field',
    'models/socket',
    'models/player',
    'tmpl/game',
], function(
    Backbone,
    FieldModel,
    Socket,
    Player,
    tmpl
){

    var GameView = Backbone.View.extend({
        template: tmpl,
        field: new FieldModel(),
        context: null,
        players: new Player(),
        waitPlrs: [],
        panel: {x: 100, y: 50, width: 150, height: 460},
        elements:[  {name: "fire",  index: 0, count: 3, place: []},
                    {name: "metal", index: 1, count: 3, place: []},
                    {name: "wood",  index: 2, count: 3, place: []},
                    {name: "earth", index: 3, count: 3, place: []},
                    {name: "water", index: 4, count: 3, place: []}],
        king:[  {name: "King_base",  pos: 1, index: 0, check:0, elem: false, x: 0, y: 0},
                {name: "King_base",  pos: 0, index: 5, x: 0, y: 0}],
        base: { centers: [], elem: [0,3,1,4,2]},
        index: 0,
        state: "find",
        timer: 0,
        move: -1,
        from: -1,
        socket: new Socket(),
        cloud: null,
        scale: 1.15,        
        stFld: [ {field : [], centers: [], elem: [0,2,4,1,3], have: [1,1,1,1,1], rotate: false}, 
                 {field : [], centers: [], elem: [3,1,4,2,0], have: [1,1,1,1,1], rotate: true}],

        events: {
            'click canvas' : 'gameClick',
            'resize' : 'draw',
            'storage' : 'moving',
            'click .choose' : 'startGame',
        },

        initialize: function () {
            this.$el.addClass("gameView__playView");
            this.$el.appendTo('.gameView');
            this.render();
            this.$el.hide();
            
            this.listenTo(this.socket, 'move', this.moving);
            this.listenTo(this.socket, 'destroy', this.destroy);
            this.listenTo(this.socket, 'reveal', this.reveal);
            this.listenTo(this.socket, 'changeElem', this.changeElem);
        },

        render: function () {
            this.$el.html(this.template(this.waitPlrs));
            this.draw();
        },

        show: function () {
            this.$el.show();
            $(".game").hide();
            this.timer = setInterval(this.lobby, 1000);
            this.trigger('show',this);
            this.socket.connect();
        },

        hide: function () {
            this.$el.hide();
        },
        
        draw: function() {
            var canvas = this.$el.find("canvas")[0];
            canvas.width = window.innerWidth;
            var TRx = this.$el.find("canvas")[0].width/2;
            var TRy = this.$el.find("canvas")[0].height/2;
            var context = canvas.getContext('2d');
            this.context = context;
            context.strokeStyle = "#09034A";
            context.lineWidth = 4;
            
            obj = this;
            coords = this.field.coords;

            // elements for king
            points = this.field.coords[1];
            cX = this.$el.find("canvas")[0].width/2;
            cY = this.$el.find("canvas")[0].height/2;
            scale = obj.scale*2;
            size = scale*90;
            
            for (k=0; k<points.length; k++) {
                this.stFld[0].field.push([(-scale*points[k][0]-this.$el.find("canvas")[0].width/1.6)/obj.scale, scale*points[k][1]/obj.scale])
            }
            dx = (this.stFld[0].field[1][0]-this.stFld[0].field[0][0])*obj.scale;
            dy = (this.stFld[0].field[1][1]-this.stFld[0].field[0][1])*obj.scale;
            R = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2))*(Math.sqrt(10)*Math.sqrt(5+Math.sqrt(5))/10);
            this.stFld[0].x = this.stFld[0].field[0][0]*obj.scale+cX+R;
            this.stFld[0].y = this.stFld[0].field[0][1]*obj.scale+cY;

            points = this.field.coords[0];
            for (k = points.length - 1; k >= 0; k--) {
                this.stFld[1].field.push([(-scale*points[k][0]+this.$el.find("canvas")[0].width/1.6)/obj.scale, scale*points[k][1]/obj.scale])
            }
            this.stFld[1].x = this.stFld[1].field[0][0]*obj.scale+cX-R;
            this.stFld[1].y = this.stFld[1].field[0][1]*obj.scale+cY;


            for (j=0; j<2; j++) {
                for (m=0; m<this.stFld[j].field.length; m++) {
                    p1 = this.stFld[j].field[m];
                    if ( m < this.stFld[j].field.length-1 ) {
                        p2 = this.stFld[j].field[m+1];
                    } else {
                        p2 = this.stFld[j].field[0];
                    }
                    if (j == 1) p1 = [p2, p2 = p1][0];
                    scale = obj.scale;
                    sc = 0.315;
                    cor = - Math.PI/180*55;
                    x = ((p2[0]-p1[0])*sc*Math.cos(cor) - (p2[1]-p1[1])*sc*Math.sin(cor) + p1[0])*scale + cX;
                    y = ((p2[1]-p1[1])*sc*Math.cos(cor) + (p2[0]-p1[0])*sc*Math.sin(cor) + p1[1])*scale + cY;
                    this.stFld[j].centers.push({x:x,y:y});
                }
            }
            //elements for base
            scale = obj.scale;
            
            x = obj.field.coords[1][0][0];
            y = obj.field.coords[1][0][1];
            R = Math.sqrt(Math.pow(obj.field.coords[1][1][0] - x,2) + Math.pow(obj.field.coords[1][1][1] - y,2))*(Math.sqrt(10)*Math.sqrt(5+Math.sqrt(5))/10);
            x = x*scale + TRx - R*scale;
            y = y*scale + TRy;
            this.king[0].x = x;
            this.king[0].y = y;
            
            x = obj.field.coords[0][4][0];
            y = obj.field.coords[0][4][1];
            R = Math.sqrt(Math.pow(obj.field.coords[0][0][0] - x,2) + Math.pow(obj.field.coords[0][0][1] - y,2))*(Math.sqrt(10)*Math.sqrt(5+Math.sqrt(5))/10);
            x = x*scale + TRx + R*scale;
            y = y*scale + TRy;
            this.king[1].x = x;
            this.king[1].y = y;
            
            area = obj.field.coords[1];
            sc = 0.315;
            cor = Math.PI/180*55;
            for ( m = 0; m < area.length; m++ ) {
                p1 = area[m];
                if ( m < area.length-1 ) {
                    p2 = area[m+1];
                } else {
                    p2 = area[0];
                }
                x = ((p2[0]-p1[0])*sc*Math.cos(cor) - (p2[1]-p1[1])*sc*Math.sin(cor) + p1[0])*scale + cX;
                y = ((p2[1]-p1[1])*sc*Math.cos(cor) + (p2[0]-p1[0])*sc*Math.sin(cor) + p1[1])*scale + cY;
                this.base.centers.push({x:x,y:y});
            }

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
                    elem.index = i;
                    elem.src = 'images/piece/'+obj.elements[i].name+'.png';
                    obj.elements[i].img = elem;
                    elem.onload = function() {
                        panel = obj.panel;
                        obj.drawElemInPanel(this.index);
                        if (obj.elements[obj.index].index == 0)
                            obj.drawSelect(obj.context, panel.x+panel.width/2, obj.elements[obj.index].index*90+60);
                    };
                }

                for (i=0; i<obj.elements.length; i++) {
                    elem = document.createElement('img');
                    elem.index = i;
                    elem.onload = function() {
                        panel = obj.panel;
                        obj.drawElemInPanel(this.index);
                        if (obj.elements[obj.index].index == 0)
                            obj.drawSelect(obj.context, panel.x+panel.width/2, obj.elements[obj.index].index*90+60);
                    };
                    elem.src = 'images/piece/'+obj.elements[i].name+'.png';
                    obj.elements[i].img = elem;
                }

                for (i=0; i<obj.king.length; i++) {
                    elem = document.createElement('img');
                    elem.index = i;
                    if (obj.king[i].elem) {
                        elem.src = 'images/King/'+obj.king[i].name+'.png';
                        obj.king[i].elem = elem;
                        elem.src = 'images/King/'+obj.king[i].name+'_enabled.png';
                        obj.king[i].elemEnbl = elem;
                    } else{
                        elem.src = 'images/King/'+obj.king[i].name+'.png';
                        obj.king[i].img = elem;
                        elem.onload = function() {
                        };
                    };
                }

                obj.drawEnemy();
            };
            img.src = 'images/ptrn.jpg';
        },

        drawField: function(context,points) {
            cX = this.$el.find("canvas")[0].width/2;
            cY = this.$el.find("canvas")[0].height/2;
            scale = obj.scale;
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
            if (this.state == "place" && 
                x > panel.x && 
                x < panel.x+panel.width && 
                y > panel.y && 
                y < panel.y+panel.height)
                for (i=0; i<this.elements.length; i++) {
                    if (y < this.elements[i].index*90+140) {
                        this.drawElemInPanel(this.index);
                        this.drawSelect(this.context, panel.x+panel.width/2, this.elements[i].index*90+60)
                        this.index = i;
                        break;
                    }
            } else
             {
                index = this.field.checkField((x-TRx)/this.scale,(y-TRy)/this.scale);
                if (index >= 0) {
                    // Place of elements
                    if ( this.state == "place" && this.field.baseField.indexOf(index) >= 0 && this.field.map[index] == -1) {
                        this.drawField(this.context,coords[index]);
                        this.drawElemInField(this.field.coords[index][0],this.field.coords[index][1])
                        this.elements[this.index].count--;
                        idField = index + 1;
                        if (localStorage['youStart'] == "true") idField = this.field.inv[index]+1;
                        this.elements[this.index].place.push(idField);
                        this.field.map[index] = this.index;
                        if (this.elements[this.index].count == 0) {
                            this.drawElemInPanel(this.index);

                            for (z=0; z<this.elements.length; z++)
                                if (this.elements[z].count > 0) {
                                    this.index = z;
                                    break;
                                }
                            if (z==6) {
                                this.state = "game";
                                this.index = 0;
                                this.field.map[1] = 0;
                                this.drawElemInField(this.field.coords[1][0],this.field.coords[1][1])
                                this.socket.sendMessage(this.elements, 1);
                            }
                        }
                        this.drawElemInPanel(this.index);
                        this.drawSelect(this.context, panel.x+panel.width/2, this.elements[this.index].index*90+60)
                        if (this.state == "game") {
                            this.context.clearRect(this.panel.x-10, this.panel.y-50, panel.width+20, this.panel.height+100);
                            this.drawStatus();
                        }
                    } else if ( this.state == "game" && this.field.map[index] != -1 && this.field.map[index] < 5) {
                        this.state = "move";
                        this.from = index;
                        this.index = this.field.map[index];
                        $(".state").text("(фишка "+this.elements[Math.abs(this.field.map[index])].name+" захвачена)"); 
                    } else if ( this.state == "move" ) {
                        if (this.field.map[index] == -1 || this.field.map[index] > 4) {
                            this.state = "game";
                            this.move = index;
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
                            $(".state").text("");
                        } else {
                            this.state = "game";
                            $(".state").text("Вы не можете так сходить. Сделайте другой ход");
                        }
                    } else if ( this.state == "change" && index == 1 ) {
                        min = {index: -1, length: 1000}
                        for (k=0; k<this.base.centers.length; k++) {
                            R = Math.sqrt(Math.pow(x-this.base.centers[k].x,2) + Math.pow(y-this.base.centers[k].y,2));
                            if ( R < min.length ) {
                                min.index = k;
                                min.length = R;
                            }
                        }
                        this.socket.sendMessage(this.base.elem[min.index], 5);
                    } 
                } else {
                    x = (x - TRx)/obj.scale;
                    y = (y - TRy)/obj.scale;
                    coords = this.stFld[0].field;
                    check = true;
                    min = {index: -1, length: 1000}
                    for (k=0; k<coords.length; k++) {
                        p1 = k;
                        if ( k < coords.length -1 ) {
                            p2 = k+1;
                        } else {
                            p2 = 0;
                        }
                        if ((coords[p2][0]-coords[p1][0])*(y-coords[p1][1]) - 
                            (coords[p2][1]-coords[p1][1])*(x-coords[p1][0]) >= 0 ) {
                                check = false;
                            }  
                    }
                    for (k=0; k<coords.length; k++) {
                        R = Math.sqrt(Math.pow(x-coords[k][0],2) + Math.pow(y-coords[k][1],2));
                        if ( R < min.length ) {
                            min.index = k;
                            min.length = R;
                        }
                    }
                    if (check) {
                        this.king[0].check = this.stFld[0].elem[min.index];
                        this.socket.sendMessage(this.stFld[0].elem[min.index],7)
                        return i;
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
            this.context.fillRect(panel.x+10, this.elements[index].index*90+60-8, panel.width-20,  90);
            this.context.drawImage(this.elements[index].img,
                panel.x+panel.width/2-this.elements[index].img.width/2, this.elements[index].index*90+60);
            this.drawCount(this.context, panel.x+panel.width/2, 
                this.elements[index].index*90+135, this.elements[index].count);
        },

        drawElemInField: function(p1,p2) {
            TRx = this.$el.find("canvas")[0].width/2;
            TRy = this.$el.find("canvas")[0].height/2;
            scale = obj.scale;
            size = 30*scale;
            sc = Math.sqrt(3)/3;
            cor = Math.PI/180*30;
            x = ((p2[0]-p1[0])*sc*Math.cos(cor) - (p2[1]-p1[1])*sc*Math.sin(cor) + p1[0])*scale + TRx;
            y = ((p2[1]-p1[1])*sc*Math.cos(cor) + (p2[0]-p1[0])*sc*Math.sin(cor) + p1[1])*scale + TRy ;
            if (p1 == this.field.coords[1][0]) {
                x = this.king[0].x;
                y = this.king[0].y;
            }
            if (p1 == this.field.coords[0][0]) {
                x = this.king[1].x;
                y = this.king[1].y;
            }
            ptrn = this.context.fillStyle;
            if (this.index < 0 ) {
                this.context.fillStyle = "#F5A9BC";
                this.index += 6;
            } else {
                this.context.fillStyle = "#FFFFFF";
            }
            if (p1 == this.field.coords[this.king[0].pos][0] ||
                p1 == this.field.coords[this.king[1].pos][0]) this.context.fillStyle = "#000000";
            
            this.context.beginPath();
            this.context.arc(x,y,size/2.3,0,2*Math.PI);
            this.context.fill();
            
            if (this.index > 5) {
                img = this.elements[5].img;
                this.context.drawImage(img,x-img.width/2*size/img.width,y-img.height/2*size/img.height,size,size);
                this.index -=6;
            }

            img = this.elements[this.index].img;
            this.context.drawImage(img,x-img.width/2*size/img.width,y-img.height/2*size/img.height,size,size);

            if (this.index == 5 && p1 == this.field.coords[this.king[1].pos][0]) {
                this.context.beginPath();
                this.context.arc(x,y,size/2.3,0,2*Math.PI);
                this.context.fill();
            }

            this.context.fillStyle = ptrn;   
        },

        drawEnemy: function() {
            var cloud = document.createElement('img');
            cloud.onload = function() {
                obj.elements.push({name: "cloud", index: 5, count: 0, img : cloud});
                obj.index = 5;
                pos = obj.field.baseField;
                for (l=0; l < pos.length; l++) {
                    p1 = obj.field.coords[obj.field.inv[pos[l]]][0];
                    p2 = obj.field.coords[obj.field.inv[pos[l]]][1];
                    obj.drawElemInField( p1, p2)
                    obj.field.map[obj.field.inv[pos[l]]] = 5;
                }
                p1 = obj.field.coords[0][0];
                p2 = obj.field.coords[0][1];
                obj.drawElemInField( p1, p2)
                obj.field.map[0] = 5;

                obj.index = 0;
            };
            cloud.src = 'images/piece/cloud.png';
            obj.cloud = cloud;
        },

        drawBase: function() {
            img = this.king[0].img;
            this.context.save();
            size = 90*obj.scale;
            this.context.translate(this.king[0].x,this.king[0].y)
            this.context.rotate(Math.PI);
            obj.context.drawImage(img,-size/2,-size/2,size,size);
            this.context.restore();

            for ( m = 0; m < this.base.centers.length; m++) {
                pos = this.base.centers[m];
                elSize = obj.scale*25;
                ptrn = this.context.fillStyle;
                this.context.fillStyle = "#FFFFFF";
                this.context.beginPath();
                this.context.arc(pos.x,pos.y,elSize/2-obj.scale,0,2*Math.PI);
                this.context.fill();
                this.context.fillStyle = ptrn;

                img = this.elements[this.base.elem[m]].img;
                this.context.drawImage(img, pos.x - elSize/2, pos.y - elSize/2, elSize, elSize);

            };
        },

        drawStatus: function( own) {
            size = obj.scale*180;

            for (j = 0; j < 2; j++) {
                this.drawField(this.context, this.stFld[j].field)

                this.context.save();
                this.context.translate(this.stFld[j].x,this.stFld[j].y)
                img = this.king[0].img;
                if (this.stFld[j].rotate) this.context.rotate(Math.PI);
                this.context.drawImage(img,-size/2,-size/2,size,size);
                this.context.restore();

                for (m=0; m<this.stFld[j].centers.length; m++) {
                    if (this.stFld[j].have[this.stFld[j].elem[m]]) {
                        pos = this.stFld[j].centers[m];
                        elSize = obj.scale*53;
                        ptrn = this.context.fillStyle;
                        this.context.fillStyle = "#FFFFFF";
                        this.context.beginPath();
                        this.context.arc(pos.x,pos.y,elSize/2-obj.scale*5,0,2*Math.PI);
                        this.context.fill();
                        this.context.fillStyle = ptrn;

                        img = this.elements[this.stFld[j].elem[m]].img;
                        this.context.drawImage(img, pos.x - elSize/2, pos.y - elSize/2, elSize, elSize);
                    }
                }
            }
        },

        drawSelect: function(context,x,y) {
            ptrn = context.fillStyle;
            context.beginPath();
            context.fillStyle = ptrn;
            context.arc(x,y+38  ,42,0,2*Math.PI);
            context.stroke();
        },

        reveal: function(array) {
            for (z = 0; z < array.length; z++) {
                pos = array[z][0]-1;
                if (localStorage['youStart'] == "true") {
                    pos = this.field.inv[array[z][0]-1];
                }
                if (pos != this.king[0].pos && pos != this.king[1].pos) {
                    enemy = (this.field.map[pos] > 4)
                    this.field.map[pos] = array[z][1];
                    if (enemy) {
                        this.field.map[pos] +=6
                    } else {
                        this.field.map[pos] -=6
                    }

                    this.drawField(this.context,this.field.coords[pos]);
                    this.index = this.field.map[pos];
                    this.drawElemInField(this.field.coords[pos][0],this.field.coords[pos][1])
                }
            }
        },

        recolor: function(chip, el) {
            this.drawField(this.context,this.field.coords[chip]);
            this.index = el;
            this.drawElemInField(this.field.coords[chip][0],this.field.coords[chip][1])
        }, 

        destroy: function(array) {
            for (z = 0; z < array.length; z++) {
                pos = array[z]-1;
                if (localStorage['youStart'] == "true") {
                    pos = this.field.inv[array[z]-1];
                }
                this.field.map[pos] = -1;
                this.drawField(this.context,this.field.coords[pos]);
            }
        },

        moving: function(coord) {
            from = coord.from;
            to = coord.to;
            if (localStorage['youStart'] == "true") {
                from = this.field.inv[coord.from];
                to = this.field.inv[coord.to];
            }
            if (this.king[0].pos == from) this.king[0].pos = to;
            if (this.king[1].pos == from) this.king[1].pos = to;
            st = this.field.map[from];
            this.field.map[from] = -1;
            this.field.map[to] = st;
            if ( to == 0 && st > 5 ) this.field.map[to] = 5;
            this.index = this.field.map[to];
            this.drawField(this.context,this.field.coords[from]);
            this.drawElemInField(this.field.coords[to][0],this.field.coords[to][1])
        },

        changeElem: function(coord) {
            ptrn = this.context.fillStyle;
            this.context.fillStyle = "#FFAAAA";
            this.drawField(this.context,this.field.coords[1]);
            this.context.fillStyle = ptrn;
            this.state = "change";
            this.drawBase();
        },

        lobby: function(coord) {
            plrs = obj.players.getList();
            obj.waitPlrs = [];
            for ( i = 0; i < plrs.length; i++ ) {
                obj.waitPlrs.push({name: plrs[i]});
            }
            obj.render();
            $(".game").hide();
        },

        startGame: function(elem) {
            clearInterval(this.timer);
            this.players.startGame(elem.target.innerHTML)
            $(".lobby").hide();
            $(".game").show();
        },

    });

    return new GameView();
});