# mdblog

A simple markdown static CMS written in clojure.

This just generates a static site and maintains the metadata. You have all the flexibility in the world to change the site as you see fit. Just edit public/js/site.js to change functionality.

## Installation

I'm the only one using this, so it's gonna be git.

Or if you want to use it, you can uberjar it.

## Usage

Usage: ```java -jar mdblog.jar [command] [args]```

Or put the jar in a runnable shell script. I do not remember how to do this, but it's easily searchable.

## Commands

### create

Usage: ```create <site name>```

A skeleton directory structure for a website will be created in a directory named after the site name. You can edit the css and index.html however you wish, but the javascript expects the "postsdiv", "contentsdiv" and "maindiv" divs to be there. Just serve the "public" subdirectory in the site directory with a webserver of your choice.

### publish

Usage: ```publish <site directory> <filename> <Title> <section> <subsection> <subsubsection> ...```

Publishes the markdown file denoted by "filename" to the site. The (sub)*section arguments are optional. I have no idea where I am going with this.

## Built with

* [clojure](http://clojure.org)
* [marked](https://github.com/chjj/marked/commit/3e02a69921b9b4009d0b17aa1fe0ae2546f96de2) 
* [jquery](http://jquery.com/) (2.1.1)
* [bootstrap](http://getbootstrap.com) (3.2.0)

## Notes

I started this project because I live in the command-line and my favourite working environment is vim+screen. Also, I wanted to try hosting a personal site using a raspberry pi, so it needs to be a very stripped down static site. Documentation is sparse, but then if you find this useful, you probably have a similar working environment and should be able to figure it out easily.

This is very much a work in progress and a learning exercise besides, so don't come looking for me if it makes your computer explode. Feel free to suggest improvements, but I would only implement them if I think I can personally use them.

## License

Distributed under the I Do Not Give a Flying Fuck License, wherein I do not give a flying fuck what you do with it.
