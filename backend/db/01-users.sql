DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'orderuser') THEN
        CREATE ROLE orderuser LOGIN PASSWORD 'orderpass';
    END IF;
END
$$;