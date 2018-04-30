(ns datastore-offsetbackingstore.datastore
  (:refer-clojure :exclude [read])
  (:import [com.google.cloud.datastore DatastoreOptions Entity]))

(def ^:private datastore (.getService (DatastoreOptions/getDefaultInstance)))

(defn ^:private create-key [key kind]
  (let [factory (.newKeyFactory datastore)]
    (doto factory
      (.setNamespace "datastore-offsetbackingstore.datastore")
      (.setKind kind))
    (.newKey factory key)))

(defn ^:private create-entity [key m]
  (let [builder (Entity/newBuilder key)]
    (doseq [[key value] m]
      (.set builder key value))
    (.build builder)))

(defn write! [{:keys [kind id]} m]
  (let [key (create-key id kind)
        entity (create-entity key m)]
    (.put datastore entity)))

(defn read [{:keys [kind id]}]
  (let [key (create-key id kind)
        entity (.get datastore key)]
    (when-not (nil? entity)
      (into {}
            (map (fn [key]
                   [key (.getString entity key)]))
            (.getNames entity)))))
