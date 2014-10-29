var now = new Date(), // rarely used
    navs = ["contents", "about"],
    postdat = null;

/* site functions */

// creates nav hook functions
function navhook(listelem){
    return function(){
        $(".navli").removeClass("active");
        $(listelem).addClass("active");
    };
}

// dispatcher function
function dispatcher(){
    var currhash = location.hash.slice(1);
    console.log(currhash);
}

/* end site functions */

/* home page functions */

// refresh post data
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

/* end home page functions */

/* post page functions */

function refresh_posts(){
    var posts = postdat["posts"],
        plen = posts.length,
        page = arguments.length==1?arguments[0]:0;
}

/* end post page functions */

$(document).ready(function(){
    // nav hooks
    for(var i=0;i<navs.length;i++){
        var n = navs[i];
        $("#"+n+"topnav").click(navhook("#"+n+"li"));
    }

    $(".homelink").click(function(){
        $(".navli").removeClass("active");
    });

    // get posts
    refresh_dat(refresh_posts);

    dispatcher();

    $(window).on('hashchange', dispatcher);
});
