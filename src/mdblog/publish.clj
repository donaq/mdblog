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

(defn title-to-name
  "Takes a title and converts all letters to lower case and all spaces to -. All other characters are removed."
  [title]
  (cljstr/lower-case title))

(defn write-post
  "Actual publish to site"
  [site fname title created & args]
  (let [dat (-> (str site "/public/posts/index.json") slurp json/read-str)]
    (println dat)))

(defn publish
  [& args]
  (if (< (count args) 3)
    (println "Usage: publish <site directory> <filename> <Title>")
    (let [site (nth args 0)
          fname (nth args 1)
          title (nth args 2)
          created (System/currentTimeMillis)]
      (ensure-exists site)
      (write-post site fname title created))))
