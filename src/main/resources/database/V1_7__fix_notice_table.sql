use intelli_note;

alter table notice
    drop foreign key notice_ibfk_2;

alter table notice
    add constraint notice_ibfk_2
        foreign key (link_follow_id) references follow (id)
            on update cascade on delete cascade;

alter table notice
    drop foreign key notice_ibfk_3;

alter table notice
    add constraint notice_ibfk_3
        foreign key (link_star_note_id) references note_favorite (id)
            on update cascade on delete cascade;

alter table notice
    drop foreign key notice_ibfk_4;

alter table notice
    add constraint notice_ibfk_4
        foreign key (link_star_collection_id) references collection_favorite (id)
            on update cascade on delete cascade;

alter table notice
    drop foreign key notice_ibfk_5;

alter table notice
    add constraint notice_ibfk_5
        foreign key (link_comment_id) references tb_comment (id)
            on update cascade on delete cascade;

alter table notice
    drop foreign key notice_ibfk_6;

alter table notice
    add constraint notice_ibfk_6
        foreign key (link_comment_like_id) references comment_like (id)
            on update cascade on delete cascade;

alter table notice
    drop foreign key notice_ibfk_7;

alter table notice
    add constraint notice_ibfk_7
        foreign key (link_bill_id) references bill (id)
            on update cascade on delete cascade;

