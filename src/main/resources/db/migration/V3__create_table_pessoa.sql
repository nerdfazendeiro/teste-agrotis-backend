CREATE TABLE pessoas
(
    id             BIGINT IDENTITY (1,1) PRIMARY KEY,
    nome           NVARCHAR(255) NOT NULL,
    data_inicial   DATETIME2     NOT NULL,
    data_final     DATETIME2     NOT NULL,
    propriedade_id BIGINT,
    laboratorio_id BIGINT,
    observacoes    NVARCHAR(1000),

    CONSTRAINT fk_pessoa_propriedade FOREIGN KEY (propriedade_id) REFERENCES propriedade (id),
    CONSTRAINT fk_pessoa_laboratorio FOREIGN KEY (laboratorio_id) REFERENCES laboratorio (id)
);