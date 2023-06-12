create table prestador(
                        id bigint not null auto_increment,
                        usuario_id bigint not null,
                        nome varchar(60) not null,
                        rg varchar(09) not null,
                        cpf varchar(20) not null,
                        telefone varchar(20) not null,                       
                        
                        end_logradouro varchar(255) not null,
                        end_numero varchar(30) not null,
                        end_complemento varchar(60),
                        end_cep varchar(10) not null,                        
                        end_cidade varchar(50) not null,
                        end_bairro varchar(150) not null,
                        end_estado varchar(30) not null,
                        end_pais varchar(30) not null,
						
                        servico_descricao varchar(255) not null,
                        servico_valor decimal(10,2) not null,
                        
                        disponibilidade_na_semana varchar(255) not null,
                        observacao varchar(255),
                        regiao varchar(20) not null,                                          
                        data_do_cadastro datetime not null,
                        data_da_atualizacao datetime not null,
                        
                        primary key (id),
                        
                        constraint fk_prestador_usuario foreign key (usuario_id) references usuario(id)                        
)engine=InnoDB default charset=utf8mb4;
