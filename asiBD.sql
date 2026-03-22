CREATE DATABASE Asi;
USE Asi;

CREATE TABLE Usuario (
id_usuario INT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(45) NOT NULL,
senha_hash VARCHAR(60) NOT NULL UNIQUE,
email VARCHAR(255) NOT NULL UNIQUE,
telefone VARCHAR(255) NOT NULL UNIQUE,
salt VARCHAR(30) NOT NULL UNIQUE,
senha_mestra_hash VARCHAR(60) NOT NULL UNIQUE,
data_criacao DATE NOT NULL
);

CREATE TABLE Senha (
id_senha INT PRIMARY KEY AUTO_INCREMENT,
nome_servico VARCHAR(60) NOT NULL,
iv VARCHAR(64) NOT NULL UNIQUE,
senha_criptografada VARCHAR(128) NOT NULL UNIQUE,
id_usuario INT NOT NULL,
FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
);


CREATE TABLE Analise_link (
id_analise INT PRIMARY KEY AUTO_INCREMENT,
link_verificado VARCHAR(255) NOT NULL,
nivel_risco VARCHAR(6) NOT NULL,
data_analise DATE NOT NULL,
resultado TEXT,
id_usuario INT NOT NULL,
FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
);

CREATE TABLE Analise_arquivo (
id_analise INT PRIMARY KEY AUTO_INCREMENT,
arquivo_verificado VARCHAR(255) NOT NULL,
nivel_risco VARCHAR(6) NOT NULL,
data_analise DATE NOT NULL,
resultado TEXT,
id_usuario INT NOT NULL,
FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
);