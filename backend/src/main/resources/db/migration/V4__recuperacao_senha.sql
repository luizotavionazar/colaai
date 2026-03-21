CREATE TABLE "tokensRecuperacaoSenha" (
  "id" BIGSERIAL PRIMARY KEY,
  "idUsuario" INTEGER NOT NULL,
  "tokenHash" VARCHAR(64) NOT NULL,
  "expiraEm" TIMESTAMP NOT NULL,
  "usadoEm" TIMESTAMP NULL,
  "dataCriacao" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT "fk_tokens_recuperacao_usuario"
    FOREIGN KEY ("idUsuario") REFERENCES "usuarios"("id")
);

CREATE INDEX "idx_tokens_recuperacao_token_hash"
  ON "tokensRecuperacaoSenha" ("tokenHash");

CREATE INDEX "idx_tokens_recuperacao_usuario"
  ON "tokensRecuperacaoSenha" ("idUsuario");