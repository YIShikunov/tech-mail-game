define([
    'jquery',
    'backbone'
], function ($, Backbone) {

    return function(method, model, options) {

        var methodMap = {
            'create': {
                method: 'POST',
                url: '/signup',
                success: function (resp) {
                    if (resp.code == 0) {
                    }
                    else if (resp.status == 500) {
                    }
                },
                error: function () {

                }
            },
            'read': {
                method: 'GET',
                url: '/signin',
                success: function (resp) {
                    if (resp.code == 0) {
                        model.reset(resp.response);
                    }
                },
                error: function () {}
            },
            'update': {}
        };
        var type = methodMap[method].method,
            url = methodMap[method].url,
            success = methodMap[method].success,
            error = methodMap[method].error || function () {};
            data = {};
            for (var num in model.attributes) {
                debugger;
                data[model.attributes[num].name] = model.attributes[num].value;
            }

        var xhr = $.ajax({
            type: methodMap[method].method,
            url: methodMap[method].url,
            data: data,
            async: false,
            dataType: 'json'
        }).done(success).fail(error);

        return xhr;
    }
});