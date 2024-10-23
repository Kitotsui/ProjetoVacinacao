CREATE TABLE public.pessoa
(
    codigo bigserial NOT NULL,
    nome text,
    cpf text,
    data_nascimento_de date,
    data_nascimento_ate date,
    profissao text,
    PRIMARY KEY (codigo)
);