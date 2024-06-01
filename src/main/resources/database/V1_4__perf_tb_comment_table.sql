use intelli_note;

alter table tb_comment
    add agree_num bigint default 0 not null comment '点赞量';

alter table tb_comment
    add reply_num bigint default 0 not null comment '回复量';