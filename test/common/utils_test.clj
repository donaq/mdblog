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
      ;(fs/delete-dir tmp)
      )))
