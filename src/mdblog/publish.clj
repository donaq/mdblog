; publishes a post to the specified site directory
(ns mdblog.publish
  (:gen-class)
  (:require [common.utils :as utils]
            [me.raynes.fs :as fs]))

(defn publish
  [& args]
  (if (< (count args) 3)
    (println "Usage: publish <site directory> <filename> <Title>")
    (println "publish")))
