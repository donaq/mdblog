(ns mdblog.posts-test
  (:require [clojure.test :refer :all]
            [mdblog.posts :refer :all]))

(deftest set-postdir-test
  (testing "Set postdir"
    (set-postdir "waffles")
    (is (= postdir "waffles"))
    (set-postdir "ice-cream")
    (is (= postdir "ice-cream"))))
