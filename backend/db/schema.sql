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