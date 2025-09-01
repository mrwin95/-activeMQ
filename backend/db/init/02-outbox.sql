-- Core outbox table
CREATE TABLE IF NOT EXISTS outbox (
                                      id             BIGSERIAL PRIMARY KEY,
                                      aggregate_type TEXT        NOT NULL,          -- e.g., "order", "invoice"
                                      aggregate_id   TEXT        NOT NULL,          -- identifier of the aggregate
                                      type           TEXT        NOT NULL,          -- event name/type, e.g., "OrderCreated"
                                      payload        JSONB       NOT NULL,          -- event payload
                                      event_key      TEXT        NOT NULL,          -- idempotency key (producer-generated)
                                      created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at   TIMESTAMPTZ
    );

-- Fast scans for the dispatcher to find unpublished events
CREATE INDEX IF NOT EXISTS outbox_unpub_idx
    ON outbox (published_at)
    WHERE published_at IS NULL;

-- Helpful composite index for typical fetch patterns (optional but recommended)
-- e.g., pulling by aggregate to rebuild stream or debug
--CREATE INDEX IF NOT EXISTS outbox_aggregate_created_idx
--    ON outbox (aggregate_type, aggregate_id, created_at);