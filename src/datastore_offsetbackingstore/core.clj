(ns datastore-offsetbackingstore.core
  (:require [datastore-offsetbackingstore.datastore :as datastore])
  (:import [java.nio ByteBuffer]
           [java.util HashMap Base64]
           [org.apache.kafka.connect.storage MemoryOffsetBackingStore])
  (:gen-class
   :name datastore-offsetbackingstore.core.DatastoreOffsetBackingStore
   :extends org.apache.kafka.connect.storage.MemoryOffsetBackingStore
   :exposes {data {:get getData :set setData}}
   :exposes-methods {start superStart}
   :state state
   :init init))

(def ^:private encoder (Base64/getEncoder))
(def ^:private decoder (Base64/getDecoder))

(defn ^:private bytebuffer->string [x]
  (->> (.array x)
       (.encodeToString encoder)))

(defn ^:private string->bytebuffer [x]
  (->> (.getBytes x)
       (.decode decoder)
       (ByteBuffer/wrap)))

(def ^:private encode (map (fn [[key value]]
                             [(bytebuffer->string key)
                              (bytebuffer->string value)])))

(def ^:private decode (map (fn [[key value]]
                             [(string->bytebuffer key)
                              (string->bytebuffer value)])))

(defn -init []
  [[] (atom {})])

(defn -configure [this config]
  (let [state  (.state this)
        config (into {} (.originalsWithPrefix config "datastore."))]
    (swap! state merge {:kind (get config "kind" "offsetbackingstore")
                        :id (get config "id" 1)})))

(defn -start [this]
  (.superStart this) ;; calling super.start()
  (when-let [state (datastore/read @(.state this))]
    (->> (into {} decode state)
         (new HashMap)
         (.setData this))))

(defn -save [this]
  (let [config @(.state this)]
    (->> (.getData this)
         (into {} encode)
         (datastore/write! config))))
