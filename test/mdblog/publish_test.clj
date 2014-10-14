(ns mdblog.publish-test
  (:require [clojure.test :refer :all]
            [mdblog.publish :refer :all]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(deftest publish-test
  (testing "file written and public/posts/index.json updated"
    (apply publish ["tmp" "file.md" "some title"])))
