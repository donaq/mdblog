var now = new Date(), // rarely used
    postdat = null;

function refresh_dat(){
    var nocb = function(data, txtstatus){ postdat = data; },
        cb = null;
    if(arguments.length>0){
        var argcb = arguments[0]
        cb = function(data, txtstatus){
            nocb(data, txtstatus);
            argcb(data, txtstatus);
        };
    }else cb = nocb;

    $.getJSON("/posts/index.json", cb);
}

function refresh_posts(){
    var posts = postdat["posts"],
        plen = posts.length,
        page = arguments.length==1?arguments[0]:0;

}

/**
 * When ready, get the posts.
 */
$(document).ready(function(){
    refresh_dat(refresh_posts);
});
