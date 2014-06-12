(ns server.adapter.jetty
  (:require [ring.adapter.jetty :as jetty]
            [server.common :refer
             [server-stop server-start]]))

(defmethod server-start :jetty
  [server]
  (jetty/run-jetty (:handler server) (assoc (:all server) :join? false)))

(defmethod server-stop :jetty
  [server]
  (-> server :instance deref (.stop)))