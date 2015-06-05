define([
    'backbone'
], function (Backbone) {

    return function(method, model, options) {

        var methodMap = {
            'create': {
                method: 'POST',
                url: '/api/v1/auth/signup',
                success: function (resp) {
                    if (resp.code == 0) {
                        alert(resp.response)
                    }
                },
                error: function () {}
            },
            'read': {
                method: 'POST',
                url: '/api/v1/auth/signin',
                success: function (resp) {
                    if (resp.code == 0) {
                        alert(resp.response);
                    }
                },
                error: function () {}
            }
        };

        var success = methodMap[method].success,
            error = methodMap[method].error;
            data = {};
            for (var num in model.attributes) {
                data[model.attributes[num].name] = model.attributes[num].value;
            }

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