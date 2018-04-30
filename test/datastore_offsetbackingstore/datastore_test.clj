(ns datastore-offsetbackingstore.datastore-test
  (:require [clojure.test :refer :all]
            [datastore-offsetbackingstore.datastore :as datastore]))

(deftest integration-tests
  (testing "read-after-write"
    (let [config {:kind "foobar" :id 100}
          data {"foo" "bar"}]
      (datastore/write! config data)
      (is (= data (datastore/read config))))))
