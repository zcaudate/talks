(defproject server.common "0.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]
                                  [ring/ring-jetty-adapter "1.2.2"]
                                  [http-kit "2.1.18"]
                                  [dk.ative/docjure "1.6.0"]
                                  [im.chit/iroh "0.1.11"]]
                    :plugins [[lein-midje "3.1.3"]]}})
