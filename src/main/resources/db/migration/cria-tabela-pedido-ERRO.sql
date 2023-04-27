create table pedido(
       id bigint not null auto_increment,
       cliente_id bigint not null,
       prestador_id bigint not null,
       tipo_de_diaria varchar(20) not null,
       data_do_pedido datetime not null,
       data_da_finalizacao datetime,
       status varchar(20) not null,
                        
        end_logradouro varchar(255) not null,
        end_numero varchar(30) not null,
        end_complemento varchar(60),
        end_cep varchar(10) not null,                        
        end_cidade varchar(40) not null,
        end_bairro varchar(30) not null,
        end_estado varchar(30) not null,
        end_pais varchar(30) not null,
						
        servico_descricao varchar(255) not null,
        servico_valor decimal(10,2) not null,
                                               
        primary key (id),
        
        constraint fk_pedido_cliente foreign key (cliente_id) references cliente (id),
        constraint fk_pedido_prestador foreign key (prestador_id) references prestador(id)
)engine=InnoDB default charset=utf8;
