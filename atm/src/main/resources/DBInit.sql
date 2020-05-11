create user if not exists BankManager password 'VeryStrongPassword';
create schema if not exists Bank;
grant all on schema Bank to BankManager;

create table if not exists Bank.AccountType(
    account_type_id number constraint account_type_id_pk primary key,
    account_type varchar2(9 char) constraint account_type_nn not null
);

create table if not exists Bank.Account(
   account_id number constraint account_id_pk primary key,
   account_password varchar2(16 char) constraint account_password_nn not null,
   owner varchar2(30 char) constraint account_owner_nn not null,
   account_type_id number constraint account_type_nn not null,
   balance decimal default 0,
   constraint account_type_id_fk foreign key (account_type_id) references Bank.AccountType(account_type_id)
);
create sequence if not exists Bank.account_sequence start with 1 increment by 1;

create table if not exists Bank.Card(
    card_number varchar2(16 char) constraint card_number_pk primary key,
    card_key varchar2(4 char),
    account_id number,
    card_status varchar2(8 char) default 'UNLOCKED',
    constraint card_account_id_fk foreign key (account_id) references Bank.Account(account_id)
);

insert into Bank.AccountType (account_type_id, account_type) values (1, 'CLIENT');
insert into Bank.AccountType (account_type_id, account_type) values (2, 'COLLECTOR');

insert into Bank.Account (account_id, account_password, owner, account_type_id)
values (Bank.account_sequence.nextval, concat('StrongPassword', to_char(Bank.account_sequence.currval)), 'Максим', 1);
insert into Bank.Account (account_id, account_password, owner, account_type_id)
values (Bank.account_sequence.nextval, concat('StrongPassword', to_char(Bank.account_sequence.currval)), 'Семен', 1);
insert into Bank.Account (account_id, account_password, owner, account_type_id)
values (Bank.account_sequence.nextval, concat('StrongPassword', to_char(Bank.account_sequence.currval)), 'Мария', 1);
insert into Bank.Account (account_id, account_password, owner, account_type_id)
values (Bank.account_sequence.nextval, concat('StrongPassword', to_char(Bank.account_sequence.currval)), 'Инкассатор Валера', 2);

insert into Bank.Card(card_number, card_key, account_id) values ('1234123412340011', '0001', 2);
insert into Bank.Card(card_number, card_key, account_id) values ('1234123412340021', '1001', 3);
insert into Bank.Card(card_number, card_key, account_id) values ('1234123412340022', '1002', 3);
insert into Bank.Card(card_number, card_key, account_id) values ('1234123412340031', '2001', 4);