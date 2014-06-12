(ns server.common)

;; Interface

(defprotocol IRunnable
  (start! [system])
  (stop!  [system])
  (restart! [system])
  (started? [system])
  (stopped? [system]))

;; Abstract Methods

;;(def server-start nil)
;;(def server-stop nil)
(defmulti  server-start :type)
(defmethod server-start :default
  [server]
  (throw (Exception. (format "server-start not implemented for {:type %s}" (:type server)))))

(defmulti server-stop :type)
(defmethod server-stop :default
  [server]
  (throw (Exception. (format "server-stop not implemented for {:type %s}" (:type server)))))

;; Abstract Implementation

(defonce ^:dynamic *running* (atom {}))

(deftype Server [state]
  Object
  (toString [server]
    (format "SERVER::<%s>"
            (-> state
                (dissoc :instance :handler)
                (assoc  :started  (started? server))
                (->>
                 (map (fn [[k v]]
                        (str (name k) "=" v)))
                 (clojure.string/join ", ")))))

  IRunnable
  (start! [server]
    (cond (or (started? server)
              (get @*running* (:port state)))
          false

          :else
          (let [_ (println "SERVER: " server)
                _ (println "RUNNING" *running*)
                instance (server-start server)]
            (println "INSTANCE:" instance)
            (reset! (:instance state) instance)
            (println "STATE:" state)
            (swap! *running* assoc (:port state) server)
            (println "RUNNING" *running*)
            true)))

  (stop! [server]
    (if (stopped? server) false
        (do (server-stop server)
            (reset! (:instance state) nil)
          (swap! *running* dissoc (:port state))
          true)))

  (restart! [server]
    (stop! server)
    (start! server))

  (started? [server]
    (not (stopped? server)))

  (stopped? [server]
    (nil? @(:instance state)))

  clojure.lang.ILookup
  (valAt [ele k]
    (if (or (nil? k)
            (= k :all))
      state
      (get state k))))

(defmethod print-method Server
  [v w]
  (.write w (str v)))

(defn server [settings]
  (Server. (assoc settings :instance (atom nil))))

(defn all-running []
  @*running*)

(defn stop-all-running []
  (do (reduce-kv
       (fn [i k v] (stop! v))
       nil @*running*)))
