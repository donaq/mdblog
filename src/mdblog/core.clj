(ns mdblog.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(def ^:dynamic config)

(defn publish [& args]
  (println args))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [dispatch-map {"publish" publish}
        action (get dispatch-map (nth args 0))]
    (apply action (subvec (vec args) 1))))

(defn read-config [fname]
  "Returns a map of the config"
  (json/read-str (slurp (io/file fname))))
