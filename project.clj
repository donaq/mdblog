(defproject mdblog "0.1.0"
  :description "Markdown blogging engine"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.4"]
                 [me.raynes/fs "1.4.4"]]
  :main ^:skip-aot mdblog.create
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :aliases {"publish"
              ^{:doc "Publishes a markdown file as a post.

Usage: lein publish <site directory> <Title> <filename>"}
              ["run" "-m" "mdblog.publish"]

            "create"
              ^{:doc "Creates a new site directory.

Usage: lein create <site directory>"}
              ["run" "-m" "mdblog.create"]})
