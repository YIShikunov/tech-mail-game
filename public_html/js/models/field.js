define([
    'backbone',
], function (
    Backbone
) {
    var FieldModel = Backbone.Model.extend({

        defaults: {
            coords: [[[154.55, -47.55], [210.45, -29.39], [210.45, 29.39], [154.55, 47.55], [120.0, 0.0]],
[[-120.0, 0.0], [-154.55, 47.55], [-210.45, 29.39], [-210.45, -29.39], [-154.55, -47.55]],
[[210.45, 29.39], [210.45, -29.39], [261.35, 0.0]],
[[210.45, -29.39], [154.55, -47.55], [198.23, -86.88]],
[[154.55, -47.55], [120.0, -0.0], [96.09, -53.7]],
[[120.0, 0.0], [154.55, 47.55], [96.09, 53.7]],
[[154.55, 47.55], [210.45, 29.39], [198.23, 86.88]],
[[263.28, -3.62], [210.45, -29.39], [259.18, -62.26]],
[[256.77, -65.58], [210.45, -29.39], [202.27, -87.59]],
[[195.38, -89.83], [154.55, -47.55], [138.35, -104.05]],
[[134.45, -102.78], [154.55, -47.55], [96.66, -57.76]],
[[92.41, -51.9], [120.0, -0.0], [61.26, -2.05]],
[[61.26, 2.05], [120.0, -0.0], [92.41, 51.9]],
[[96.66, 57.76], [154.55, 47.55], [134.45, 102.79]],
[[138.35, 104.05], [154.55, 47.55], [195.38, 89.83]],
[[202.27, 87.59], [210.45, 29.39], [256.77, 65.58]],
[[259.18, 62.26], [210.45, 29.39], [263.28, 3.62]],
[[-261.35, 0.0], [-210.45, -29.39], [-210.45, 29.39]],
[[-198.23, 86.88], [-210.45, 29.39], [-154.55, 47.55]],
[[-96.09, 53.7], [-154.55, 47.55], [-120.0, 0.0]],
[[-96.09, -53.7], [-120.0, -0.0], [-154.55, -47.55]],
[[-198.23, -86.88], [-154.55, -47.55], [-210.45, -29.39]],
[[-259.18, 62.26], [-263.28, 3.62], [-210.45, 29.39]],
[[-202.27, 87.59], [-256.77, 65.58], [-210.45, 29.39]],
[[-138.35, 104.05], [-195.38, 89.83], [-154.55, 47.55]],
[[-96.66, 57.76], [-134.45, 102.78], [-154.55, 47.55]],
[[-61.26, 2.05], [-92.41, 51.9], [-120.0, 0.0]],
[[-92.41, -51.9], [-61.26, -2.05], [-120.0, 0.0]],
[[-134.45, -102.79], [-96.66, -57.76], [-154.55, -47.55]],
[[-195.38, -89.83], [-138.35, -104.05], [-154.55, -47.55]],
[[-256.77, -65.58], [-202.27, -87.59], [-210.45, -29.39]],
[[-263.28, -3.62], [-259.18, -62.26], [-210.45, -29.39]],
[[0.0, 103.92], [60.0, 207.84], [-60.0, 207.84]],
[[-60.0, -207.84], [60.0, -207.84], [0.0, -103.92]],
[[0.0, 103.92], [120.0, 103.92], [60.0, 207.84]],
[[-120.0, -103.92], [-60.0, -207.84], [0.0, -103.92]],
[[0.0, 103.92], [60.0, -0.0], [120.0, 103.92]],
[[-60.0, 0.0], [-120.0, -103.92], [0.0, -103.92]],
[[0.0, 103.92], [-60.0, -0.0], [60.0, 0.0]],
[[60.0, 0.0], [-60.0, -0.0], [0.0, -103.92]],
[[0.0, 103.92], [-120.0, 103.92], [-60.0, -0.0]],
[[120.0, -103.92], [60.0, 0.0], [0.0, -103.92]],
[[0.0, 103.92], [-60.0, 207.84], [-120.0, 103.92]],
[[60.0, -207.84], [120.0, -103.92], [0.0, -103.92]]
]

        },

        initialize: function () {
        },

        checkField: function(x,y) {
            coords = this.get("coords");
            for (i=0; i<coords.length; i++) {
                check = true;
                for (k=0; k<coords[i].length-1; k++) {
                    if ((coords[i][k+1][0]-coords[i][k][0])*(y-coords[i][k][1]) - 
                        (coords[i][k+1][1]-coords[i][k][1])*(x-coords[i][k][0]) <= 0 )
                        check = false;
                }
                if ((coords[i][0][0]-coords[i][k][0])*(y-coords[i][k][1]) - 
                    (coords[i][0][1]-coords[i][k][1])*(x-coords[i][k][0]) <= 0 )
                        check = false;
                if (check) { 
                    console.info('filed: '+i);
                    return i;
                }
            }
            return -1;
        },

    });

    return FieldModel;

});