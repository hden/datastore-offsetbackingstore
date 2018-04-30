# datastore-offsetbackingstore

Using Google Cloud Datastore as a Kafka Connect OffsetBackingStore.
Offset data are flushed to a Cloud Datastore entity and restored upon restart.

## Usage

Add the following values to the worker configuration.

* `offset.storage`: `datastore-offsetbackingstore.core.DatastoreOffsetBackingStore`
* `datastore.kind`: String. The kind of the Cloud Datastore entity.
* `datastore.id`: Integer or String. The id or key for the worker.

Also refer to the following documentations:

* [Entities, Properties, and Keys](https://cloud.google.com/datastore/docs/concepts/entities) - Cloud Datastore Documentation.
* [Kafka Connect User Guide](https://docs.confluent.io/2.0.0/connect/userguide.html#configuring-workers) for general concepts of Kafka Connect.
* [Debezium-embedded](https://github.com/debezium/debezium/tree/master/debezium-embedded) for Running Debezium without a Kafka cluster.

## License

Copyright © 2018 Haokang Den

Distributed under the Eclipse Public License, the same as Clojure.
