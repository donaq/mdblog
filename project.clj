(defproject mdblog "0.1.0-SNAPSHOT"
  :description "Markdown blogging engine"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.4"]]
  :main ^:skip-aot mdblog.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
