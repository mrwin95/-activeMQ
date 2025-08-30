0) Broker (Artemis) — DLQ & redelivery (one time)

Artemis already ships with DLQ/EXPIRY by default. To make retries + backoff deterministic in dev, add an address setting. If you mount a config file, include this fragment in broker.xml (or set it once via the web console):