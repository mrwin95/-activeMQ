create table if not exists processed_events (
    event_key text primary key,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

create table if not exists notification_logs (
    order_id bigint primary key,
    customer_name text not null,
    total numeric(12,2) not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

create table if not exists outbox (
                                      id bigserial primary key,
                                      aggregate_type text not null,
                                      aggregate_id text not null,
                                      type text not null,
                                      payload jsonb not null,
                                      event_key text not null,
                                      created_at timestamptz not null default now(),
    published_at timestamptz
    );

create index if not exists outbox_unpub_idx on outbox(published_at) where published_at is null;