use intelli_note;
alter table notice
    modify type int not null comment '通知类型：0-关注通知，1-收藏通知，2-评论通知，3-点赞通知，4-交易通知';