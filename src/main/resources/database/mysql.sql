-- 创建数据库
drop database if exists intelli_note;
create database if not exists intelli_note default character set utf8mb4 collate utf8mb4_unicode_ci;

use intelli_note;

-- 创建数据表
create table tb_user
(
    id        bigint         not null comment '用户ID',
    username  varchar(20)    not null comment '用户名，20字符内',
    avatar    text           not null comment '用户头像url',
    biography varchar(255)   null comment '用户简介，255字符内，可为空',
    gender    boolean        null comment '性别：null-未知，true-男生，false-女生',
    openid    varchar(130)   not null comment '用户微信OpenID',
    balance   decimal(10, 2) not null comment '余额',
    revenue   decimal(10, 2) not null comment '收入',

    primary key (id)
) comment '用户表';

create table follow
(
    id          bigint   not null comment '关注联系ID',
    follower_id bigint   not null comment '当前用户ID',
    follow_id   bigint   not null comment '被关注用户ID',
    create_time datetime not null comment '关注时间',

    unique (follower_id, follow_id),
    primary key (id),
    foreign key (follower_id) references tb_user (id) on update cascade on delete cascade,
    foreign key (follow_id) references tb_user (id) on update cascade on delete cascade
) comment '关注表';

create table note
(
    id             bigint         not null comment '笔记ID',
    title          varchar(50)    not null comment '标题，50字以内',
    cover          text           null comment '封面图片url，可以为空',
    content        longtext       not null comment '内容',

    user_id        bigint         not null comment '作者，用户ID',

    create_time    datetime       not null comment '创建时间',
    update_time    datetime       not null comment '修改时间',
    is_open_public boolean        not null comment '是否公开',
    price          decimal(10, 2) null comment '价格，免费为null/0',

    primary key (id),
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade
) comment '笔记表';

create table collection
(
    id                 bigint       not null comment '合集ID',
    cname              varchar(50)  not null comment '名称，50字以内',
    cover              text         null comment '封面图片url，可以为空',
    brief_introduction varchar(255) null comment '简介，255字以内，可以为空',

    user_id            bigint       not null comment '拥有者用户ID',

    create_time        datetime     not null comment '创建时间',
    is_open_public     boolean      not null comment '是否公开',

    primary key (id),
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade
) comment '合集表';

create table favorite
(
    id                 bigint       not null comment '收藏夹ID',
    fname              varchar(50)  not null comment '名称，50字以内',
    brief_introduction varchar(255) null comment '简介，255字以内，可以为空',

    user_id            bigint       not null comment '拥有者用户ID',

    create_time        datetime     not null comment '创建时间',
    is_option_public   boolean      not null comment '是否公开',
    is_option_default  boolean      not null comment '是否为默认收藏夹',

    primary key (id),
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade
) comment '收藏夹表';

create table note_favorite
(
    id          bigint   not null comment '收藏联系ID',
    note_id     bigint   not null comment '笔记ID',
    favorite_id bigint   not null comment '收藏夹ID',

    create_time datetime not null comment '收藏时间',

    unique (note_id, favorite_id),
    primary key (id),
    foreign key (note_id) references note (id) on update cascade on delete cascade,
    foreign key (favorite_id) references favorite (id) on update cascade on delete cascade
) comment '笔记收藏表';

create table collection_favorite
(
    collection_id bigint   not null comment '笔记ID',
    favorite_id   bigint   not null comment '收藏夹ID',

    create_time   datetime not null comment '收藏时间',

    primary key (collection_id, favorite_id),
    foreign key (collection_id) references collection (id) on update cascade on delete cascade,
    foreign key (favorite_id) references favorite (id) on update cascade on delete cascade
) comment '合集收藏表';

create table note_in_collection
(
    note_id       bigint not null comment '笔记ID',
    collection_id bigint not null comment '合集ID',

    primary key (note_id, collection_id),
    foreign key (note_id) references note (id) on update cascade on delete cascade,
    foreign key (collection_id) references collection (id) on update cascade on delete cascade
) comment '笔记-in-合集表';

create table bill
(
    id          bigint         not null comment '账单ID',
    type        int            not null comment '账单类型：0-收入，1-支出，2-提现，3-充值',
    amount      decimal(10, 2) not null comment '交易金额',
    create_time datetime       not null comment '创建时间',

    user_id     bigint         not null comment '用户ID',
    note_id     bigint         not null comment '关联笔记ID',

    is_deleted  boolean        not null comment '是否逻辑删除',

    primary key (id),
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade,
    foreign key (note_id) references note (id) on update cascade on delete cascade
) comment '账单表';


create table view_history
(
    user_id     bigint   not null comment '用户ID',
    note_id     bigint   not null comment '笔记ID',
    create_time datetime not null comment '查看时间',

    primary key (user_id, note_id),
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade,
    foreign key (note_id) references note (id) on update cascade on delete cascade
) comment '笔记查看历史表';


create table tb_comment
(
    id           bigint   not null comment '评论ID',
    content      text     null comment '评论文本，可选',
    image_list   text     null comment '评论图片列表，可选',
    audio        text     null comment '音频url，可选',
    video        text     null comment '视频url，可选',
    link_note_id bigint   null comment '关联笔记ID，可选',

    note_id      bigint   not null comment '所属笔记ID',
    root_id      bigint   null comment '根评论ID',
    parent_id    bigint   null comment '父评论ID',

    user_id      bigint   not null comment '用户ID',
    create_time  datetime not null comment '评论时间',

    primary key (id),
    foreign key (note_id) references note (id) on update cascade on delete cascade,
    foreign key (link_note_id) references note (id) on update cascade on delete cascade,
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade
) comment '评论表';

create table comment_like
(
    id          bigint   not null comment '评论点赞联系ID',
    note_id     bigint   not null comment '笔记ID',
    user_id     bigint   not null comment '用户ID',
    create_time datetime not null comment '点赞时间',

    unique (note_id, user_id),
    primary key (id),
    foreign key (note_id) references note (id) on update cascade on delete cascade,
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade
) comment '评论点赞表';

create table notice
(
    id              bigint   not null comment '通知ID',
    type            bigint   not null comment '通知类型：0-关注通知，1-收藏通知，2-评论通知，3-点赞通知，4-交易通知',
    link_follow_id  bigint   null comment '关联关注表ID，可选',
    link_star_id    bigint   null comment '关联笔记收藏表ID，可选',
    link_comment_id bigint   null comment '关联评论表ID，可选',
    link_like_id    bigint   null comment '关联评论点赞表ID，可选',
    link_bill_id    bigint   null comment '关联账单ID，可选',

    user_id         bigint   not null comment '用户ID',
    create_time     datetime not null comment '通知时间',
    is_read         boolean  not null comment '是否已读',

    primary key (id),
    foreign key (user_id) references tb_user (id) on update cascade on delete cascade,
    foreign key (link_follow_id) references follow (id) on update cascade on delete set null,
    foreign key (link_star_id) references note_favorite (id) on update cascade on delete set null,
    foreign key (link_comment_id) references tb_comment (id) on update cascade on delete set null,
    foreign key (link_like_id) references comment_like (id) on update cascade on delete set null,
    foreign key (link_bill_id) references bill (id) on update cascade on delete set null
) comment '通知表';