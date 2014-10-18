; publishes a post to the specified site directory
(ns mdblog.publish
  (:gen-class)
  (:require [common.utils :as utils]
            [clojure.data.json :as json]
            [me.raynes.fs :as fs]))

(defn publish
  [& args]
  (if (< (count args) 3)
    (println "Usage: publish <site directory> <filename> <Title>")
    (let [site (nth args 0)
          fname (nth args 1)
          title (nth args 2)
          dat (-> (str site "/public/posts/index.json") slurp json/read-str)
          created (System/currentTimeMillis)]
      (println dat)
      )))
