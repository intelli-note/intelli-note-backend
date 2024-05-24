use intelli_note;

alter table bill
    modify note_id bigint null comment '关联笔记ID，可以为空';