CREATE TABLE "user" (
  id BIGSERIAL CONSTRAINT user_id_pkey PRIMARY KEY,
  name TEXT,
  email TEXT,
  password TEXT,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ,
  deleted_at TIMESTAMPTZ
) WITH (FILLFACTOR=100);

CREATE UNIQUE INDEX user_email_idx ON "user" (email);
