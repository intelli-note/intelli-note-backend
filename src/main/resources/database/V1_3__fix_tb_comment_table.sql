use intelli_note;

alter table tb_comment
    drop foreign key tb_comment_ibfk_2;

alter table tb_comment
    add constraint tb_comment_ibfk_2
        foreign key (link_note_id) references note (id)
            on update cascade;

