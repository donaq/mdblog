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
  (let [dat (-> (str site "/public/posts/index.json") slurp json/read-str)
        dstdir (clstr/join "/" (concat [site] ["public/posts"] subdirs))
        dstfname (str (utils/title-to-name title) ".md")
        dstname (str dstdir "/" dstfname)
        reldstname (clstr/join "/" (concat ["" "posts"] subdirs [dstfname]))]
    (println reldstname)))

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
