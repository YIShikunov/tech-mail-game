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

        login: function (data) {
            this.clear();
            this.set(data);
            this.fetch();
        },

        signup: function (data) {
            this.clear();
            this.set(data);
            this.save();
        }

    });

    return UserModel;

});