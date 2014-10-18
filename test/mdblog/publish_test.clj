(ns mdblog.publish-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [mdblog.publish :refer :all]
            [common.utils :refer :all]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(def tmp "tmp")
(def fmd "file.md")

(defn create-tmp
  "fixture to create site in tmp"
  [f]
  (create tmp)
  (fs/create (io/file fmd))
  (f)
  (fs/delete-dir tmp)
  (fs/delete fmd))

(deftest publish-test
  (testing "file written and public/posts/index.json updated"
    (publish tmp fmd "some title")
    (let [dat (-> (str tmp "/public/posts/index.json") slurp json/read-str)]
      (is (= 1 (count (dat "posts")))))))

(deftest publish-to-non-existent
  (testing "trying to publish to non-existent site should create that site"
    (fs/delete-dir tmp)
    (publish tmp fmd "some title")
    (is (fs/directory? tmp))))

(use-fixtures :each create-tmp)
