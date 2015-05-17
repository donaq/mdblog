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
  [site fname title sections created & args]
  (let [sitepostdir (str site "/public/posts/")
        datname (str sitepostdir "/index.json")
        dat (-> datname slurp json/read-str)
        posts (dat "posts")
        section-dat-test (get-in posts sections)
        section-dat (if (nil? section-dat-test) [] section-dat-test)
        dstfname (str (utils/title-to-name title) ".md")
        dstname (str sitepostdir dstfname)
        reldstname (str "/posts/" dstfname)
        postdat {"title" title,
                 "created" created,
                 "modified" created,
                 "location" reldstname}]
    ; write to index file
    (->> postdat (conj section-dat) (assoc-in dat (into [] (concat ["posts"] sections))) json/write-str (spit datname))
    ; copy markdown file to destination
    (fs/copy+ fname dstname)
    ; delete original markdown file
    (fs/delete fname)))

(defn publish
  [& args]
  (if (< (count args) 4)
    (println "Usage: publish <site directory> <filename> <Title> <section> <subsection> <subsubsection> ...")
    (let [site (nth args 0)
          fname (nth args 1)
          title (nth args 2)
          sections (drop 3 args)
          created (System/currentTimeMillis)]
      (ensure-exists site)
      (write-post site fname title sections created))))
