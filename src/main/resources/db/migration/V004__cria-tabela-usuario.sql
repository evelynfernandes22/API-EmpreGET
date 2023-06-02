create table usuarios (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome VARCHAR(60) NOT NULL,
    login VARCHAR(60) NOT NULL,
    senha VARCHAR(20) NOT NULL,
    sou_cliente tinyint(1) not null,
    data_do_cadastro datetime not null
       
)engine=InnoDB default charset=utf8mb4;
