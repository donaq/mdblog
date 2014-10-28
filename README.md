# mdblog

A simple markdown blogging engine written in clojure.

This just generates a basic site and maintains the metadata. You have all the flexibility in the world to change the site as you see fit. Just edit public/js/site.js to change functionality.

## Installation

I'm the only one using this, so it's gonna be git.

Or if you want to use it, you can uberjar it.

## Usage

Usage: ```java -jar mdblog.jar [command] [args]```

Or put the jar in a runnable shell script. I do not remember how to do this, but it's easily searchable.

See project.clj aliases for the list of commands.

## Built with

* [clojure](http://clojure.org)
* [marked](https://github.com/chjj/marked/commit/3e02a69921b9b4009d0b17aa1fe0ae2546f96de2) 
* [jquery](http://jquery.com/) (2.1.1)
* [bootstrap](http://getbootstrap.com) (3.2.0)

## Notes

I started this project because I live in the command-line and my favourite working environment is vim+screen. Documentation is sparse, but then if you find this useful, you probably have a similar working environment and should be able to figure it out easily.

This is very much a work in progress and a learning exercise besides, so don't come looking for me if it makes your computer explode. Feel free to suggest improvements, but I would only implement them if I think I can personally use them.

## License

Distributed under the I Do Not Give a Flying Fuck License, wherein I do not give a flying fuck what you do with it.
