; publishes a post to the specified site directory
(ns mdblog.publish
  (:gen-class)
  (:require [common.utils :as utils]
            [clojure.data.json :as json]
            [clojure.string :as clstr]
            [me.raynes.fs :as fs]))

(defn ensure-exists
  "If site does not exist, it is created"
  [site]
  (if (fs/directory? site)
    true
    (utils/create site)))

(defn write-post
  "Actual publish to site"
  [site fname title subdirs created & args]
  (let [sitepostdir (str site "/public/posts")
        datname (str sitepostdir "/index.json")
        dat (-> datname slurp json/read-str)
        posts (dat "posts")
        dstdir (clstr/join "/" (concat [sitepostdir] subdirs))
        dstfname (str (utils/title-to-name title) ".md")
        dstname (str dstdir "/" dstfname)
        reldstname (str (clstr/join "/" (concat ["/posts"] subdirs)) "/" dstfname)
        postdat {"title" title,
                 "created" created,
                 "modified" created,
                 "location" reldstname}]
    ; write to index file
    (->> postdat (conj posts) (assoc dat "posts") json/write-str (spit datname))
    ; copy markdown file to destination
    (fs/copy+ fname dstname)
    ; delete original markdown file
    (fs/delete fname)))

(defn publish
  [& args]
  (if (< (count args) 3)
    (println "Usage: publish <site directory> <filename> <Title> <subdir1> <subdir2> ...")
    (let [site (nth args 0)
          fname (nth args 1)
          title (nth args 2)
          subdirs (drop 3 args)
          created (System/currentTimeMillis)]
      (ensure-exists site)
      (write-post site fname title subdirs created))))
