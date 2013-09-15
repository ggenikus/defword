(defproject defword "0.1"
  :description "Super app"
  :url "defword.ggenikus.com.ua"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/kepl-v10.html"}
  :source-paths ["src/clj"]          

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.2"]
                 [org.clojure/core.incubator "0.1.3"]
                 [midje "1.5.1"]
                 [me.raynes/laser "2.0.0-SNAPSHOT"]
                 [compojure "1.1.5"]
                 [domina "1.0.2-SNAPSHOT"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [shoreleave/shoreleave-remote "0.3.0"]]
  :plugins [[lein-ring "0.8.6"]
            [lein-cljsbuild "0.3.2"]]

  :ring {:handler defword.core/app}

;; cljsbuild options configuration
  :cljsbuild {:builds
              [{;; CLJS source code path
                :source-paths ["src/cljs"]

                ;; Google Closure (CLS) options configuration
                :compiler {;; CLS generated JS script filename
                           :output-to "resources/public/js/app.js"

                           ;; minimal JS optimization directive
                           :optimizations :whitespace

                           ;; generated JS code prettyfication
                           :pretty-print true}}]})
