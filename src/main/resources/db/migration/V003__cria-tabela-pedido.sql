create table pedido(
       id bigint not null auto_increment,
       cliente_id bigint not null,
       prestador_id bigint not null,
       tipo_de_diaria varchar(20) not null,
       data_do_pedido datetime not null,
       data_da_finalizacao datetime,
       status varchar(20) not null,
                                                    
       primary key (id),
        
       constraint fk_pedido_cliente foreign key (cliente_id) references cliente (id),
       constraint fk_pedido_prestador foreign key (prestador_id) references prestador(id)
       
)engine=InnoDB default charset=utf8;
