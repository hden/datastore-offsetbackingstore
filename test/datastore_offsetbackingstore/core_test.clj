(ns datastore-offsetbackingstore.core-test
  (:require [clojure.test :refer :all])
  (:import [java.util HashMap]
           [java.nio ByteBuffer]
           [org.apache.kafka.connect.runtime.standalone StandaloneConfig]
           [datastore-offsetbackingstore.core DatastoreOffsetBackingStore]))

(def ^:const json-converter "org.apache.kafka.connect.json.JsonConverter")

(defn buffer [s]
  (-> (.getBytes s)
      (ByteBuffer/wrap)))

(defn create-store []
  (let [store (new DatastoreOffsetBackingStore)
        props (new HashMap {"datastore.kind" "foobar"
                            "datastore.id" 1
                            "offset.storage.file.filename" "none"
                            "key.converter" json-converter
                            "value.converter" json-converter
                            "internal.key.converter" json-converter
                            "internal.value.converter" json-converter})]
    (doto store
      (.configure (new StandaloneConfig props))
      (.start))
    store))

(deftest integration-tests
  (testing "read-after-write"
    (let [store (create-store)
          data (new HashMap {(buffer "key") (buffer "value")})]
      @(.set store data nil)
      (is (= data @(.get store [(buffer "key")] nil)))))
  (testing "restore-after-restart"
    (let [store (create-store)
          data @(.get store [(buffer "key")] nil)]
      (is (= (buffer "value") (.get data (buffer "key")))))))
