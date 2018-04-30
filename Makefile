.PHONY: test emulator

test:
	DATASTORE_EMULATOR_HOST=localhost:8081 lein test

emulator:
	gcloud beta emulators datastore start --no-store-on-disk
