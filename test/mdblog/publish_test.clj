(ns mdblog.publish-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [mdblog.publish :refer :all]
            [common.utils :as utils]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(def tmp "tmp")
(def fmd "file.md")

(defn create-tmp
  "fixture to create site in tmp"
  [f]
  (utils/create tmp)
  (fs/create (io/file fmd))
  (f)
  (fs/delete-dir tmp)
  (fs/delete fmd))

(deftest publish-test-file
  (testing "md file copied to target directory and deleted"
    (publish tmp fmd "some title" "subdir1" "subdir2")
    ))

(deftest publish-test-meta
  (testing "public/posts/index.json updated"
    (is (== 0 (count ((-> (str tmp "/public/posts/index.json") slurp json/read-str) "posts"))))
    (publish tmp fmd "some title" "subdir1" "subdir2")
    (let [dat (-> (str tmp "/public/posts/index.json") slurp json/read-str)
          post (-> "posts" dat (nth 0))
          testdat {"title" "some title",
                   "location" "/posts/subdir1/subdir2/some-title.md"}]
      (is (== 1 (count (dat "posts"))))
      (is (= (dissoc post "created" "modified") testdat))
      (is (and (contains? post "created") (contains? post "modified"))))))

(deftest publish-to-non-existent
  (testing "trying to publish to non-existent site should create that site"
    (fs/delete-dir tmp)
    (publish tmp fmd "some title")
    (is (fs/directory? tmp))))

(use-fixtures :each create-tmp)
