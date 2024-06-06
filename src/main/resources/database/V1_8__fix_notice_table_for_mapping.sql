use intelli_note;
alter table notice
    change is_read read_tick tinyint(1) not null comment '是否已读';