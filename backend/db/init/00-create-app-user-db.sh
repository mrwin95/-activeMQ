#!/usr/bin/env bash
set -euo pipefail

# Uses env: POSTGRES_USER, APP_DB, APP_USER, APP_PASSWORD
# Runs only on first container init (when data dir is empty).

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-SQL
DO
$$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = '${APP_USER}') THEN
    CREATE ROLE ${APP_USER} LOGIN PASSWORD '${APP_PASSWORD}';
  END IF;

  IF NOT EXISTS (SELECT FROM pg_database WHERE datname = '${APP_DB}') THEN
    CREATE DATABASE ${APP_DB};
  END IF;
END
$$;
SQL

# Ownership & privileges inside the new DB
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$APP_DB" <<-SQL
-- Make the app user own the DB and public schema
ALTER DATABASE ${APP_DB} OWNER TO ${APP_USER};

-- Lock down "public" defaults and grant only what you want
REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT USAGE, CREATE ON SCHEMA public TO ${APP_USER};
ALTER SCHEMA public OWNER TO ${APP_USER};

-- Ensure app user can connect & create temp objects
GRANT CONNECT, TEMP ON DATABASE ${APP_DB} TO ${APP_USER};

-- Grant current objects
GRANT ALL ON ALL TABLES     IN SCHEMA public TO ${APP_USER};
GRANT ALL ON ALL SEQUENCES  IN SCHEMA public TO ${APP_USER};
GRANT ALL ON ALL FUNCTIONS  IN SCHEMA public TO ${APP_USER};

-- Grant future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT ALL ON TABLES    TO ${APP_USER};
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT ALL ON SEQUENCES TO ${APP_USER};
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT ALL ON FUNCTIONS TO ${APP_USER};
SQL