(ns mdblog.publish-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [mdblog.publish :refer :all]
            [common.utils :as utils]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(def tmp "tmp")
(def fmd "file.md")
(def md-contents "Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn")

(defn create-tmp
  "fixture to create site in tmp"
  [f]
  (utils/create tmp)
  (spit fmd md-contents)
  (f)
  (fs/delete-dir tmp)
  (fs/delete fmd))

(deftest publish-test
  (testing "public/posts/index.json updated and md file copied to target directory and deleted"
    (is (== 0 (count ((-> (str tmp "/public/posts/index.json") slurp json/read-str) "posts"))))
    (publish tmp fmd "some title" "section" "subsection")
    (let [dat (-> (str tmp "/public/posts/index.json") slurp json/read-str)
          post (nth (get-in dat ["posts" "section" "subsection"]) 0)
          testdat {"title" "some title",
                   "location" "/posts/some-title.md"}]
      ; check contents of metadata
      (is (= (dissoc post "created" "modified") testdat))
      (is (and (contains? post "created") (contains? post "modified")))
      ; check copied file content is identical to original file content
      (is (= (slurp (str tmp "/public/posts/some-title.md")) md-contents))
      (println (slurp (str tmp "/public/posts/some-title.md")))
      ; check original markdown file deleted
      (is (not (fs/exists? fmd))))))

(deftest publish-to-non-existent
  (testing "trying to publish to non-existent site should create that site"
    (fs/delete-dir tmp)
    (publish tmp fmd "some title" "section")
    (is (fs/directory? tmp))))

(use-fixtures :each create-tmp)
