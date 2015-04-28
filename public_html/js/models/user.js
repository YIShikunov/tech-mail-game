define([
    'backbone',
    'sync/userSync',
], function (
    Backbone, 
    userSync
) {
    var UserModel = Backbone.Model.extend({
        sync: userSync,

        initialize: function () {
        },

        logout: function () {
            this.save();
        },

        login: function (data) {
            this.set(data);
            this.save();
        },

        signup: function (data) {
            this.set(data);
            this.save();
        }

    });

    return UserModel;

});