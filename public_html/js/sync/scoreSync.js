define([
    'backbone'
], function (Backbone) {

    return function(method, collection, options) {
        
        var methodMap = {
            'read': {
                method: 'POST',
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
            dataType: 'json'
        }).done(methodMap[method].success);

        return xhr;
    }
});