use intelli_note;

alter table comment_like
    change note_id comment_id bigint not null comment '评论ID';

alter table comment_like
    drop foreign key comment_like_ibfk_1;

alter table comment_like
    drop key note_id;

alter table comment_like
    add constraint comment_id
        unique (comment_id, user_id);


alter table comment_like
    add constraint comment_like_ibfk_1
        foreign key (comment_id) references tb_comment (id)
            on update cascade on delete cascade;

