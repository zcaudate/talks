(ns server.example.http-kit
  (:require [server.common :refer :all]
            [server.adapter.http-kit]))

(def serv-http-kit
  (server {:type :http-kit
           :port 8082
           :handler (fn [req] {:status 200
                              :body "Hello http-kit!"})}))


(>ns string String)
(string/value "aoeoaeu")
(def hello "hello")
(string/value hello (char-array "World"))
(string/value "aoeuoaeu")#<char[] [C@4104ebd>

(comment

  (start! serv-http-kit)

  (stop! serv-http-kit)

  (all-running)


  (stop-all-running))
