create table avaliacao(
       id bigint not null auto_increment,
       prestador_id bigint not null,
       cliente_id bigint not null,
       ordem_servico_id bigint not null,
       estrelas bigint null,
       comentario varchar(255) null,
       data_do_cadastro datetime not null,
                                                                
       primary key (id),
              
       constraint fk_avaliacao_prestador foreign key (prestador_id) references prestador(id),
       constraint fk_avaliacao_cliente foreign key (cliente_id) references cliente(id),
       constraint fk_avaliacao_ordem_servico foreign key (ordem_servico_id) references ordem_servico(id)
       
)engine=InnoDB default charset=utf8mb4;



