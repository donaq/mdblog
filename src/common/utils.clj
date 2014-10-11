(ns common.utils
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(defn copy-public
  "Recursively copies public resources to target dir"
  [dir resourcename]
  (let [resource (-> resourcename io/resource io/file)]
    ; directory recursive call
    (if (.isDirectory resource)
      (let [files (.list resource)
            subdir (str dir "/" (.getName resource))]
        (fs/mkdir subdir)
        ; for each file in directory, do copy public
        (doseq [fname files]
          (copy-public subdir (str resourcename "/" fname))))
      ; copy
      (io/copy resource (-> (str dir "/" (.getName resource)) io/file)))))

(defn create
  "Does the actual creation. Takes a directory name and creates a site directory, copying all the static files into it if it does not exist."
  [dir]
  (if (fs/directory? dir)
    (print "Directory " dir " already exists")
    (do
      (fs/mkdir dir)
      (copy-public dir "public"))))
