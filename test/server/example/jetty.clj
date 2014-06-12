(ns server.example.jetty
  (:require [server.common :refer :all]
            [server.adapter.jetty]))

(def serv-jetty
  (server {:type :jetty
           :port 8081
           :handler (fn [req] {:status 200
                              :body "Hello jetty!"})}))

(comment
  (start! serv-jetty)
  (all-running)
  (stop-all-running)
  (stop!  serv-jetty))
