CREATE TABLE pedido_agenda (
	
	  pedido_id bigint NOT NULL,
	  agenda_id bigint NOT NULL,
	  
	  PRIMARY KEY (pedido_id, agenda_id),
	  
	  KEY fk_pedido_agenda_agenda (agenda_id),
	  
	  CONSTRAINT fk_pedido_agenda_agenda FOREIGN KEY (agenda_id) REFERENCES agenda (id),
	  CONSTRAINT fk_pedido_agenda_agenda FOREIGN KEY (pedido_id) REFERENCES pedido (id)
	  
) engine=InnoDB default charset=utf8;