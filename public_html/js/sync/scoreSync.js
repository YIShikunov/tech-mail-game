define([
    'backbone'
], function (Backbone) {

    return function(method, collection, options) {
        
        var methodMap = {
            'read': {
                method: 'GET',
                url: '/scoreboard',
                success: function (resp) {
                    if (resp.code == 0) {
                        collection.reset(resp.scores);
                    }
                }
            }
        };

        var xhr = $.ajax({
            method: methodMap[method].method,
            url: methodMap[method].url,
            async: false,
            dataType: 'json'
        }).done(methodMap[method].success);

        return xhr;
    }
});