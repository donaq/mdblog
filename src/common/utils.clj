(ns common.utils
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as clstr]
            [me.raynes.fs :as fs])
  (:import (java.util.zip ZipFile ZipEntry)))

; so this function is probably totally fucking useless now
(defn copy-public-file
  "Recursively copies public resources to target dir. This is only useful when running from lein."
  [dir resourcename]
  (let [resource (-> resourcename io/resource io/file)]
    ; directory recursive call
    (if (.isDirectory resource)
      (let [files (.list resource)
            subdir (str dir "/" (.getName resource))]
        (fs/mkdir subdir)
        ; for each file in directory, do copy public
        (doseq [fname files]
          (copy-public-file subdir (str resourcename "/" fname))))
      ; copy
      (io/copy resource (-> (str dir "/" (.getName resource)) io/file)))))

(defn copy-public-jar
  "Copies public resources from jar to target dir. Because java is fucking retarded."
  [dir resourcename]
  (let [jarname (-> (java.lang.ClassLoader/getSystemClassLoader) .getURLs first .getPath)
        ^ZipFile zf (ZipFile. jarname)
        ; get entries in public/
        entries (vec (for [^ZipEntry entry (enumeration-seq (.entries zf))
                  :let [ename (.getName entry)]
                  :when (.startsWith ename "public")]
                  ename))]
    (.close zf)
    (doseq [entry entries]
      ; ending with / means directory in zipfile format
      (if (.endsWith entry "/")
        (fs/mkdirs (str dir "/" entry))
        (io/copy (-> entry io/resource .getContent) (io/file (str dir "/" entry)))))))

(defn copy-public
  "Determine if protocol is jar"
  [dir resourcename]
  ; file
  (if (= "jar" (-> resourcename io/resource .getProtocol))
    (copy-public-jar dir resourcename)
    (copy-public-file dir resourcename)))

(defn create
  "Does the actual creation. Takes a directory name and creates a site directory, copying all the static files into it if it does not exist."
  [dir]
  (if (fs/directory? dir)
    (println (str "Directory " dir " already exists"))
    (do
      (fs/mkdir dir)
      (copy-public dir "public"))))

(defn only-allowed-chars
  "Removes any character not in a-z0-9-."
  [src]
  (clstr/join "" (for [c src
                       :let [i (int c)]
                       ; alphabets
                       :when (or (and (>= i 97) (<= i 122))
                                 ; hyphen
                                 (= i 45)
                                 ; numbers
                                 (and (>= i 48) (<= i 57)))]
                   c)))

(defn title-to-name
  "Takes a title and returns a file name"
  [title]
  (-> title clstr/lower-case
      (clstr/replace " " "-")
      (only-allowed-chars)))
