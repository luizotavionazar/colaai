ALTER TABLE "tokensRecuperacaoSenha"
  ADD COLUMN "ipSolicitacao" VARCHAR(45),
  ADD COLUMN "invalidadoEm" TIMESTAMP NULL,
  ADD COLUMN "motivoEncerramento" VARCHAR(60) NULL;

CREATE TABLE "controleRecuperacaoSenha" (
  "id" BIGSERIAL PRIMARY KEY,
  "ip" VARCHAR(45) NOT NULL UNIQUE,
  "janelaInicio" TIMESTAMP NOT NULL,
  "quantidade" INTEGER NOT NULL,
  "bloqueadoAte" TIMESTAMP NULL,
  "dataCriacao" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "dataAtualiza" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);