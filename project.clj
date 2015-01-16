(defproject cardgame "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [manifold "0.1.0-beta7"]
                 [aleph "0.4.0-alpha9"]]
  :main ^:skip-aot cardgame.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
