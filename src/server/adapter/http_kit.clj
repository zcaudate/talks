(ns server.adapter.http-kit
  (:require [org.httpkit.server :as httpkit]
            [server.common :refer
             [server-stop server-start]]))

(defmethod server-start :http-kit
  [server]
  (httpkit/run-server (:handler server) (:all server)))

(defmethod server-stop :http-kit
  [server]
  ((-> server :instance deref)))
