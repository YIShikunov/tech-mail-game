define([
    'backbone'
], function (Backbone) {

    return function(method, model, options) {

        var methodMap = {
            'create': {
                method: 'POST',
                url: '/matchmaking',
                success: function (resp) {
                    if (resp.code == 0) {
                        alert(resp.response);
                    }
                },
                error: function () {}
            },
            'read': {
                method: 'GET',
                url: '/matchmaking',
                success: function (resp) {
                    if (resp.code == 0) {
                        model.data = resp.players;
                    }
                },
                error: function () {}
            }
        };

        var success = methodMap[method].success,
            error = methodMap[method].error;
            data = { opponent: model.opponent};

        var xhr = $.ajax({
            type: methodMap[method].method,
            url: methodMap[method].url,
            data: data,
            // async: false,
            dataType: 'json'
        }).done(success).fail(error);

        return xhr;
    }
});