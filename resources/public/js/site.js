var postdat = null;
/**
 * When ready, get the posts.
 */
$(document).ready(function(){
    $.getJSON("/posts/index.json", function(data, txtstatus){
        postdat = data;
    });
});
