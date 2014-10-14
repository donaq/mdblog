(ns mdblog.core
  (:gen-class)
  (:require [common.utils :as utils]
            [mdblog.publish :as publish]
            [me.raynes.fs :as fs]))

(defn -main
  [& args]
  (let [command (first args)
        funcs {"create" utils/create
               "publish" publish/publish}]
    (apply (funcs command) (rest args))))
