var now = new Date(), // rarely used
    activetabmap = { "contents": "contentsli",
        "about": "aboutli"
    },
    postdat = null;

// returns a compare function for use in sorting posts based on the page
// modify this to get customised sorts based on the hashed url in the contents section
function get_compare(page){
    function default_compare(a,b){
        return a<b?-1:1;
    }
    return default_compare;
}

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

// handles getting to the correct level
function to_level(sections, posts, errdiv){
    var selen = sections.length;

    for(var i=0;i<selen;i++){
        var k = decodeURIComponent(sections[i]);
        // deal with non-existent section
        if(!(k in posts)){
            $(errdiv).append('<p class="contentsitem">/ ' + sections.join(" / ") + ' does not exist!</p>');
            return false;
        }
        posts = posts[k];
    }
    return posts;
}

// handles breadcrumbs
function get_breadcrumbs(sections, posts){
    var selen = sections.length,
        prefix = "contents",
        breadcrumbs = '<p class="contentsitem">location: <a href="#contents">Top</a> / ';

    for(var i=0;i<selen;i++){
        var k = decodeURIComponent(sections[i]);
        // breadcrumbing
        breadcrumbs = breadcrumbs + '<a href="#' + prefix + '/' + k + '">' + k + '</a> / ';
        prefix = prefix + k + '/';
        posts = posts[k];
    }
    breadcrumbs = breadcrumbs + "</p>";
    return breadcrumbs;
}

/* end site functions */

/* contents page functions */

//TODO: currently, we have only sort_by param, which does not work. Eventually we want to be able to sort by alphabet or date. We should also have pagination and display subdirectories nicely.
function contents(){
    // clear the contentdiv first
    $(".contentsitem").remove();
    var posts = postdat['posts'],
        ourdiv = "#contentsdiv",
        pkeys = Object.keys(posts);
    // no contents at all
    if(pkeys.length==0){
        $(ourdiv).append('<p class="contentsitem">No posts yet. The site owner should probably do something about that.</p>');
        return $(ourdiv).show();
    }

    var currpage = arguments[0] + '/',
        splitted = arguments[0].split("/"),
        page = splitted[0],
        sections = splitted.slice(1),
        level = to_level(sections, posts, ourdiv);

    if(!level) return $(ourdiv).show();

    breadcrumbs = get_breadcrumbs(sections, posts);
    $(ourdiv).append(breadcrumbs);

    compare = get_compare(arguments[0]);

    // still displaying section level
    if(!(level instanceof Array)){
        var subsections = Object.keys(level),
            sublen = subsections.length;
        // always sorted by alphabet
        subsections.sort(compare);
        for(var i=0;i<sublen;i++){
            var ln = currpage + subsections[i];
            var htmlstr = '<p class="contentsitem"><a href="#' + ln + '">' + subsections[i] + '</a>';
            $(ourdiv).append(htmlstr);
        }
        return $(ourdiv).show();
    }
    // displaying story level
    var plen = level.length,
        sortedlevel = level.slice();
    sortedlevel.sort(compare);
    for(var i=0;i<plen;i++){
        var ln = ["posts"].concat(sections,[i]).join("/"),
            post = sortedlevel[i];
        var htmlstr = '<p class="contentsitem"><a href="#' + ln + '">' + post.title + '</a>';
        $(ourdiv).append(htmlstr);
    }

    return $(ourdiv).show();
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

function posts_handler(){
    var posts = postdat['posts'],
        ourdiv = "#postsdiv",
        currpage = arguments[0] + '/',
        splitted = arguments[0].split("/"),
        splen = splitted.length,
        sections = splitted.slice(1,splen-1),
        page = Number.parseInt(splitted[splen-1]),
        level = to_level(sections, posts, ourdiv);

    if(!level) return $(ourdiv).show();

    function failfunc(){
        var md = "## Error.\n\nPage not found";
        $("#postsdiv").html(breadcrumbs + marked(md)).show();
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    };

    breadcrumbs = get_breadcrumbs(sections, posts);
    try{
        var post = level[page],
            ln = post['location'];
    }catch(err){
        return failfunc();
    }
    $.get(ln, function(dat){
        // render the markdown and inject into the div
        $("#postsdiv").html(breadcrumbs + marked(dat)).show();
        // scroll to the top of the div
        $("html, body").animate({scrollTop: $("#postsdiv").offset().top}, 500);
    }, "text").fail(failfunc);
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
