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
            "": function(){ homeabout(null, "home") },
            "about": homeabout,
            "posts": posts_handler
        };

    // handle highlighting of active tab
    activate_tab(page);

    $(".hideonchange").hide();
    controllers[page](stripped, page);
    // google analytics
    if(typeof ga!=="undefined")
        ga('send', 'pageview', {'page': location.pathname+location.search+location.hash});
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

function preproc_posts(){
    // preprocess posts so that they can be accessed by their location
    var posts = postdat["posts"];
}

// refresh posts data.
function refresh_dat(){
    var nocb = function(data, txtstatus){
        postdat = data;
        preproc_posts(postdat.posts);
    },
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
    var posts = postdat['posts'],
        pkeys = Object.keys(posts);
    if(pkeys.length==0){
        $("#contentsdiv").append('<p class="contentsitem">No posts yet. The site owner should probably do something about that.</p>');
        return $("#contentsdiv").show();
    }

    var splitted = arguments[0].split("/"),
        order = splitted.slice(0, 2),
        sections = splitted.slice(2),
        selen = sections.length;
    for(var i=0;i<selen;i++){
        var k = sections[i];
        if(!(k in posts)){
            $("#contentsdiv").append('<p class="contentsitem">/ ' + sections.join(" / ") + ' does not exist!</p>');
            return $("#contentsdiv").show();
        }
        posts = posts[k];
    }

    //var htmlstr = '<p class="contentsitem"><a href="#' + post.location + '">' + post.title + '</a>';
    //$("#contentsdiv").append(htmlstr);
    //$("#contentsdiv").show();
}

/* end contents page functions */

/* home and about page functions */

function homeabout(stripped, page){
    // here we just show either /home.md or /about.md
    var mdname = page + ".md";
    $.get(mdname, function(dat){
        $("#postsdiv").html(marked(dat)).show();
        // scroll to the top of the div
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    }, "text").fail(function(){
        var md = "## Error.\n\nPage not found";
        $("#postsdiv").html(marked(md)).show();
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    });
}

/* end home and about page functions */

/* post page functions */

function posts_handler(loc){
    var lockey = "/" + loc;
    $.get(lockey, function(dat){
        // render the markdown and inject into the div
        $("#postsdiv").html(marked(md)).show();
        // scroll to the top of the div
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    }, "text").fail(function(){
        var md = "## Error.\n\nPage not found";
        $("#postsdiv").html(marked(md)).show();
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    });
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
