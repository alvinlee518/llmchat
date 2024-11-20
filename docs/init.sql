create table if not exists oauth_user
(
    id              bigint                                                                  not null
    primary key,
    dept_id         bigint       default 0                                                  not null,
    name            varchar(64)  default ''::character varying                              not null,
    username        varchar(64)  default ''::character varying                              not null
    unique,
    password        varchar(128) default ''::character varying                              not null,
    phone           varchar(32)  default ''::character varying                              not null,
    email           varchar(128) default ''::character varying                              not null,
    gender          integer      default 0                                                  not null,
    birthday        date         default '1970-01-01'::date                                 not null,
    last_login_ip   varchar(32)  default ''::character varying                              not null,
    last_login_time timestamp(6) default '1970-01-01 00:00:00'::timestamp without time zone not null,
    remark          varchar(256) default ''::character varying                              not null,
    status          integer      default 0                                                  not null,
    create_by       varchar(64)  default ''::character varying                              not null,
    create_at       timestamp(6) default CURRENT_TIMESTAMP                                  not null,
    update_by       varchar(32)  default ''::character varying                              not null,
    update_at       timestamp(6) default CURRENT_TIMESTAMP                                  not null
    );

comment on column oauth_user.dept_id is '部门Id';

comment on column oauth_user.name is '姓名';

comment on column oauth_user.username is '用户名';

comment on column oauth_user.password is '密码';

comment on column oauth_user.phone is '手机号';

comment on column oauth_user.email is '邮箱';

comment on column oauth_user.gender is '性别:0-未知;1-男;2-女;';

comment on column oauth_user.birthday is '生日';

comment on column oauth_user.last_login_ip is '最后登录IP';

comment on column oauth_user.last_login_time is '最后登录时间';

comment on column oauth_user.remark is '备注';

comment on column oauth_user.status is '状态:0-无效;1-有效';

comment on column oauth_user.create_by is '创建人';

comment on column oauth_user.create_at is '创建时间';

comment on column oauth_user.update_by is '更新人';

comment on column oauth_user.update_at is '更新时间';

alter table oauth_user
    owner to postgres;

create table if not exists oauth_post
(
    id        bigint                                     not null
    primary key,
    name      varchar(64)  default ''::character varying not null,
    code      varchar(64)  default ''::character varying not null
    unique,
    sorting   integer      default 0                     not null,
    remark    varchar(256) default ''::character varying not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_post is '岗位';

comment on column oauth_post.name is '名称';

comment on column oauth_post.code is '编码';

comment on column oauth_post.sorting is '排序';

comment on column oauth_post.remark is '备注';

comment on column oauth_post.status is '状态:0-无效;1-有效';

comment on column oauth_post.create_by is '创建人';

comment on column oauth_post.create_at is '创建时间';

comment on column oauth_post.update_by is '更新人';

comment on column oauth_post.update_at is '更新时间';

alter table oauth_post
    owner to postgres;

create table if not exists oauth_role
(
    id         bigint                                     not null
    primary key,
    name       varchar(64)  default ''::character varying not null,
    code       varchar(64)  default ''::character varying not null
    unique,
    data_scope integer      default 0                     not null,
    sorting    integer      default 0                     not null,
    remark     varchar(256) default ''::character varying not null,
    status     integer      default 0                     not null,
    create_by  varchar(64)  default ''::character varying not null,
    create_at  timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by  varchar(64)  default ''::character varying not null,
    update_at  timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_role is '角色';

comment on column oauth_role.name is '名称';

comment on column oauth_role.code is '编码';

comment on column oauth_role.data_scope is '数据范围(0-全部数据权限;1-自定义数据权限-;2-本部门数据权限;3-本部门及以下数据权限;4-仅本人数据权限)';

comment on column oauth_role.sorting is '排序';

comment on column oauth_role.remark is '备注';

comment on column oauth_role.status is '状态:0-无效;1-有效';

comment on column oauth_role.create_by is '创建人';

comment on column oauth_role.create_at is '创建时间';

comment on column oauth_role.update_by is '更新人';

comment on column oauth_role.update_at is '更新时间';

alter table oauth_role
    owner to postgres;

create table if not exists oauth_dept
(
    id        bigint                                     not null
    primary key,
    parent_id bigint       default 0                     not null,
    name      varchar(64)  default ''::character varying not null,
    code      varchar(64)  default ''::character varying not null
    unique,
    sorting   integer      default 0                     not null,
    remark    varchar(256) default ''::character varying not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_dept is '部门';

comment on column oauth_dept.parent_id is '上级Id';

comment on column oauth_dept.name is '名称';

comment on column oauth_dept.code is '编码';

comment on column oauth_dept.sorting is '排序';

comment on column oauth_dept.remark is '备注';

comment on column oauth_dept.status is '状态:0-无效;1-有效';

comment on column oauth_dept.create_by is '创建人';

comment on column oauth_dept.create_at is '创建时间';

comment on column oauth_dept.update_by is '更新人';

comment on column oauth_dept.update_at is '更新时间';

alter table oauth_dept
    owner to postgres;

create table if not exists oauth_menu
(
    id         bigint                                     not null
    primary key,
    parent_id  bigint       default 0                     not null,
    menu_type  integer      default 0                     not null,
    name       varchar(64)  default ''::character varying not null,
    path       varchar(256) default ''::character varying not null,
    component  varchar(256) default ''::character varying not null,
    perms      varchar(64)  default ''::character varying not null,
    target     integer      default 0                     not null,
    hidden     integer      default 0                     not null,
    keep_alive integer      default 0                     not null,
    sorting    integer      default 0                     not null,
    remark     varchar(256) default ''::character varying not null,
    status     integer      default 0                     not null,
    create_by  varchar(64)  default ''::character varying not null,
    create_at  timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by  varchar(64)  default ''::character varying not null,
    update_at  timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_menu is '菜单';

comment on column oauth_menu.parent_id is '上级Id';

comment on column oauth_menu.menu_type is '类型:0-目录;1-菜单;2-按钮';

comment on column oauth_menu.name is '名称';

comment on column oauth_menu.path is '请求地址';

comment on column oauth_menu.component is '组件地址';

comment on column oauth_menu.perms is '权限标识';

comment on column oauth_menu.target is '打开方式:0-页签;2-新窗口';

comment on column oauth_menu.hidden is '隐藏路由:0-否;2-是';

comment on column oauth_menu.keep_alive is '缓存网页:0-否;2-是';

comment on column oauth_menu.sorting is '排序';

comment on column oauth_menu.remark is '备注';

comment on column oauth_menu.status is '状态:0-无效;1-有效';

comment on column oauth_menu.create_by is '创建人';

comment on column oauth_menu.create_at is '创建时间';

comment on column oauth_menu.update_by is '更新人';

comment on column oauth_menu.update_at is '更新时间';

alter table oauth_menu
    owner to postgres;

create table if not exists dict_data
(
    id        bigint                                     not null
    primary key,
    type_id   bigint       default 0                     not null,
    name      varchar(64)  default ''::character varying not null,
    code      varchar(64)  default ''::character varying not null,
    sorting   integer      default 0                     not null,
    remark    varchar(256) default ''::character varying not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table dict_data is '字典';

comment on column dict_data.type_id is '类型Id';

comment on column dict_data.name is '名称';

comment on column dict_data.code is '编码';

comment on column dict_data.sorting is '排序';

comment on column dict_data.remark is '备注';

comment on column dict_data.status is '状态:0-无效;1-有效';

comment on column dict_data.create_by is '创建人';

comment on column dict_data.create_at is '创建时间';

comment on column dict_data.update_by is '更新人';

comment on column dict_data.update_at is '更新时间';

alter table dict_data
    owner to postgres;

create table if not exists oauth_user_role
(
    id        bigint                                     not null
    primary key,
    user_id   bigint       default 0                     not null,
    role_id   bigint       default 0                     not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_user_role is '用户角色关联表';

comment on column oauth_user_role.user_id is '用户Id';

comment on column oauth_user_role.role_id is '角色Id';

comment on column oauth_user_role.status is '状态:0-无效;1-有效';

comment on column oauth_user_role.create_by is '创建人';

comment on column oauth_user_role.create_at is '创建时间';

comment on column oauth_user_role.update_by is '更新人';

comment on column oauth_user_role.update_at is '更新时间';

alter table oauth_user_role
    owner to postgres;

create table if not exists oauth_role_menu
(
    id        bigint                                     not null
    primary key,
    role_id   bigint       default 0                     not null,
    menu_id   bigint       default 0                     not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_role_menu is '角色菜单关联表';

comment on column oauth_role_menu.role_id is '角色Id';

comment on column oauth_role_menu.menu_id is '菜单Id';

comment on column oauth_role_menu.status is '状态:0-无效;1-有效';

comment on column oauth_role_menu.create_by is '创建人';

comment on column oauth_role_menu.create_at is '创建时间';

comment on column oauth_role_menu.update_by is '更新人';

comment on column oauth_role_menu.update_at is '更新时间';

alter table oauth_role_menu
    owner to postgres;

create table if not exists oauth_role_dept
(
    id        bigint                                     not null
    primary key,
    role_id   bigint       default 0                     not null,
    dept_id   bigint       default 0                     not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_role_dept is '角色部门关联表';

comment on column oauth_role_dept.role_id is '角色Id';

comment on column oauth_role_dept.dept_id is '部门Id';

comment on column oauth_role_dept.status is '状态:0-无效;1-有效';

comment on column oauth_role_dept.create_by is '创建人';

comment on column oauth_role_dept.create_at is '创建时间';

comment on column oauth_role_dept.update_by is '更新人';

comment on column oauth_role_dept.update_at is '更新时间';

alter table oauth_role_dept
    owner to postgres;

create table if not exists oauth_user_post
(
    id        bigint                                     not null
    primary key,
    user_id   bigint       default 0                     not null,
    post_id   bigint       default 0                     not null,
    status    integer      default 0                     not null,
    create_by varchar(64)  default ''::character varying not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table oauth_user_post is '用户岗位关联表';

comment on column oauth_user_post.user_id is '用户Id';

comment on column oauth_user_post.post_id is '岗位Id';

comment on column oauth_user_post.status is '状态:0-无效;1-有效';

comment on column oauth_user_post.create_by is '创建人';

comment on column oauth_user_post.create_at is '创建时间';

comment on column oauth_user_post.update_by is '更新人';

comment on column oauth_user_post.update_at is '更新时间';

alter table oauth_user_post
    owner to postgres;

create table if not exists ai_model
(
    id             bigint                                     not null
    primary key,
    model_name     varchar(128) default ''::character varying not null,
    model_provider integer      default 0                     not null,
    model_type     integer      default 0                     not null,
    base_url       varchar(255) default ''::character varying not null,
    api_key        varchar(255) default ''::character varying not null,
    secret_key     varchar(255) default ''::character varying not null,
    status         integer      default 0                     not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP     not null,
    create_by      varchar(64)  default ''::character varying not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by      varchar(64)  default ''::character varying not null
    );

comment on table ai_model is '模型配置';

comment on column ai_model.id is '主键';

comment on column ai_model.model_name is '模型名称';

comment on column ai_model.model_provider is '模型提供商';

comment on column ai_model.model_type is '模型类型';

comment on column ai_model.base_url is 'API域名';

comment on column ai_model.api_key is 'API Key';

comment on column ai_model.secret_key is 'Secret Key';

comment on column ai_model.status is '有效状态：0-无效；1-有效';

comment on column ai_model.create_at is '创建时间';

comment on column ai_model.create_by is '创建人';

comment on column ai_model.update_at is '更新时间';

comment on column ai_model.update_by is '更新人';

alter table ai_model
    owner to postgres;

create table if not exists ai_dataset
(
    id          bigint                                       not null
    primary key,
    embed_id    bigint         default 0                     not null,
    name        varchar(64)    default ''::character varying not null,
    description varchar(256)   default ''::character varying not null,
    search_mode integer        default 2                     not null,
    top_k       integer        default 3                     not null,
    score       numeric(10, 2) default 0.5                   not null,
    status      integer        default 0                     not null,
    create_at   timestamp(6)   default CURRENT_TIMESTAMP     not null,
    create_by   varchar(64)    default ''::character varying not null,
    update_at   timestamp(6)   default CURRENT_TIMESTAMP     not null,
    update_by   varchar(64)    default ''::character varying not null
    );

comment on table ai_dataset is '数据集';

comment on column ai_dataset.id is '主键';

comment on column ai_dataset.embed_id is '向量模型';

comment on column ai_dataset.name is '数据集名称';

comment on column ai_dataset.description is '数据集描述';

comment on column ai_dataset.search_mode is '检索模式:0-向量检索;1-全文检索;2-混合检索;';

comment on column ai_dataset.top_k is '召回数量';

comment on column ai_dataset.score is '相似度';

comment on column ai_dataset.status is '有效状态：0-无效；1-有效';

comment on column ai_dataset.create_at is '创建时间';

comment on column ai_dataset.create_by is '创建人';

comment on column ai_dataset.update_at is '更新时间';

comment on column ai_dataset.update_by is '更新人';

alter table ai_dataset
    owner to postgres;

create table if not exists file_detail
(
    id                bigint       not null
    primary key,
    url               varchar(512) not null,
    size              bigint,
    filename          varchar(256) default NULL::character varying,
    original_filename varchar(256) default NULL::character varying,
    base_path         varchar(256) default NULL::character varying,
    path              varchar(256) default NULL::character varying,
    ext               varchar(32)  default NULL::character varying,
    content_type      varchar(128) default NULL::character varying,
    platform          varchar(32)  default NULL::character varying,
    th_url            varchar(512) default NULL::character varying,
    th_filename       varchar(256) default NULL::character varying,
    th_size           bigint,
    th_content_type   varchar(128) default NULL::character varying,
    object_id         varchar(32)  default NULL::character varying,
    object_type       varchar(32)  default NULL::character varying,
    metadata          text,
    user_metadata     text,
    th_metadata       text,
    th_user_metadata  text,
    attr              text,
    file_acl          varchar(32)  default NULL::character varying,
    th_file_acl       varchar(32)  default NULL::character varying,
    hash_info         text,
    upload_id         varchar(128) default NULL::character varying,
    upload_status     integer,
    create_time       timestamp(6)
    );

comment on table file_detail is '文件记录表';

comment on column file_detail.id is '文件id';

comment on column file_detail.url is '文件访问地址';

comment on column file_detail.size is '文件大小，单位字节';

comment on column file_detail.filename is '文件名称';

comment on column file_detail.original_filename is '原始文件名';

comment on column file_detail.base_path is '基础存储路径';

comment on column file_detail.path is '存储路径';

comment on column file_detail.ext is '文件扩展名';

comment on column file_detail.content_type is 'MIME类型';

comment on column file_detail.platform is '存储平台';

comment on column file_detail.th_url is '缩略图访问路径';

comment on column file_detail.th_filename is '缩略图名称';

comment on column file_detail.th_size is '缩略图大小，单位字节';

comment on column file_detail.th_content_type is '缩略图MIME类型';

comment on column file_detail.object_id is '文件所属对象id';

comment on column file_detail.object_type is '文件所属对象类型，例如用户头像，评价图片';

comment on column file_detail.metadata is '文件元数据';

comment on column file_detail.user_metadata is '文件用户元数据';

comment on column file_detail.th_metadata is '缩略图元数据';

comment on column file_detail.th_user_metadata is '缩略图用户元数据';

comment on column file_detail.attr is '附加属性';

comment on column file_detail.file_acl is '文件ACL';

comment on column file_detail.th_file_acl is '缩略图文件ACL';

comment on column file_detail.hash_info is '哈希信息';

comment on column file_detail.upload_id is '上传ID，仅在手动分片上传时使用';

comment on column file_detail.upload_status is '上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成';

comment on column file_detail.create_time is '创建时间';

alter table file_detail
    owner to postgres;

create table if not exists ai_document
(
    id            bigint                                              not null
    primary key,
    dataset_id    bigint        default 0                             not null,
    file_id       bigint        default 0                             not null,
    name          varchar(128)  default ''::character varying         not null,
    para_count    integer       default 0                             not null,
    word_count    integer       default 0                             not null,
    data_type     integer       default 0                             not null,
    segment_mode  integer       default 0                             not null,
    separators    varchar(64)[] default '{}'::character varying(64)[] not null,
    chunk_size    integer       default 256                           not null,
    chunk_overlap integer       default 64                            not null,
    clean_rules   integer[]     default '{}'::integer[]               not null,
    state   integer       default 0                             not null,
    failure       text          default ''::text                      not null,
    embed_cols    integer       default 3                             not null,
    reply_cols    integer       default 3                             not null,
    status        integer       default 0                             not null,
    create_at     timestamp(6)  default CURRENT_TIMESTAMP             not null,
    create_by     varchar(64)   default ''::character varying         not null,
    update_at     timestamp(6)  default CURRENT_TIMESTAMP             not null,
    update_by     varchar(64)   default ''::character varying         not null
    );

comment on table ai_document is '数据文档';

comment on column ai_document.id is '主键';

comment on column ai_document.dataset_id is '数据集Id';

comment on column ai_document.file_id is '文件Id';

comment on column ai_document.name is '文档名称';

comment on column ai_document.para_count is '分段数';

comment on column ai_document.word_count is '字符数';

comment on column ai_document.data_type is '数据类型:0-非结构;1-结构';

comment on column ai_document.segment_mode is '分段模式:0-智能切分;1-自定义切分';

comment on column ai_document.separators is '分段标识符';

comment on column ai_document.chunk_size is '分段预估长度';

comment on column ai_document.chunk_overlap is '分段重叠长度';

comment on column ai_document.clean_rules is '数据清洗规则';

comment on column ai_document.state is '索引状态:0-待处理;1-处理中;2-已处理;3-处理失败';

comment on column ai_document.failure is '失败原因';

comment on column ai_document.embed_cols is '检索字段(位运算):1-title;2-content;';

comment on column ai_document.reply_cols is '模型回复字段(位运算):1-title;2-content;';

comment on column ai_document.status is '有效状态：0-无效；1-有效';

comment on column ai_document.create_at is '创建时间';

comment on column ai_document.create_by is '创建人';

comment on column ai_document.update_at is '更新时间';

comment on column ai_document.update_by is '更新人';

alter table ai_document
    owner to postgres;

create table if not exists ai_app_dataset
(
    id         bigint                                     not null
    primary key,
    app_id     bigint       default 0                     not null,
    dataset_id bigint       default 0                     not null,
    status     integer      default 0                     not null,
    create_by  varchar(64)  default ''::character varying not null,
    create_at  timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by  varchar(64)  default ''::character varying not null,
    update_at  timestamp(6) default CURRENT_TIMESTAMP     not null
    );

comment on table ai_app_dataset is '应用数据集关联表';

comment on column ai_app_dataset.app_id is '应用Id';

comment on column ai_app_dataset.dataset_id is '数据集Id';

comment on column ai_app_dataset.status is '状态:0-无效;1-有效';

comment on column ai_app_dataset.create_by is '创建人';

comment on column ai_app_dataset.create_at is '创建时间';

comment on column ai_app_dataset.update_by is '更新人';

comment on column ai_app_dataset.update_at is '更新时间';

alter table ai_app_dataset
    owner to postgres;

create table if not exists ai_chat_message
(
    id                bigint                                     not null
    primary key,
    chat_id           bigint       default 0                     not null,
    user_id           bigint       default 0                     not null,
    instruction       text         default ''::text              not null,
    prompt            text         default ''::text              not null,
    prompt_tokens     integer      default 0                     not null,
    completion        text         default ''::text              not null,
    completion_tokens integer      default 0                     not null,
    failed            integer      default 0                     not null,
    citations         text         default ''::text              not null,
    duration          bigint       default 0                     not null,
    rating            integer      default 0                     not null,
    status            integer      default 0                     not null,
    create_at         timestamp(6) default CURRENT_TIMESTAMP     not null,
    create_by         varchar(64)  default ''::character varying not null,
    update_at         timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by         varchar(64)  default ''::character varying not null
    );

comment on table ai_chat_message is '对话';

comment on column ai_chat_message.id is '主键';

comment on column ai_chat_message.chat_id is '对话Id';

comment on column ai_chat_message.user_id is '用户id';

comment on column ai_chat_message.instruction is '指令';

comment on column ai_chat_message.prompt is '模型输入';

comment on column ai_chat_message.prompt_tokens is '模型输入消耗tokens';

comment on column ai_chat_message.completion is '模型输出';

comment on column ai_chat_message.completion_tokens is '模型输出消耗tokens';

comment on column ai_chat_message.failed is '是否异常:0-否;1-是';

comment on column ai_chat_message.citations is '引文';

comment on column ai_chat_message.duration is '耗时(ms)';

comment on column ai_chat_message.rating is '有效状态：0-未投票；1-赞成；2-不赞成';

comment on column ai_chat_message.status is '有效状态：0-无效；1-有效';

comment on column ai_chat_message.create_at is '创建时间';

comment on column ai_chat_message.create_by is '创建人';

comment on column ai_chat_message.update_at is '更新时间';

comment on column ai_chat_message.update_by is '更新人';

alter table ai_chat_message
    owner to postgres;

create table if not exists ai_paragraph
(
    id          bigint                                      not null
    primary key,
    dataset_id  bigint        default 0                     not null,
    doc_id      bigint        default 0                     not null,
    index_id    varchar(64)   default ''::character varying not null,
    title       varchar(128)  default ''::character varying not null,
    content     varchar(2048) default ''::character varying not null,
    position    integer       default 0                     not null,
    word_count  integer       default 0                     not null,
    hit_count   integer       default 0                     not null,
    state integer       default 0                     not null,
    failure     text          default ''::text              not null,
    status      integer       default 0                     not null,
    create_at   timestamp(6)  default CURRENT_TIMESTAMP     not null,
    create_by   varchar(64)   default ''::character varying not null,
    update_at   timestamp(6)  default CURRENT_TIMESTAMP     not null,
    update_by   varchar(64)   default ''::character varying not null
    );

comment on table ai_paragraph is '数据文档';

comment on column ai_paragraph.id is '主键';

comment on column ai_paragraph.dataset_id is '数据集Id';

comment on column ai_paragraph.doc_id is '文档Id';

comment on column ai_paragraph.index_id is '索引Id';

comment on column ai_paragraph.title is '分段标题';

comment on column ai_paragraph.content is '分段内容';

comment on column ai_paragraph.position is '分段位置';

comment on column ai_paragraph.word_count is '字符数';

comment on column ai_paragraph.hit_count is '命中次数';

comment on column ai_paragraph.state is '索引状态:0-待处理;1-处理中;2-已处理;3-处理失败';

comment on column ai_paragraph.failure is '失败原因';

comment on column ai_paragraph.status is '有效状态：0-无效；1-有效';

comment on column ai_paragraph.create_at is '创建时间';

comment on column ai_paragraph.create_by is '创建人';

comment on column ai_paragraph.update_at is '更新时间';

comment on column ai_paragraph.update_by is '更新人';

alter table ai_paragraph
    owner to postgres;

create table if not exists ai_app
(
    id              bigint                                       not null
    primary key,
    model_id        bigint         default 0                     not null,
    rerank_id       bigint         default 0                     not null,
    name            varchar(64)    default ''::character varying not null,
    description     varchar(256)   default ''::character varying not null,
    source_enabled  integer        default 0                     not null,
    rewrite_enabled integer        default 1                     not null,
    suggest_enabled integer        default 0                     not null,
    top_k           integer        default 3                     not null,
    score           numeric(10, 2) default 0.5                   not null,
    temperature     numeric(10, 2) default 0.8                   not null,
    max_tokens      integer        default 1024                  not null,
    max_memory      integer        default 5                     not null,
    prompt          varchar(8192)  default ''::character varying not null,
    prologue        varchar(4096)  default ''::character varying not null,
    status          integer        default 0                     not null,
    create_at       timestamp(6)   default CURRENT_TIMESTAMP     not null,
    create_by       varchar(64)    default ''::character varying not null,
    update_at       timestamp(6)   default CURRENT_TIMESTAMP     not null,
    update_by       varchar(64)    default ''::character varying not null
    );

comment on table ai_app is '应用';

comment on column ai_app.id is '主键';

comment on column ai_app.model_id is 'LLM模型';

comment on column ai_app.rerank_id is 'Rerank模型';

comment on column ai_app.name is '应用名称';

comment on column ai_app.description is '应用描述';

comment on column ai_app.source_enabled is '展示回答来源:0-否;1-是';

comment on column ai_app.rewrite_enabled is '多轮对话改写:0-否;1-是';

comment on column ai_app.suggest_enabled is '问题建议:0-否;1-是';

comment on column ai_app.top_k is '召回数量';

comment on column ai_app.score is '相似度';

comment on column ai_app.temperature is '温度系数';

comment on column ai_app.max_tokens is '最大输出长度';

comment on column ai_app.max_memory is '携带上下文轮数';

comment on column ai_app.prompt is '提示词';

comment on column ai_app.prologue is '开场白';

comment on column ai_app.status is '有效状态：0-无效；1-有效';

comment on column ai_app.create_at is '创建时间';

comment on column ai_app.create_by is '创建人';

comment on column ai_app.update_at is '更新时间';

comment on column ai_app.update_by is '更新人';

alter table ai_app
    owner to postgres;

create table if not exists ai_chat
(
    id        bigint                                     not null
    primary key,
    app_id    bigint       default 0                     not null,
    model_id  bigint       default 0                     not null,
    user_id   bigint       default 0                     not null,
    title     varchar(256) default ''::character varying not null,
    testing   integer      default 0                     not null,
    status    integer      default 0                     not null,
    create_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    create_by varchar(64)  default ''::character varying not null,
    update_at timestamp(6) default CURRENT_TIMESTAMP     not null,
    update_by varchar(64)  default ''::character varying not null
    );

comment on table ai_chat is '对话';

comment on column ai_chat.id is '主键';

comment on column ai_chat.app_id is '应用Id';

comment on column ai_chat.model_id is '模型Id';

comment on column ai_chat.user_id is '用户id';

comment on column ai_chat.title is '标题';

comment on column ai_chat.testing is '是否测试:0-否;1-是';

comment on column ai_chat.status is '有效状态：0-无效；1-有效';

comment on column ai_chat.create_at is '创建时间';

comment on column ai_chat.create_by is '创建人';

comment on column ai_chat.update_at is '更新时间';

comment on column ai_chat.update_by is '更新人';

alter table ai_chat
    owner to postgres;



INSERT INTO "public"."oauth_user" ( "id", "dept_id", "name", "username", "password", "phone", "email", "gender", "birthday", "last_login_ip", "last_login_time", "remark", "status" )
VALUES
    ( 1857227850498183170, 0, '超级管理员', 'llmchat', '7e00734692dd9c5c8c1f0b0355dd8f4d073bda22f094517062737b4cb8807c09', '13444444444', 'admin@llmchat.ai', 0, '1970-01-01', '0:0:0:0:0:0:0:1', '2024-11-15 09:19:38.379059', '', 1 );