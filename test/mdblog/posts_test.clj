(ns mdblog.posts-test
  (:require [clojure.test :refer :all]
            [mdblog.posts :refer :all]
            [clojure.java.io :as io]
            [clojure.java.shell :refer :all]))

(def tdir "tmp")

(defn create-postdir-fixture [f]
  (let [dir (io/file tdir)]
    (.mkdir dir))
  (f)
  (sh "rm" "-rf" "tmp"))

(deftest config-test
  (testing "Configuration read"
    (let [pfile (io/file "tmp.txt")]
        (println "done"))))

(use-fixtures :each create-postdir-fixture)
