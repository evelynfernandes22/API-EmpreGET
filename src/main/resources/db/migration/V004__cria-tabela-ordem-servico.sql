create table ordem_servico(
       id bigint not null auto_increment,
       cliente_id bigint not null,
       prestador_id bigint not null,
       data_servico date not null,
       periodo varchar(20) not null,
       status_agenda varchar(20) not null, 
      
       tipo_de_diaria varchar(20) not null,
       data_da_solicitacao datetime not null,       
       data_da_finalizacao datetime,
       status_ordem_servico varchar(20) not null,
                                                    
       primary key (id),
        
       constraint fk_os_cliente foreign key (cliente_id) references cliente (id),
       constraint fk_os_prestador foreign key (prestador_id) references prestador(id)
           
)engine=InnoDB default charset=utf8mb4;
 	