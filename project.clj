(defproject mdblog "0.1.0"
  :description "Markdown blogging engine"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.4"]]
  :main ^:skip-aot mdblog.create
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :publish {:main mdblog.publish}
             :create {:main mdblog.create}}
  :aliases {"publish" ["with-profile" "publish" "run"]})
