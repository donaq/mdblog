; creates a site directory.
(ns mdblog.create
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [common.utils :as ccu]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (ccu/create (first args)))
