(ns mdblog.publish-test
  (:require [clojure.test :refer :all]
            [mdblog.publish :refer :all]
            [common.utils :refer :all]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(def tmp "tmp")

(defn create-tmp
  "fixture to create site in tmp"
  [f]
  (create tmp)
  (f)
  (fs/delete-dir tmp))

(deftest publish-test
  (testing "file written and public/posts/index.json updated"
    (publish tmp "file.md" "some title")))

(deftest publish-to-non-existent
  (testing "trying to publish to non-existent site should create that site"
    (fs/delete-dir tmp)
    (publish tmp "file.md" "some title")
    (is (fs/directory? tmp))))
    

(use-fixtures :each create-tmp)
