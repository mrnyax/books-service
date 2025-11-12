create table wallets
(
    id         uuid primary key,
    owner      varchar(120) not null,
    version    bigint       not null default 0,
    created_at timestamptz  not null default now()
);