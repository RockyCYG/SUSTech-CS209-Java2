create table questions
(
    id          integer,
    title       varchar(200),
    score       integer,
    view_count  integer,
    create_year integer
);
create table question_tags
(
    question_id integer,
    tag         varchar(100)
);
create table repos
(
    id           integer,
    title        varchar(200),
    watchers     integer,
    forks        integer,
    created_time varchar(20)
);
create table repos_tags
(
    repo_id integer,
    tag     varchar(50)
);
create table record_forks
(
    id    integer primary key,
    title varchar(200),
    time  varchar(20)
);