create table usuario (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,  
    email VARCHAR(60) NOT NULL,
    senha VARCHAR(20) NOT NULL,
    sou_cliente tinyint(1) not null    
       
)engine=InnoDB default charset=utf8mb4;
