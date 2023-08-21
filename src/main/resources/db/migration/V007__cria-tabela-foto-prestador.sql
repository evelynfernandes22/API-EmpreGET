create table foto_prestador(
	prestador_id bigint not null,
	nome_arquivo varchar(150) not null, 
	content_type varchar(80) not null,
	tamanho int not null,
	
	primary key(prestador_id),
	constraint fk_foto_prestador_prestador foreign key(prestador_id) references prestador(id)

)engine=InnoDB default charset=utf8mb4;