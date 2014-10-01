(ns mdblog.posts
  (:gen-class))

; Directory to be used for storing posts
(def postdir)

; setting postdir
(defn set-postdir [pd]
  (def postdir pd))

(defn -main
  [& args]
  (println "hello post"))
