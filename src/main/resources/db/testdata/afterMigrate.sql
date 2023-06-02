set foreign_key_checks=0;

delete from cliente;
delete from prestador;
delete from ordem_servico;
delete from usuarios;

set foreign_key_checks=1;

alter table cliente auto_increment = 1;
alter table prestador auto_increment = 1;
alter table ordem_servico auto_increment = 1;
alter table usuarios auto_increment = 1;

insert into cliente (id, nome, rg, cpf, telefone, email, data_do_cadastro, data_da_atualizacao, end_logradouro, end_numero, end_complemento, end_cep, end_cidade, end_bairro, end_estado, end_pais) values 
(1, 'Ana Pereira', '7777777-7', '555.444.555-20', '43 9999-9999', 'ana@email.com', utc_timestamp, utc_timestamp, 'Rua das Flores', '500', 'casa', '86000-000', 'Londrina', 'Jardim Primavera', 'Paraná', 'BRA');
insert into cliente (id, nome, rg, cpf, telefone, email, data_do_cadastro, data_da_atualizacao, end_logradouro, end_numero, end_complemento, end_cep, end_cidade, end_bairro, end_estado, end_pais) values 
(2, 'Helena Martins', '2222222-0', '553.444.555-22', '43 9999-9988', 'helena@email.com', utc_timestamp, utc_timestamp, 'Rua Higienopolis', '1250', 'Apto. 1201', '86000-100', 'Londrina', 'Higienópolis', 'Paraná', 'BRA');
insert into cliente (id, nome, rg, cpf, telefone, email, data_do_cadastro, data_da_atualizacao, end_logradouro, end_numero, end_complemento, end_cep, end_cidade, end_bairro, end_estado, end_pais) values 
(3, 'Marina Lisboa', '1111111-2', '554.444.555-21', '43 9999-9977', 'marina@email.com', utc_timestamp, utc_timestamp, 'Rua Pernambuco', '485', 'casa', '86200-000', 'Londrina', 'Centro', 'Paraná', 'BRA');

insert into prestador(id, nome, rg, cpf, telefone, email, data_do_cadastro, data_da_atualizacao, end_logradouro, end_numero, end_complemento, end_cep, end_cidade, end_bairro, end_estado, end_pais, servico_descricao, servico_valor, disponibilidade_na_semana, observacao, regiao) values
(1, 'Sueli Cavalcante', '1234567-8', '111.222.333-44', '43 9999-9991', 'sueli@email.com', utc_timestamp, utc_timestamp, 'Rua Venezuela', '500', 'casa', '86010-680', 'Londrina', 'Vila Brasil', 'Paraná', 'BRA', 'Faxina, lavar e passar', 150, 'De Seguda à sexta-feira, horário comercial', 'Não passeio com pets.', 'CENTRO'); 
insert into prestador(id, nome, rg, cpf, telefone, email, data_do_cadastro, data_da_atualizacao, end_logradouro, end_numero, end_complemento, end_cep, end_cidade, end_bairro, end_estado, end_pais, servico_descricao, servico_valor, disponibilidade_na_semana, observacao, regiao) values
(2, 'Marinete da Silva', '1234567-1', '110.221.332-43', '43 9999-9992', 'marinete@email.com', utc_timestamp, utc_timestamp, 'Rua Lázaro José Carias de Souza', '116', 'casa', '86088-070', 'Londrina', 'Conjunto Semiramis Barros Braga', 'Paraná', 'BRA', 'Faxina, lavar e passar', 150, 'De Seguda à sexta-feira, horário comercial', 'Passeio com pets.', 'NORTE');

insert into ordem_servico(id, cliente_id, prestador_id, data_servico, periodo, status_agenda, tipo_de_diaria, data_da_solicitacao, data_da_finalizacao, status_ordem_servico) values
(1, 1, 1, '2023-05-20T08:00:00', 'MATUTINO', 'PRE_RESERVADO','MEIA_DIARIA', utc_timestamp, null, 'AGUARDANDO_ACEITE');
insert into ordem_servico(id, cliente_id, prestador_id, data_servico, periodo, status_agenda, tipo_de_diaria, data_da_solicitacao, data_da_finalizacao, status_ordem_servico) values
(2, 3, 2, '2023-05-25T08:00:00', 'COMERCIAL', 'PRE_RESERVADO','DIARIA_CHEIA', utc_timestamp, null, 'AGUARDANDO_ACEITE');

insert into usuarios (id, nome, login, senha, sou_cliente, data_do_cadastro) values
(1, 'Roberto Carlos', 'roberto.carlos@email.com', '123', 1,  utc_timestamp),
(2, 'Maria Joaquina', 'maria.j@email.com', '123', 0,  utc_timestamp),
(3, 'Marcos Santana', 'cabelo@email.com', '123', 1,  utc_timestamp);