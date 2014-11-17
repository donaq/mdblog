var now = new Date(), // rarely used
    activetabmap = { "contents": "contentsli",
        "about": "aboutli",
        "title": "contentsli"
    },
    postdat = null;

/* site functions */

// highlights active tab based on page
function activate_tab(page){
    $(".navli").removeClass("active");
    if(!(page in activetabmap))
        return;
    var tabid = "#" + activetabmap[page];
    $(tabid).addClass("active");
}

// dispatcher function
function dispatcher(){
    var currhash = location.hash.slice(1),
        stripped = currhash.replace(/^\/+|\/+$/gm,''),
        splitted = stripped.split("/"),
        page = splitted[0],
        controllers = {"contents": contents,
            "": home,
            "about": about,
            "posts": posts
        };

    console.log(page);

    // handle highlighting of active tab
    activate_tab(page);

    $(".hideonchange").hide();
    controllers[page](stripped);
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

//TODO: currently, we have only sort_by param, which does not work. Eventually we want to be able to sort by alphabet or date. We should also have pagination and display subdirectories nicely.
function contents(){
    // clear the contentdiv first
    $(".contentsitem").remove();
    if(postdat.posts.length==0){
        $("#contentsdiv").append('<p class="contentsitem">No posts yet. The site owner should probably do something about that.</p>');

    }else{
        for(var i=0;i<postdat.posts.length;i++){
            var post = postdat.posts[i];
            var htmlstr = '<p class="contentsitem"><a href="#' + post.location + '">' + post.title + '</a>';
            $("#contentsdiv").append(htmlstr);
        }
    }
    $("#contentsdiv").show();
}

/* end contents page functions */

/* home page functions */

function home(){
    $("#homediv").show();
}


/* end home page functions */

/* about page functions */

function about(){
}

/* post page functions */

function posts(loc){
    $.get("/"+loc, function(dat){
        $("#postsdiv").html(marked(dat)).show();
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    }, "text");
}

function refresh_posts(){
    var posts = postdat["posts"],
        plen = posts.length,
        page = arguments.length==1?arguments[0]:0;
}

/* end post page functions */

$(document).ready(function(){
    // get posts, call dispatcher on load so that we serve the correct page based on the hash
    refresh_dat(dispatcher);

    // handle detection of change in hash
    if("onhashchange" in window) $(window).on('hashchange', dispatcher);
    else{
        var hashchanger = new hashchange();
        setInterval(hashchanger.pollfunction, 50);
    }
});
