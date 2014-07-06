(ns mdblog.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [mdblog.core :refer :all]))

(deftest read-config-test
  (testing "read-config sets config map"
    ; write the file first
    (let [cfgmap {"postdir" "posts"}
          cfgfile (io/file "test.cfg")
          cfgstr (json/write-str cfgmap)]
      (spit cfgfile cfgstr)
      (is (= config (read-config "test.cfg")))
      (.delete cfgfile))))
