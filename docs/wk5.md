0) Broker (Artemis) — DLQ & redelivery (one time)

Artemis already ships with DLQ/EXPIRY by default. To make retries + backoff deterministic in dev, add an address setting. If you mount a config file, include this fragment in broker.xml (or set it once via the web console):

4) Ordering with JMSXGroupID (per-key FIFO)
   If you need in-order processing per key (e.g., per customer):

Producer sets a group id:


5) Horizontal scaling (Docker/K8s)

6) Throughput & resource tuning


7) Observability (Actuator + metrics)

B) Consumer side (notification-service): idempotent & transactional

    1) Dedupe table

