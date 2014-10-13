(ns common.utils-test
  (:require [clojure.test :refer :all]
            [common.utils :refer :all]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(deftest create-test
  (testing "directory created"
    (let [tmp "tmp"]
      (create tmp)
      (is (fs/directory? tmp))
      (is (fs/directory? (str tmp "/public")))
      (is (fs/directory? (str tmp "/public/css")))
      (is (fs/file? (str tmp "/public/css//bootstrap/bootstrap.min.css")))
      (is (fs/directory? (str tmp "/public/img")))
      (is (fs/directory? (str tmp "/public/js")))
      (is (fs/directory? (str tmp "/public/posts")))
      (is (fs/file? (str tmp "/public/posts/index.json")))
      (fs/delete-dir tmp))))
