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
    var currhash = location.hash.slice(1),
        splitted = currhash.replace(/^\/+|\/+$/gm,'').split("/"),
        page = splitted(0),
        controllers = {"contents": contents};


    console.log(currhash);
}

// simple check for change in hash.
function hashchange(){
    var self = this;

    self.currhash = location.hash;
    
    self.pollfunction = function(){
        if(self.currhash==location.hash) return;
        console.log("changed from "+self.currhash+" to "+location.hash);
        self.currhash = location.hash;
        dispatcher();
    }
}

// refresh posts data. to be used as callback for refresh_posts
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

/* end site functions */

/* contents page functions */

//TODO: currently, we have only sort_by param, which does not work. Eventually we want to be able to sort by alphabet and date. We should also have pagination and display subdirectories nicely.
function contents(){
}

/* end contents page functions */

/* home page functions */


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

    // get posts, call dispatcher on load so that we serve the correct page based on the hash
    refresh_dat(dispatcher);

    // handle detection of change in hash
    if("onhashchange" in window) $(window).on('hashchange', dispatcher);
    else{
        var hashchanger = new hashchange();
        setInterval(hashchanger.pollfunction, 50);
    }
});
