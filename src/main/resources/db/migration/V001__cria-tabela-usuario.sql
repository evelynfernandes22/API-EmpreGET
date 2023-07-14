create table usuario (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,  
    email VARCHAR(60) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    role varchar(20) not null
       
)engine=InnoDB default charset=utf8mb4;
