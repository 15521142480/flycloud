create table ACT_EVT_LOG
(
    LOG_NR_       bigint auto_increment
        primary key,
    TYPE_         varchar(64)                               null,
    PROC_DEF_ID_  varchar(64)                               null,
    PROC_INST_ID_ varchar(64)                               null,
    EXECUTION_ID_ varchar(64)                               null,
    TASK_ID_      varchar(64)                               null,
    TIME_STAMP_   timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    USER_ID_      varchar(255)                              null,
    DATA_         longblob                                  null,
    LOCK_OWNER_   varchar(255)                              null,
    LOCK_TIME_    timestamp(3)                              null,
    IS_PROCESSED_ tinyint      default 0                    null
)
    collate = utf8_bin;

create table ACT_GE_PROPERTY
(
    NAME_  varchar(64)  not null
        primary key,
    VALUE_ varchar(300) null,
    REV_   int          null
)
    collate = utf8_bin;

create table ACT_HI_ACTINST
(
    ID_                varchar(64)             not null
        primary key,
    REV_               int          default 1  null,
    PROC_DEF_ID_       varchar(64)             not null,
    PROC_INST_ID_      varchar(64)             not null,
    EXECUTION_ID_      varchar(64)             not null,
    ACT_ID_            varchar(255)            not null,
    TASK_ID_           varchar(64)             null,
    CALL_PROC_INST_ID_ varchar(64)             null,
    ACT_NAME_          varchar(255)            null,
    ACT_TYPE_          varchar(255)            not null,
    ASSIGNEE_          varchar(255)            null,
    START_TIME_        datetime(3)             not null,
    END_TIME_          datetime(3)             null,
    TRANSACTION_ORDER_ int                     null,
    DURATION_          bigint                  null,
    DELETE_REASON_     varchar(4000)           null,
    TENANT_ID_         varchar(255) default '' null
)
    collate = utf8_bin;

create index ACT_IDX_HI_ACT_INST_END
    on ACT_HI_ACTINST (END_TIME_);

create index ACT_IDX_HI_ACT_INST_EXEC
    on ACT_HI_ACTINST (EXECUTION_ID_, ACT_ID_);

create index ACT_IDX_HI_ACT_INST_PROCINST
    on ACT_HI_ACTINST (PROC_INST_ID_, ACT_ID_);

create index ACT_IDX_HI_ACT_INST_START
    on ACT_HI_ACTINST (START_TIME_);

create table ACT_HI_ATTACHMENT
(
    ID_           varchar(64)   not null
        primary key,
    REV_          int           null,
    USER_ID_      varchar(255)  null,
    NAME_         varchar(255)  null,
    DESCRIPTION_  varchar(4000) null,
    TYPE_         varchar(255)  null,
    TASK_ID_      varchar(64)   null,
    PROC_INST_ID_ varchar(64)   null,
    URL_          varchar(4000) null,
    CONTENT_ID_   varchar(64)   null,
    TIME_         datetime(3)   null
)
    collate = utf8_bin;

create table ACT_HI_COMMENT
(
    ID_           varchar(64)   not null
        primary key,
    TYPE_         varchar(255)  null,
    TIME_         datetime(3)   not null,
    USER_ID_      varchar(255)  null,
    TASK_ID_      varchar(64)   null,
    PROC_INST_ID_ varchar(64)   null,
    ACTION_       varchar(255)  null,
    MESSAGE_      varchar(4000) null,
    FULL_MSG_     longblob      null
)
    collate = utf8_bin;

create table ACT_HI_DETAIL
(
    ID_           varchar(64)   not null
        primary key,
    TYPE_         varchar(255)  not null,
    PROC_INST_ID_ varchar(64)   null,
    EXECUTION_ID_ varchar(64)   null,
    TASK_ID_      varchar(64)   null,
    ACT_INST_ID_  varchar(64)   null,
    NAME_         varchar(255)  not null,
    VAR_TYPE_     varchar(255)  null,
    REV_          int           null,
    TIME_         datetime(3)   not null,
    BYTEARRAY_ID_ varchar(64)   null,
    DOUBLE_       double        null,
    LONG_         bigint        null,
    TEXT_         varchar(4000) null,
    TEXT2_        varchar(4000) null
)
    collate = utf8_bin;

create index ACT_IDX_HI_DETAIL_ACT_INST
    on ACT_HI_DETAIL (ACT_INST_ID_);

create index ACT_IDX_HI_DETAIL_NAME
    on ACT_HI_DETAIL (NAME_);

create index ACT_IDX_HI_DETAIL_PROC_INST
    on ACT_HI_DETAIL (PROC_INST_ID_);

create index ACT_IDX_HI_DETAIL_TASK_ID
    on ACT_HI_DETAIL (TASK_ID_);

create index ACT_IDX_HI_DETAIL_TIME
    on ACT_HI_DETAIL (TIME_);

create table ACT_HI_ENTITYLINK
(
    ID_                      varchar(64)  not null
        primary key,
    LINK_TYPE_               varchar(255) null,
    CREATE_TIME_             datetime(3)  null,
    SCOPE_ID_                varchar(255) null,
    SUB_SCOPE_ID_            varchar(255) null,
    SCOPE_TYPE_              varchar(255) null,
    SCOPE_DEFINITION_ID_     varchar(255) null,
    PARENT_ELEMENT_ID_       varchar(255) null,
    REF_SCOPE_ID_            varchar(255) null,
    REF_SCOPE_TYPE_          varchar(255) null,
    REF_SCOPE_DEFINITION_ID_ varchar(255) null,
    ROOT_SCOPE_ID_           varchar(255) null,
    ROOT_SCOPE_TYPE_         varchar(255) null,
    HIERARCHY_TYPE_          varchar(255) null
)
    collate = utf8_bin;

create index ACT_IDX_HI_ENT_LNK_REF_SCOPE
    on ACT_HI_ENTITYLINK (REF_SCOPE_ID_, REF_SCOPE_TYPE_, LINK_TYPE_);

create index ACT_IDX_HI_ENT_LNK_ROOT_SCOPE
    on ACT_HI_ENTITYLINK (ROOT_SCOPE_ID_, ROOT_SCOPE_TYPE_, LINK_TYPE_);

create index ACT_IDX_HI_ENT_LNK_SCOPE
    on ACT_HI_ENTITYLINK (SCOPE_ID_, SCOPE_TYPE_, LINK_TYPE_);

create index ACT_IDX_HI_ENT_LNK_SCOPE_DEF
    on ACT_HI_ENTITYLINK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_, LINK_TYPE_);

create table ACT_HI_IDENTITYLINK
(
    ID_                  varchar(64)  not null
        primary key,
    GROUP_ID_            varchar(255) null,
    TYPE_                varchar(255) null,
    USER_ID_             varchar(255) null,
    TASK_ID_             varchar(64)  null,
    CREATE_TIME_         datetime(3)  null,
    PROC_INST_ID_        varchar(64)  null,
    SCOPE_ID_            varchar(255) null,
    SUB_SCOPE_ID_        varchar(255) null,
    SCOPE_TYPE_          varchar(255) null,
    SCOPE_DEFINITION_ID_ varchar(255) null
)
    collate = utf8_bin;

create index ACT_IDX_HI_IDENT_LNK_PROCINST
    on ACT_HI_IDENTITYLINK (PROC_INST_ID_);

create index ACT_IDX_HI_IDENT_LNK_SCOPE
    on ACT_HI_IDENTITYLINK (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_HI_IDENT_LNK_SCOPE_DEF
    on ACT_HI_IDENTITYLINK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_HI_IDENT_LNK_SUB_SCOPE
    on ACT_HI_IDENTITYLINK (SUB_SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_HI_IDENT_LNK_TASK
    on ACT_HI_IDENTITYLINK (TASK_ID_);

create index ACT_IDX_HI_IDENT_LNK_USER
    on ACT_HI_IDENTITYLINK (USER_ID_);

create table ACT_HI_PROCINST
(
    ID_                        varchar(64)             not null
        primary key,
    REV_                       int          default 1  null,
    PROC_INST_ID_              varchar(64)             not null,
    BUSINESS_KEY_              varchar(255)            null,
    PROC_DEF_ID_               varchar(64)             not null,
    START_TIME_                datetime(3)             not null,
    END_TIME_                  datetime(3)             null,
    DURATION_                  bigint                  null,
    START_USER_ID_             varchar(255)            null,
    START_ACT_ID_              varchar(255)            null,
    END_ACT_ID_                varchar(255)            null,
    SUPER_PROCESS_INSTANCE_ID_ varchar(64)             null,
    DELETE_REASON_             varchar(4000)           null,
    TENANT_ID_                 varchar(255) default '' null,
    NAME_                      varchar(255)            null,
    CALLBACK_ID_               varchar(255)            null,
    CALLBACK_TYPE_             varchar(255)            null,
    REFERENCE_ID_              varchar(255)            null,
    REFERENCE_TYPE_            varchar(255)            null,
    PROPAGATED_STAGE_INST_ID_  varchar(255)            null,
    BUSINESS_STATUS_           varchar(255)            null,
    constraint PROC_INST_ID_
        unique (PROC_INST_ID_)
)
    collate = utf8_bin;

create index ACT_IDX_HI_PRO_INST_END
    on ACT_HI_PROCINST (END_TIME_);

create index ACT_IDX_HI_PRO_I_BUSKEY
    on ACT_HI_PROCINST (BUSINESS_KEY_);

create index ACT_IDX_HI_PRO_SUPER_PROCINST
    on ACT_HI_PROCINST (SUPER_PROCESS_INSTANCE_ID_);

create table ACT_HI_TASKINST
(
    ID_                       varchar(64)             not null
        primary key,
    REV_                      int          default 1  null,
    PROC_DEF_ID_              varchar(64)             null,
    TASK_DEF_ID_              varchar(64)             null,
    TASK_DEF_KEY_             varchar(255)            null,
    PROC_INST_ID_             varchar(64)             null,
    EXECUTION_ID_             varchar(64)             null,
    SCOPE_ID_                 varchar(255)            null,
    SUB_SCOPE_ID_             varchar(255)            null,
    SCOPE_TYPE_               varchar(255)            null,
    SCOPE_DEFINITION_ID_      varchar(255)            null,
    PROPAGATED_STAGE_INST_ID_ varchar(255)            null,
    NAME_                     varchar(255)            null,
    PARENT_TASK_ID_           varchar(64)             null,
    DESCRIPTION_              varchar(4000)           null,
    OWNER_                    varchar(255)            null,
    ASSIGNEE_                 varchar(255)            null,
    START_TIME_               datetime(3)             not null,
    CLAIM_TIME_               datetime(3)             null,
    END_TIME_                 datetime(3)             null,
    DURATION_                 bigint                  null,
    DELETE_REASON_            varchar(4000)           null,
    PRIORITY_                 int                     null,
    DUE_DATE_                 datetime(3)             null,
    FORM_KEY_                 varchar(255)            null,
    CATEGORY_                 varchar(255)            null,
    TENANT_ID_                varchar(255) default '' null,
    LAST_UPDATED_TIME_        datetime(3)             null
)
    collate = utf8_bin;

create index ACT_IDX_HI_TASK_INST_PROCINST
    on ACT_HI_TASKINST (PROC_INST_ID_);

create index ACT_IDX_HI_TASK_SCOPE
    on ACT_HI_TASKINST (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_HI_TASK_SCOPE_DEF
    on ACT_HI_TASKINST (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_HI_TASK_SUB_SCOPE
    on ACT_HI_TASKINST (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_HI_TSK_LOG
(
    ID_                  bigint auto_increment
        primary key,
    TYPE_                varchar(64)                               null,
    TASK_ID_             varchar(64)                               not null,
    TIME_STAMP_          timestamp(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3),
    USER_ID_             varchar(255)                              null,
    DATA_                varchar(4000)                             null,
    EXECUTION_ID_        varchar(64)                               null,
    PROC_INST_ID_        varchar(64)                               null,
    PROC_DEF_ID_         varchar(64)                               null,
    SCOPE_ID_            varchar(255)                              null,
    SCOPE_DEFINITION_ID_ varchar(255)                              null,
    SUB_SCOPE_ID_        varchar(255)                              null,
    SCOPE_TYPE_          varchar(255)                              null,
    TENANT_ID_           varchar(255) default ''                   null
)
    collate = utf8_bin;

create table ACT_HI_VARINST
(
    ID_                varchar(64)   not null
        primary key,
    REV_               int default 1 null,
    PROC_INST_ID_      varchar(64)   null,
    EXECUTION_ID_      varchar(64)   null,
    TASK_ID_           varchar(64)   null,
    NAME_              varchar(255)  not null,
    VAR_TYPE_          varchar(100)  null,
    SCOPE_ID_          varchar(255)  null,
    SUB_SCOPE_ID_      varchar(255)  null,
    SCOPE_TYPE_        varchar(255)  null,
    BYTEARRAY_ID_      varchar(64)   null,
    DOUBLE_            double        null,
    LONG_              bigint        null,
    TEXT_              varchar(4000) null,
    TEXT2_             varchar(4000) null,
    CREATE_TIME_       datetime(3)   null,
    LAST_UPDATED_TIME_ datetime(3)   null
)
    collate = utf8_bin;

create index ACT_IDX_HI_PROCVAR_EXE
    on ACT_HI_VARINST (EXECUTION_ID_);

create index ACT_IDX_HI_PROCVAR_NAME_TYPE
    on ACT_HI_VARINST (NAME_, VAR_TYPE_);

create index ACT_IDX_HI_PROCVAR_PROC_INST
    on ACT_HI_VARINST (PROC_INST_ID_);

create index ACT_IDX_HI_PROCVAR_TASK_ID
    on ACT_HI_VARINST (TASK_ID_);

create index ACT_IDX_HI_VAR_SCOPE_ID_TYPE
    on ACT_HI_VARINST (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_HI_VAR_SUB_ID_TYPE
    on ACT_HI_VARINST (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_ID_BYTEARRAY
(
    ID_    varchar(64)  not null
        primary key,
    REV_   int          null,
    NAME_  varchar(255) null,
    BYTES_ longblob     null
)
    collate = utf8_bin;

create table ACT_ID_GROUP
(
    ID_   varchar(64)  not null
        primary key,
    REV_  int          null,
    NAME_ varchar(255) null,
    TYPE_ varchar(255) null
)
    collate = utf8_bin;

create table ACT_ID_INFO
(
    ID_        varchar(64)  not null
        primary key,
    REV_       int          null,
    USER_ID_   varchar(64)  null,
    TYPE_      varchar(64)  null,
    KEY_       varchar(255) null,
    VALUE_     varchar(255) null,
    PASSWORD_  longblob     null,
    PARENT_ID_ varchar(255) null
)
    collate = utf8_bin;

create table ACT_ID_PRIV
(
    ID_   varchar(64)  not null
        primary key,
    NAME_ varchar(255) not null,
    constraint ACT_UNIQ_PRIV_NAME
        unique (NAME_)
)
    collate = utf8_bin;

create table ACT_ID_PRIV_MAPPING
(
    ID_       varchar(64)  not null
        primary key,
    PRIV_ID_  varchar(64)  not null,
    USER_ID_  varchar(255) null,
    GROUP_ID_ varchar(255) null,
    constraint ACT_FK_PRIV_MAPPING
        foreign key (PRIV_ID_) references ACT_ID_PRIV (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_PRIV_GROUP
    on ACT_ID_PRIV_MAPPING (GROUP_ID_);

create index ACT_IDX_PRIV_USER
    on ACT_ID_PRIV_MAPPING (USER_ID_);

create table ACT_ID_PROPERTY
(
    NAME_  varchar(64)  not null
        primary key,
    VALUE_ varchar(300) null,
    REV_   int          null
)
    collate = utf8_bin;

create table ACT_ID_TOKEN
(
    ID_          varchar(64)                               not null
        primary key,
    REV_         int                                       null,
    TOKEN_VALUE_ varchar(255)                              null,
    TOKEN_DATE_  timestamp(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3),
    IP_ADDRESS_  varchar(255)                              null,
    USER_AGENT_  varchar(255)                              null,
    USER_ID_     varchar(255)                              null,
    TOKEN_DATA_  varchar(2000)                             null
)
    collate = utf8_bin;

create table ACT_ID_USER
(
    ID_           varchar(64)             not null
        primary key,
    REV_          int                     null,
    FIRST_        varchar(255)            null,
    LAST_         varchar(255)            null,
    DISPLAY_NAME_ varchar(255)            null,
    EMAIL_        varchar(255)            null,
    PWD_          varchar(255)            null,
    PICTURE_ID_   varchar(64)             null,
    TENANT_ID_    varchar(255) default '' null
)
    collate = utf8_bin;

create table ACT_ID_MEMBERSHIP
(
    USER_ID_  varchar(64) not null,
    GROUP_ID_ varchar(64) not null,
    primary key (USER_ID_, GROUP_ID_),
    constraint ACT_FK_MEMB_GROUP
        foreign key (GROUP_ID_) references ACT_ID_GROUP (ID_),
    constraint ACT_FK_MEMB_USER
        foreign key (USER_ID_) references ACT_ID_USER (ID_)
)
    collate = utf8_bin;

create table ACT_RE_DEPLOYMENT
(
    ID_                   varchar(64)             not null
        primary key,
    NAME_                 varchar(255)            null,
    CATEGORY_             varchar(255)            null,
    KEY_                  varchar(255)            null,
    TENANT_ID_            varchar(255) default '' null,
    DEPLOY_TIME_          timestamp(3)            null,
    DERIVED_FROM_         varchar(64)             null,
    DERIVED_FROM_ROOT_    varchar(64)             null,
    PARENT_DEPLOYMENT_ID_ varchar(255)            null,
    ENGINE_VERSION_       varchar(255)            null
)
    collate = utf8_bin;

create table ACT_GE_BYTEARRAY
(
    ID_            varchar(64)  not null
        primary key,
    REV_           int          null,
    NAME_          varchar(255) null,
    DEPLOYMENT_ID_ varchar(64)  null,
    BYTES_         longblob     null,
    GENERATED_     tinyint      null,
    constraint ACT_FK_BYTEARR_DEPL
        foreign key (DEPLOYMENT_ID_) references ACT_RE_DEPLOYMENT (ID_)
)
    collate = utf8_bin;

create table ACT_RE_MODEL
(
    ID_                           varchar(64)             not null
        primary key,
    REV_                          int                     null,
    NAME_                         varchar(255)            null,
    KEY_                          varchar(255)            null,
    CATEGORY_                     varchar(255)            null,
    CREATE_TIME_                  timestamp(3)            null,
    LAST_UPDATE_TIME_             timestamp(3)            null,
    VERSION_                      int                     null,
    META_INFO_                    varchar(4000)           null,
    DEPLOYMENT_ID_                varchar(64)             null,
    EDITOR_SOURCE_VALUE_ID_       varchar(64)             null,
    EDITOR_SOURCE_EXTRA_VALUE_ID_ varchar(64)             null,
    TENANT_ID_                    varchar(255) default '' null,
    constraint ACT_FK_MODEL_DEPLOYMENT
        foreign key (DEPLOYMENT_ID_) references ACT_RE_DEPLOYMENT (ID_),
    constraint ACT_FK_MODEL_SOURCE
        foreign key (EDITOR_SOURCE_VALUE_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_MODEL_SOURCE_EXTRA
        foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_) references ACT_GE_BYTEARRAY (ID_)
)
    collate = utf8_bin;

create table ACT_RE_PROCDEF
(
    ID_                     varchar(64)             not null
        primary key,
    REV_                    int                     null,
    CATEGORY_               varchar(255)            null,
    NAME_                   varchar(255)            null,
    KEY_                    varchar(255)            not null,
    VERSION_                int                     not null,
    DEPLOYMENT_ID_          varchar(64)             null,
    RESOURCE_NAME_          varchar(4000)           null,
    DGRM_RESOURCE_NAME_     varchar(4000)           null,
    DESCRIPTION_            varchar(4000)           null,
    HAS_START_FORM_KEY_     tinyint                 null,
    HAS_GRAPHICAL_NOTATION_ tinyint                 null,
    SUSPENSION_STATE_       int                     null,
    TENANT_ID_              varchar(255) default '' null,
    ENGINE_VERSION_         varchar(255)            null,
    DERIVED_FROM_           varchar(64)             null,
    DERIVED_FROM_ROOT_      varchar(64)             null,
    DERIVED_VERSION_        int          default 0  not null,
    constraint ACT_UNIQ_PROCDEF
        unique (KEY_, VERSION_, DERIVED_VERSION_, TENANT_ID_)
)
    collate = utf8_bin;

create table ACT_PROCDEF_INFO
(
    ID_           varchar(64) not null
        primary key,
    PROC_DEF_ID_  varchar(64) not null,
    REV_          int         null,
    INFO_JSON_ID_ varchar(64) null,
    constraint ACT_UNIQ_INFO_PROCDEF
        unique (PROC_DEF_ID_),
    constraint ACT_FK_INFO_JSON_BA
        foreign key (INFO_JSON_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_INFO_PROCDEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_INFO_PROCDEF
    on ACT_PROCDEF_INFO (PROC_DEF_ID_);

create table ACT_RU_ACTINST
(
    ID_                varchar(64)             not null
        primary key,
    REV_               int          default 1  null,
    PROC_DEF_ID_       varchar(64)             not null,
    PROC_INST_ID_      varchar(64)             not null,
    EXECUTION_ID_      varchar(64)             not null,
    ACT_ID_            varchar(255)            not null,
    TASK_ID_           varchar(64)             null,
    CALL_PROC_INST_ID_ varchar(64)             null,
    ACT_NAME_          varchar(255)            null,
    ACT_TYPE_          varchar(255)            not null,
    ASSIGNEE_          varchar(255)            null,
    START_TIME_        datetime(3)             not null,
    END_TIME_          datetime(3)             null,
    DURATION_          bigint                  null,
    TRANSACTION_ORDER_ int                     null,
    DELETE_REASON_     varchar(4000)           null,
    TENANT_ID_         varchar(255) default '' null
)
    collate = utf8_bin;

create index ACT_IDX_RU_ACTI_END
    on ACT_RU_ACTINST (END_TIME_);

create index ACT_IDX_RU_ACTI_EXEC
    on ACT_RU_ACTINST (EXECUTION_ID_);

create index ACT_IDX_RU_ACTI_EXEC_ACT
    on ACT_RU_ACTINST (EXECUTION_ID_, ACT_ID_);

create index ACT_IDX_RU_ACTI_PROC
    on ACT_RU_ACTINST (PROC_INST_ID_);

create index ACT_IDX_RU_ACTI_PROC_ACT
    on ACT_RU_ACTINST (PROC_INST_ID_, ACT_ID_);

create index ACT_IDX_RU_ACTI_START
    on ACT_RU_ACTINST (START_TIME_);

create index ACT_IDX_RU_ACTI_TASK
    on ACT_RU_ACTINST (TASK_ID_);

create table ACT_RU_ENTITYLINK
(
    ID_                      varchar(64)  not null
        primary key,
    REV_                     int          null,
    CREATE_TIME_             datetime(3)  null,
    LINK_TYPE_               varchar(255) null,
    SCOPE_ID_                varchar(255) null,
    SUB_SCOPE_ID_            varchar(255) null,
    SCOPE_TYPE_              varchar(255) null,
    SCOPE_DEFINITION_ID_     varchar(255) null,
    PARENT_ELEMENT_ID_       varchar(255) null,
    REF_SCOPE_ID_            varchar(255) null,
    REF_SCOPE_TYPE_          varchar(255) null,
    REF_SCOPE_DEFINITION_ID_ varchar(255) null,
    ROOT_SCOPE_ID_           varchar(255) null,
    ROOT_SCOPE_TYPE_         varchar(255) null,
    HIERARCHY_TYPE_          varchar(255) null
)
    collate = utf8_bin;

create index ACT_IDX_ENT_LNK_REF_SCOPE
    on ACT_RU_ENTITYLINK (REF_SCOPE_ID_, REF_SCOPE_TYPE_, LINK_TYPE_);

create index ACT_IDX_ENT_LNK_ROOT_SCOPE
    on ACT_RU_ENTITYLINK (ROOT_SCOPE_ID_, ROOT_SCOPE_TYPE_, LINK_TYPE_);

create index ACT_IDX_ENT_LNK_SCOPE
    on ACT_RU_ENTITYLINK (SCOPE_ID_, SCOPE_TYPE_, LINK_TYPE_);

create index ACT_IDX_ENT_LNK_SCOPE_DEF
    on ACT_RU_ENTITYLINK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_, LINK_TYPE_);

create table ACT_RU_EXECUTION
(
    ID_                        varchar(64)             not null
        primary key,
    REV_                       int                     null,
    PROC_INST_ID_              varchar(64)             null,
    BUSINESS_KEY_              varchar(255)            null,
    PARENT_ID_                 varchar(64)             null,
    PROC_DEF_ID_               varchar(64)             null,
    SUPER_EXEC_                varchar(64)             null,
    ROOT_PROC_INST_ID_         varchar(64)             null,
    ACT_ID_                    varchar(255)            null,
    IS_ACTIVE_                 tinyint                 null,
    IS_CONCURRENT_             tinyint                 null,
    IS_SCOPE_                  tinyint                 null,
    IS_EVENT_SCOPE_            tinyint                 null,
    IS_MI_ROOT_                tinyint                 null,
    SUSPENSION_STATE_          int                     null,
    CACHED_ENT_STATE_          int                     null,
    TENANT_ID_                 varchar(255) default '' null,
    NAME_                      varchar(255)            null,
    START_ACT_ID_              varchar(255)            null,
    START_TIME_                datetime(3)             null,
    START_USER_ID_             varchar(255)            null,
    LOCK_TIME_                 timestamp(3)            null,
    LOCK_OWNER_                varchar(255)            null,
    IS_COUNT_ENABLED_          tinyint                 null,
    EVT_SUBSCR_COUNT_          int                     null,
    TASK_COUNT_                int                     null,
    JOB_COUNT_                 int                     null,
    TIMER_JOB_COUNT_           int                     null,
    SUSP_JOB_COUNT_            int                     null,
    DEADLETTER_JOB_COUNT_      int                     null,
    EXTERNAL_WORKER_JOB_COUNT_ int                     null,
    VAR_COUNT_                 int                     null,
    ID_LINK_COUNT_             int                     null,
    CALLBACK_ID_               varchar(255)            null,
    CALLBACK_TYPE_             varchar(255)            null,
    REFERENCE_ID_              varchar(255)            null,
    REFERENCE_TYPE_            varchar(255)            null,
    PROPAGATED_STAGE_INST_ID_  varchar(255)            null,
    BUSINESS_STATUS_           varchar(255)            null,
    constraint ACT_FK_EXE_PARENT
        foreign key (PARENT_ID_) references ACT_RU_EXECUTION (ID_)
            on delete cascade,
    constraint ACT_FK_EXE_PROCDEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_),
    constraint ACT_FK_EXE_PROCINST
        foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION (ID_)
            on update cascade on delete cascade,
    constraint ACT_FK_EXE_SUPER
        foreign key (SUPER_EXEC_) references ACT_RU_EXECUTION (ID_)
            on delete cascade
)
    collate = utf8_bin;

create table ACT_RU_DEADLETTER_JOB
(
    ID_                  varchar(64)             not null
        primary key,
    REV_                 int                     null,
    CATEGORY_            varchar(255)            null,
    TYPE_                varchar(255)            not null,
    EXCLUSIVE_           tinyint(1)              null,
    EXECUTION_ID_        varchar(64)             null,
    PROCESS_INSTANCE_ID_ varchar(64)             null,
    PROC_DEF_ID_         varchar(64)             null,
    ELEMENT_ID_          varchar(255)            null,
    ELEMENT_NAME_        varchar(255)            null,
    SCOPE_ID_            varchar(255)            null,
    SUB_SCOPE_ID_        varchar(255)            null,
    SCOPE_TYPE_          varchar(255)            null,
    SCOPE_DEFINITION_ID_ varchar(255)            null,
    CORRELATION_ID_      varchar(255)            null,
    EXCEPTION_STACK_ID_  varchar(64)             null,
    EXCEPTION_MSG_       varchar(4000)           null,
    DUEDATE_             timestamp(3)            null,
    REPEAT_              varchar(255)            null,
    HANDLER_TYPE_        varchar(255)            null,
    HANDLER_CFG_         varchar(4000)           null,
    CUSTOM_VALUES_ID_    varchar(64)             null,
    CREATE_TIME_         timestamp(3)            null,
    TENANT_ID_           varchar(255) default '' null,
    constraint ACT_FK_DEADLETTER_JOB_CUSTOM_VALUES
        foreign key (CUSTOM_VALUES_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_DEADLETTER_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_DEADLETTER_JOB_EXECUTION
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_DEADLETTER_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_DEADLETTER_JOB_CORRELATION_ID
    on ACT_RU_DEADLETTER_JOB (CORRELATION_ID_);

create index ACT_IDX_DEADLETTER_JOB_CUSTOM_VALUES_ID
    on ACT_RU_DEADLETTER_JOB (CUSTOM_VALUES_ID_);

create index ACT_IDX_DEADLETTER_JOB_EXCEPTION_STACK_ID
    on ACT_RU_DEADLETTER_JOB (EXCEPTION_STACK_ID_);

create index ACT_IDX_DJOB_SCOPE
    on ACT_RU_DEADLETTER_JOB (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_DJOB_SCOPE_DEF
    on ACT_RU_DEADLETTER_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_DJOB_SUB_SCOPE
    on ACT_RU_DEADLETTER_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_RU_EVENT_SUBSCR
(
    ID_                  varchar(64)                               not null
        primary key,
    REV_                 int                                       null,
    EVENT_TYPE_          varchar(255)                              not null,
    EVENT_NAME_          varchar(255)                              null,
    EXECUTION_ID_        varchar(64)                               null,
    PROC_INST_ID_        varchar(64)                               null,
    ACTIVITY_ID_         varchar(64)                               null,
    CONFIGURATION_       varchar(255)                              null,
    CREATED_             timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    PROC_DEF_ID_         varchar(64)                               null,
    SUB_SCOPE_ID_        varchar(64)                               null,
    SCOPE_ID_            varchar(64)                               null,
    SCOPE_DEFINITION_ID_ varchar(64)                               null,
    SCOPE_TYPE_          varchar(64)                               null,
    LOCK_TIME_           timestamp(3)                              null,
    LOCK_OWNER_          varchar(255)                              null,
    TENANT_ID_           varchar(255) default ''                   null,
    constraint ACT_FK_EVENT_EXEC
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_EVENT_SUBSCR_CONFIG_
    on ACT_RU_EVENT_SUBSCR (CONFIGURATION_);

create index ACT_IDX_EVENT_SUBSCR_SCOPEREF_
    on ACT_RU_EVENT_SUBSCR (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDC_EXEC_ROOT
    on ACT_RU_EXECUTION (ROOT_PROC_INST_ID_);

create index ACT_IDX_EXEC_BUSKEY
    on ACT_RU_EXECUTION (BUSINESS_KEY_);

create index ACT_IDX_EXEC_REF_ID_
    on ACT_RU_EXECUTION (REFERENCE_ID_);

create table ACT_RU_EXTERNAL_JOB
(
    ID_                  varchar(64)             not null
        primary key,
    REV_                 int                     null,
    CATEGORY_            varchar(255)            null,
    TYPE_                varchar(255)            not null,
    LOCK_EXP_TIME_       timestamp(3)            null,
    LOCK_OWNER_          varchar(255)            null,
    EXCLUSIVE_           tinyint(1)              null,
    EXECUTION_ID_        varchar(64)             null,
    PROCESS_INSTANCE_ID_ varchar(64)             null,
    PROC_DEF_ID_         varchar(64)             null,
    ELEMENT_ID_          varchar(255)            null,
    ELEMENT_NAME_        varchar(255)            null,
    SCOPE_ID_            varchar(255)            null,
    SUB_SCOPE_ID_        varchar(255)            null,
    SCOPE_TYPE_          varchar(255)            null,
    SCOPE_DEFINITION_ID_ varchar(255)            null,
    CORRELATION_ID_      varchar(255)            null,
    RETRIES_             int                     null,
    EXCEPTION_STACK_ID_  varchar(64)             null,
    EXCEPTION_MSG_       varchar(4000)           null,
    DUEDATE_             timestamp(3)            null,
    REPEAT_              varchar(255)            null,
    HANDLER_TYPE_        varchar(255)            null,
    HANDLER_CFG_         varchar(4000)           null,
    CUSTOM_VALUES_ID_    varchar(64)             null,
    CREATE_TIME_         timestamp(3)            null,
    TENANT_ID_           varchar(255) default '' null,
    constraint ACT_FK_EXTERNAL_JOB_CUSTOM_VALUES
        foreign key (CUSTOM_VALUES_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_EXTERNAL_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_) references ACT_GE_BYTEARRAY (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_EJOB_SCOPE
    on ACT_RU_EXTERNAL_JOB (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_EJOB_SCOPE_DEF
    on ACT_RU_EXTERNAL_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_EJOB_SUB_SCOPE
    on ACT_RU_EXTERNAL_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_EXTERNAL_JOB_CORRELATION_ID
    on ACT_RU_EXTERNAL_JOB (CORRELATION_ID_);

create index ACT_IDX_EXTERNAL_JOB_CUSTOM_VALUES_ID
    on ACT_RU_EXTERNAL_JOB (CUSTOM_VALUES_ID_);

create index ACT_IDX_EXTERNAL_JOB_EXCEPTION_STACK_ID
    on ACT_RU_EXTERNAL_JOB (EXCEPTION_STACK_ID_);

create table ACT_RU_HISTORY_JOB
(
    ID_                 varchar(64)             not null
        primary key,
    REV_                int                     null,
    LOCK_EXP_TIME_      timestamp(3)            null,
    LOCK_OWNER_         varchar(255)            null,
    RETRIES_            int                     null,
    EXCEPTION_STACK_ID_ varchar(64)             null,
    EXCEPTION_MSG_      varchar(4000)           null,
    HANDLER_TYPE_       varchar(255)            null,
    HANDLER_CFG_        varchar(4000)           null,
    CUSTOM_VALUES_ID_   varchar(64)             null,
    ADV_HANDLER_CFG_ID_ varchar(64)             null,
    CREATE_TIME_        timestamp(3)            null,
    SCOPE_TYPE_         varchar(255)            null,
    TENANT_ID_          varchar(255) default '' null
)
    collate = utf8_bin;

create table ACT_RU_JOB
(
    ID_                  varchar(64)             not null
        primary key,
    REV_                 int                     null,
    CATEGORY_            varchar(255)            null,
    TYPE_                varchar(255)            not null,
    LOCK_EXP_TIME_       timestamp(3)            null,
    LOCK_OWNER_          varchar(255)            null,
    EXCLUSIVE_           tinyint(1)              null,
    EXECUTION_ID_        varchar(64)             null,
    PROCESS_INSTANCE_ID_ varchar(64)             null,
    PROC_DEF_ID_         varchar(64)             null,
    ELEMENT_ID_          varchar(255)            null,
    ELEMENT_NAME_        varchar(255)            null,
    SCOPE_ID_            varchar(255)            null,
    SUB_SCOPE_ID_        varchar(255)            null,
    SCOPE_TYPE_          varchar(255)            null,
    SCOPE_DEFINITION_ID_ varchar(255)            null,
    CORRELATION_ID_      varchar(255)            null,
    RETRIES_             int                     null,
    EXCEPTION_STACK_ID_  varchar(64)             null,
    EXCEPTION_MSG_       varchar(4000)           null,
    DUEDATE_             timestamp(3)            null,
    REPEAT_              varchar(255)            null,
    HANDLER_TYPE_        varchar(255)            null,
    HANDLER_CFG_         varchar(4000)           null,
    CUSTOM_VALUES_ID_    varchar(64)             null,
    CREATE_TIME_         timestamp(3)            null,
    TENANT_ID_           varchar(255) default '' null,
    constraint ACT_FK_JOB_CUSTOM_VALUES
        foreign key (CUSTOM_VALUES_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_JOB_EXECUTION
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_JOB_CORRELATION_ID
    on ACT_RU_JOB (CORRELATION_ID_);

create index ACT_IDX_JOB_CUSTOM_VALUES_ID
    on ACT_RU_JOB (CUSTOM_VALUES_ID_);

create index ACT_IDX_JOB_EXCEPTION_STACK_ID
    on ACT_RU_JOB (EXCEPTION_STACK_ID_);

create index ACT_IDX_JOB_SCOPE
    on ACT_RU_JOB (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_JOB_SCOPE_DEF
    on ACT_RU_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_JOB_SUB_SCOPE
    on ACT_RU_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_RU_SUSPENDED_JOB
(
    ID_                  varchar(64)             not null
        primary key,
    REV_                 int                     null,
    CATEGORY_            varchar(255)            null,
    TYPE_                varchar(255)            not null,
    EXCLUSIVE_           tinyint(1)              null,
    EXECUTION_ID_        varchar(64)             null,
    PROCESS_INSTANCE_ID_ varchar(64)             null,
    PROC_DEF_ID_         varchar(64)             null,
    ELEMENT_ID_          varchar(255)            null,
    ELEMENT_NAME_        varchar(255)            null,
    SCOPE_ID_            varchar(255)            null,
    SUB_SCOPE_ID_        varchar(255)            null,
    SCOPE_TYPE_          varchar(255)            null,
    SCOPE_DEFINITION_ID_ varchar(255)            null,
    CORRELATION_ID_      varchar(255)            null,
    RETRIES_             int                     null,
    EXCEPTION_STACK_ID_  varchar(64)             null,
    EXCEPTION_MSG_       varchar(4000)           null,
    DUEDATE_             timestamp(3)            null,
    REPEAT_              varchar(255)            null,
    HANDLER_TYPE_        varchar(255)            null,
    HANDLER_CFG_         varchar(4000)           null,
    CUSTOM_VALUES_ID_    varchar(64)             null,
    CREATE_TIME_         timestamp(3)            null,
    TENANT_ID_           varchar(255) default '' null,
    constraint ACT_FK_SUSPENDED_JOB_CUSTOM_VALUES
        foreign key (CUSTOM_VALUES_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_SUSPENDED_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_SUSPENDED_JOB_EXECUTION
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_SUSPENDED_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_SJOB_SCOPE
    on ACT_RU_SUSPENDED_JOB (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_SJOB_SCOPE_DEF
    on ACT_RU_SUSPENDED_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_SJOB_SUB_SCOPE
    on ACT_RU_SUSPENDED_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_SUSPENDED_JOB_CORRELATION_ID
    on ACT_RU_SUSPENDED_JOB (CORRELATION_ID_);

create index ACT_IDX_SUSPENDED_JOB_CUSTOM_VALUES_ID
    on ACT_RU_SUSPENDED_JOB (CUSTOM_VALUES_ID_);

create index ACT_IDX_SUSPENDED_JOB_EXCEPTION_STACK_ID
    on ACT_RU_SUSPENDED_JOB (EXCEPTION_STACK_ID_);

create table ACT_RU_TASK
(
    ID_                       varchar(64)             not null
        primary key,
    REV_                      int                     null,
    EXECUTION_ID_             varchar(64)             null,
    PROC_INST_ID_             varchar(64)             null,
    PROC_DEF_ID_              varchar(64)             null,
    TASK_DEF_ID_              varchar(64)             null,
    SCOPE_ID_                 varchar(255)            null,
    SUB_SCOPE_ID_             varchar(255)            null,
    SCOPE_TYPE_               varchar(255)            null,
    SCOPE_DEFINITION_ID_      varchar(255)            null,
    PROPAGATED_STAGE_INST_ID_ varchar(255)            null,
    NAME_                     varchar(255)            null,
    PARENT_TASK_ID_           varchar(64)             null,
    DESCRIPTION_              varchar(4000)           null,
    TASK_DEF_KEY_             varchar(255)            null,
    OWNER_                    varchar(255)            null,
    ASSIGNEE_                 varchar(255)            null,
    DELEGATION_               varchar(64)             null,
    PRIORITY_                 int                     null,
    CREATE_TIME_              timestamp(3)            null,
    DUE_DATE_                 datetime(3)             null,
    CATEGORY_                 varchar(255)            null,
    SUSPENSION_STATE_         int                     null,
    TENANT_ID_                varchar(255) default '' null,
    FORM_KEY_                 varchar(255)            null,
    CLAIM_TIME_               datetime(3)             null,
    IS_COUNT_ENABLED_         tinyint                 null,
    VAR_COUNT_                int                     null,
    ID_LINK_COUNT_            int                     null,
    SUB_TASK_COUNT_           int                     null,
    constraint ACT_FK_TASK_EXE
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_TASK_PROCDEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_),
    constraint ACT_FK_TASK_PROCINST
        foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION (ID_)
)
    collate = utf8_bin;

create table ACT_RU_IDENTITYLINK
(
    ID_                  varchar(64)  not null
        primary key,
    REV_                 int          null,
    GROUP_ID_            varchar(255) null,
    TYPE_                varchar(255) null,
    USER_ID_             varchar(255) null,
    TASK_ID_             varchar(64)  null,
    PROC_INST_ID_        varchar(64)  null,
    PROC_DEF_ID_         varchar(64)  null,
    SCOPE_ID_            varchar(255) null,
    SUB_SCOPE_ID_        varchar(255) null,
    SCOPE_TYPE_          varchar(255) null,
    SCOPE_DEFINITION_ID_ varchar(255) null,
    constraint ACT_FK_ATHRZ_PROCEDEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_),
    constraint ACT_FK_IDL_PROCINST
        foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_TSKASS_TASK
        foreign key (TASK_ID_) references ACT_RU_TASK (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_ATHRZ_PROCEDEF
    on ACT_RU_IDENTITYLINK (PROC_DEF_ID_);

create index ACT_IDX_IDENT_LNK_GROUP
    on ACT_RU_IDENTITYLINK (GROUP_ID_);

create index ACT_IDX_IDENT_LNK_SCOPE
    on ACT_RU_IDENTITYLINK (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_IDENT_LNK_SCOPE_DEF
    on ACT_RU_IDENTITYLINK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_IDENT_LNK_SUB_SCOPE
    on ACT_RU_IDENTITYLINK (SUB_SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_IDENT_LNK_USER
    on ACT_RU_IDENTITYLINK (USER_ID_);

create index ACT_IDX_TASK_CREATE
    on ACT_RU_TASK (CREATE_TIME_);

create index ACT_IDX_TASK_SCOPE
    on ACT_RU_TASK (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_TASK_SCOPE_DEF
    on ACT_RU_TASK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_TASK_SUB_SCOPE
    on ACT_RU_TASK (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_RU_TIMER_JOB
(
    ID_                  varchar(64)             not null
        primary key,
    REV_                 int                     null,
    CATEGORY_            varchar(255)            null,
    TYPE_                varchar(255)            not null,
    LOCK_EXP_TIME_       timestamp(3)            null,
    LOCK_OWNER_          varchar(255)            null,
    EXCLUSIVE_           tinyint(1)              null,
    EXECUTION_ID_        varchar(64)             null,
    PROCESS_INSTANCE_ID_ varchar(64)             null,
    PROC_DEF_ID_         varchar(64)             null,
    ELEMENT_ID_          varchar(255)            null,
    ELEMENT_NAME_        varchar(255)            null,
    SCOPE_ID_            varchar(255)            null,
    SUB_SCOPE_ID_        varchar(255)            null,
    SCOPE_TYPE_          varchar(255)            null,
    SCOPE_DEFINITION_ID_ varchar(255)            null,
    CORRELATION_ID_      varchar(255)            null,
    RETRIES_             int                     null,
    EXCEPTION_STACK_ID_  varchar(64)             null,
    EXCEPTION_MSG_       varchar(4000)           null,
    DUEDATE_             timestamp(3)            null,
    REPEAT_              varchar(255)            null,
    HANDLER_TYPE_        varchar(255)            null,
    HANDLER_CFG_         varchar(4000)           null,
    CUSTOM_VALUES_ID_    varchar(64)             null,
    CREATE_TIME_         timestamp(3)            null,
    TENANT_ID_           varchar(255) default '' null,
    constraint ACT_FK_TIMER_JOB_CUSTOM_VALUES
        foreign key (CUSTOM_VALUES_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_TIMER_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_TIMER_JOB_EXECUTION
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_TIMER_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_TIMER_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_TIMER_JOB_CORRELATION_ID
    on ACT_RU_TIMER_JOB (CORRELATION_ID_);

create index ACT_IDX_TIMER_JOB_CUSTOM_VALUES_ID
    on ACT_RU_TIMER_JOB (CUSTOM_VALUES_ID_);

create index ACT_IDX_TIMER_JOB_DUEDATE
    on ACT_RU_TIMER_JOB (DUEDATE_);

create index ACT_IDX_TIMER_JOB_EXCEPTION_STACK_ID
    on ACT_RU_TIMER_JOB (EXCEPTION_STACK_ID_);

create index ACT_IDX_TJOB_SCOPE
    on ACT_RU_TIMER_JOB (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_TJOB_SCOPE_DEF
    on ACT_RU_TIMER_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);

create index ACT_IDX_TJOB_SUB_SCOPE
    on ACT_RU_TIMER_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_RU_VARIABLE
(
    ID_           varchar(64)   not null
        primary key,
    REV_          int           null,
    TYPE_         varchar(255)  not null,
    NAME_         varchar(255)  not null,
    EXECUTION_ID_ varchar(64)   null,
    PROC_INST_ID_ varchar(64)   null,
    TASK_ID_      varchar(64)   null,
    SCOPE_ID_     varchar(255)  null,
    SUB_SCOPE_ID_ varchar(255)  null,
    SCOPE_TYPE_   varchar(255)  null,
    BYTEARRAY_ID_ varchar(64)   null,
    DOUBLE_       double        null,
    LONG_         bigint        null,
    TEXT_         varchar(4000) null,
    TEXT2_        varchar(4000) null,
    constraint ACT_FK_VAR_BYTEARRAY
        foreign key (BYTEARRAY_ID_) references ACT_GE_BYTEARRAY (ID_),
    constraint ACT_FK_VAR_EXE
        foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_),
    constraint ACT_FK_VAR_PROCINST
        foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION (ID_)
)
    collate = utf8_bin;

create index ACT_IDX_RU_VAR_SCOPE_ID_TYPE
    on ACT_RU_VARIABLE (SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_RU_VAR_SUB_ID_TYPE
    on ACT_RU_VARIABLE (SUB_SCOPE_ID_, SCOPE_TYPE_);

create index ACT_IDX_VARIABLE_TASK_ID
    on ACT_RU_VARIABLE (TASK_ID_);

create table FLW_CHANNEL_DEFINITION
(
    ID_             varchar(255) not null
        primary key,
    NAME_           varchar(255) null,
    VERSION_        int          null,
    KEY_            varchar(255) null,
    CATEGORY_       varchar(255) null,
    DEPLOYMENT_ID_  varchar(255) null,
    CREATE_TIME_    datetime(3)  null,
    TENANT_ID_      varchar(255) null,
    RESOURCE_NAME_  varchar(255) null,
    DESCRIPTION_    varchar(255) null,
    TYPE_           varchar(255) null,
    IMPLEMENTATION_ varchar(255) null,
    constraint ACT_IDX_CHANNEL_DEF_UNIQ
        unique (KEY_, VERSION_, TENANT_ID_)
);

create table FLW_EVENT_DEFINITION
(
    ID_            varchar(255) not null
        primary key,
    NAME_          varchar(255) null,
    VERSION_       int          null,
    KEY_           varchar(255) null,
    CATEGORY_      varchar(255) null,
    DEPLOYMENT_ID_ varchar(255) null,
    TENANT_ID_     varchar(255) null,
    RESOURCE_NAME_ varchar(255) null,
    DESCRIPTION_   varchar(255) null,
    constraint ACT_IDX_EVENT_DEF_UNIQ
        unique (KEY_, VERSION_, TENANT_ID_)
);

create table FLW_EVENT_DEPLOYMENT
(
    ID_                   varchar(255) not null
        primary key,
    NAME_                 varchar(255) null,
    CATEGORY_             varchar(255) null,
    DEPLOY_TIME_          datetime(3)  null,
    TENANT_ID_            varchar(255) null,
    PARENT_DEPLOYMENT_ID_ varchar(255) null
);

create table FLW_EVENT_RESOURCE
(
    ID_             varchar(255) not null
        primary key,
    NAME_           varchar(255) null,
    DEPLOYMENT_ID_  varchar(255) null,
    RESOURCE_BYTES_ longblob     null
);

create table FLW_EV_DATABASECHANGELOG
(
    ID            varchar(255) not null,
    AUTHOR        varchar(255) not null,
    FILENAME      varchar(255) not null,
    DATEEXECUTED  datetime     not null,
    ORDEREXECUTED int          not null,
    EXECTYPE      varchar(10)  not null,
    MD5SUM        varchar(35)  null,
    DESCRIPTION   varchar(255) null,
    COMMENTS      varchar(255) null,
    TAG           varchar(255) null,
    LIQUIBASE     varchar(20)  null,
    CONTEXTS      varchar(255) null,
    LABELS        varchar(255) null,
    DEPLOYMENT_ID varchar(10)  null
);

create table FLW_EV_DATABASECHANGELOGLOCK
(
    ID          int          not null
        primary key,
    LOCKED      bit          not null,
    LOCKGRANTED datetime     null,
    LOCKEDBY    varchar(255) null
);

create table FLW_RU_BATCH
(
    ID_            varchar(64)             not null
        primary key,
    REV_           int                     null,
    TYPE_          varchar(64)             not null,
    SEARCH_KEY_    varchar(255)            null,
    SEARCH_KEY2_   varchar(255)            null,
    CREATE_TIME_   datetime(3)             not null,
    COMPLETE_TIME_ datetime(3)             null,
    STATUS_        varchar(255)            null,
    BATCH_DOC_ID_  varchar(64)             null,
    TENANT_ID_     varchar(255) default '' null
)
    collate = utf8_bin;

create table FLW_RU_BATCH_PART
(
    ID_            varchar(64)             not null
        primary key,
    REV_           int                     null,
    BATCH_ID_      varchar(64)             null,
    TYPE_          varchar(64)             not null,
    SCOPE_ID_      varchar(64)             null,
    SUB_SCOPE_ID_  varchar(64)             null,
    SCOPE_TYPE_    varchar(64)             null,
    SEARCH_KEY_    varchar(255)            null,
    SEARCH_KEY2_   varchar(255)            null,
    CREATE_TIME_   datetime(3)             not null,
    COMPLETE_TIME_ datetime(3)             null,
    STATUS_        varchar(255)            null,
    RESULT_DOC_ID_ varchar(64)             null,
    TENANT_ID_     varchar(255) default '' null,
    constraint FLW_FK_BATCH_PART_PARENT
        foreign key (BATCH_ID_) references FLW_RU_BATCH (ID_)
)
    collate = utf8_bin;

create index FLW_IDX_BATCH_PART
    on FLW_RU_BATCH_PART (BATCH_ID_);

create table bpm_category
(
    id          bigint auto_increment comment '分类编号'
        primary key,
    name        varchar(30)  default ''                null comment '分类名',
    code        varchar(30)  default ''                null comment '分类标志',
    description varchar(255) default ''                not null comment '分类描述',
    status      tinyint                                null comment '分类状态',
    sort        int                                    null comment '分类排序',
    create_by   varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit          default b'0'              not null comment '是否删除'
)
    comment 'BPM 流程分类' collate = utf8mb4_unicode_ci;

create table bpm_form
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(64)                           not null comment '表单名',
    status      tinyint                               not null comment '开启状态',
    conf        varchar(1000)                         not null comment '表单的配置',
    fields      varchar(5000)                         not null comment '表单项的数组',
    remark      varchar(255)                          null comment '备注',
    create_by   varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit         default b'0'              not null comment '是否删除'
)
    comment 'BPM 表单定义表' collate = utf8mb4_unicode_ci;

create table bpm_oa_leave
(
    id                  bigint auto_increment comment '请假表单主键'
        primary key,
    user_id             bigint                                not null comment '申请人的用户编号',
    type                tinyint                               not null comment '请假类型',
    reason              varchar(200)                          not null comment '请假原因',
    start_time          datetime                              not null comment '开始时间',
    end_time            datetime                              not null comment '结束时间',
    day                 tinyint                               not null comment '请假天数',
    status              tinyint                               not null comment '审批结果',
    process_instance_id varchar(64)                           null comment '流程实例的编号',
    create_by           varchar(64) default ''                null comment '创建者',
    create_time         datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by           varchar(64) default ''                null comment '更新者',
    update_time         datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted          bit         default b'0'              not null comment '是否删除'
)
    comment 'OA 请假申请表' collate = utf8mb4_unicode_ci;

create table bpm_process_definition_info
(
    id                      bigint auto_increment comment '编号'
        primary key,
    process_definition_id   varchar(64)                             not null comment '流程定义的编号',
    model_id                varchar(64)                             not null comment '流程模型的编号',
    model_type              tinyint       default 10                not null comment '流程模型的类型',
    icon                    varchar(512)                            null comment '图标',
    description             varchar(255)                            null comment '描述',
    form_type               tinyint                                 not null comment '表单类型',
    form_id                 bigint                                  null comment '表单编号',
    form_conf               varchar(1000)                           null comment '表单的配置',
    form_fields             varchar(5000)                           null comment '表单项的数组',
    form_custom_create_path varchar(255)                            null comment '自定义表单的提交路径',
    form_custom_view_path   varchar(255)                            null comment '自定义表单的查看路径',
    simple_model            varchar(5000) default ''               null comment 'SIMPLE 设计器模型数据 JSON 格式',
    visible                 bit           default b'1'              not null comment '是否可见',
    start_user_ids          varchar(256)                            null comment '可发起用户编号数组',
    manager_user_ids        varchar(256)                            null comment '可管理用户编号数组',
    create_by               varchar(64)   default ''                null comment '创建者',
    create_time             datetime      default CURRENT_TIMESTAMP null comment '创建时间',
    update_by               varchar(64)   default ''                null comment '更新者',
    update_time             datetime      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted              bit           default b'0'              not null comment '是否删除'
)
    comment 'BPM 流程定义的信息表' collate = utf8mb4_unicode_ci;

create table bpm_process_expression
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(64) default ''                not null comment '表达式名字',
    status      tinyint                               not null comment '表达式状态',
    expression  varchar(1024)                         not null comment '表达式',
    create_by   varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit         default b'0'              not null comment '是否删除'
)
    comment 'BPM 流程表达式表' collate = utf8mb4_unicode_ci;

create table bpm_process_instance_copy
(
    id                    bigint auto_increment comment '编号'
        primary key,
    user_id               bigint      default 0                 not null comment '用户编号 (被抄送的用户编号)',
    start_user_id         bigint      default 0                 not null comment '发起流程的用户编号',
    process_instance_id   varchar(64) default ''                not null comment '流程实例的id',
    process_instance_name varchar(64) default ''                not null comment '流程实例的名字',
    category              varchar(64)                           not null comment '流程定义的分类',
    task_id               varchar(64) default ''                not null comment '发起抄送的任务编号',
    task_name             varchar(64)                           null comment '任务的名字',
    activity_id           varchar(64) default ''                null comment '流程活动编号',
    activity_name         varchar(128)                          null comment '流程活动的名字',
    reason                varchar(256)                          null comment '抄送意见',
    create_by             varchar(64) default ''                null comment '创建者',
    create_time           datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by             varchar(64) default ''                null comment '更新者',
    update_time           datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted            bit         default b'0'              not null comment '是否删除'
)
    comment 'BPM 流程实例抄送表' collate = utf8mb4_unicode_ci;

create table bpm_process_listener
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(30) default ''                not null comment '监听器名字',
    type        varchar(255)                          not null comment '监听器类型',
    status      tinyint                               not null comment '监听器状态',
    event       varchar(30) default ''                not null comment '监听事件',
    value_type  varchar(64) default ''                not null comment '监听器值类型',
    value       varchar(1024)                         not null comment '监听器值',
    create_by   varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit         default b'0'              not null comment '是否删除'
)
    comment 'BPM 流程监听器表' collate = utf8mb4_unicode_ci;

create table bpm_user_group
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(30)  default ''                not null comment '组名',
    description varchar(255) default ''                not null comment '描述',
    user_ids    varchar(1024)                          null comment '成员编号数组',
    status      tinyint                                not null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit          default b'0'              not null comment '是否删除'
)
    comment 'BPM 用户组表' collate = utf8mb4_unicode_ci;

create table gen_table
(
    table_id          bigint                      not null comment '编号'
        primary key,
    table_name        varchar(200) default ''     null comment '表名称',
    table_comment     varchar(500) default ''     null comment '表描述',
    sub_table_name    varchar(64)                 null comment '关联子表的表名',
    sub_table_fk_name varchar(64)                 null comment '子表关联的外键名',
    class_name        varchar(100) default ''     null comment '实体类名称',
    tpl_category      varchar(200) default 'crud' null comment '使用的模板（crud单表操作 tree树表操作）',
    package_name      varchar(100)                null comment '生成包路径',
    module_name       varchar(30)                 null comment '生成模块名',
    business_name     varchar(30)                 null comment '生成业务名',
    function_name     varchar(50)                 null comment '生成功能名',
    function_author   varchar(50)                 null comment '生成功能作者',
    gen_type          char         default '0'    null comment '生成代码方式（0zip压缩包 1自定义路径）',
    gen_path          varchar(200) default '/'    null comment '生成路径（不填默认项目路径）',
    options           varchar(1000)               null comment '其它生成选项',
    create_by         varchar(64)  default ''     null comment '创建者',
    create_time       datetime                    null comment '创建时间',
    update_by         varchar(64)  default ''     null comment '更新者',
    update_time       datetime                    null comment '更新时间',
    remark            varchar(500)                null comment '备注'
)
    comment '代码生成业务表' charset = utf8mb4;

create table gen_table_column
(
    column_id      bigint                    not null comment '编号'
        primary key,
    table_id       bigint                    null comment '归属表编号',
    column_name    varchar(200)              null comment '列名称',
    column_comment varchar(500)              null comment '列描述',
    column_type    varchar(100)              null comment '列类型',
    java_type      varchar(500)              null comment 'JAVA类型',
    java_field     varchar(200)              null comment 'JAVA字段名',
    is_pk          char                      null comment '是否主键（1是）',
    is_increment   char                      null comment '是否自增（1是）',
    is_required    char                      null comment '是否必填（1是）',
    is_insert      char                      null comment '是否为插入字段（1是）',
    is_edit        char                      null comment '是否编辑字段（1是）',
    is_list        char                      null comment '是否列表字段（1是）',
    is_query       char                      null comment '是否查询字段（1是）',
    query_type     varchar(200) default 'EQ' null comment '查询方式（等于、不等于、大于、小于、范围）',
    html_type      varchar(200)              null comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
    dict_type      varchar(200) default ''   null comment '字典类型',
    sort           int                       null comment '排序',
    create_by      varchar(64)  default ''   null comment '创建者',
    create_time    datetime                  null comment '创建时间',
    update_by      varchar(64)  default ''   null comment '更新者',
    update_time    datetime                  null comment '更新时间'
)
    comment '代码生成业务表字段' charset = utf8mb4;

create table sys_api
(
    id           bigint auto_increment comment '主键id'
        primary key,
    code         varchar(255)                       not null comment '接口编码',
    name         varchar(100)                       null comment '接口名称',
    notes        varchar(200)                       null comment '接口描述',
    method       varchar(20)                        null comment '请求方法',
    class_name   varchar(255)                       null comment '类名',
    method_name  varchar(100)                       null comment '方法名',
    path         varchar(255)                       null comment '请求路径',
    content_type varchar(100)                       null comment '响应类型',
    service_id   varchar(100)                       null comment '服务ID',
    status       tinyint  default 0                 null comment 'API状态:0:启用 1:禁用',
    auth         char     default '0'               null comment '是否认证:0:不认证 1:认证',
    create_by    varchar(32)                        null comment '创建人',
    update_by    varchar(32)                        null comment '更新人',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted   char     default '0'               null comment '删除标识',
    tenant_id    int                                null comment '租户ID'
)
    comment '系统接口表' charset = utf8mb4;

create table sys_attachment
(
    id                  bigint auto_increment comment '附件ID'
        primary key,
    storage_id          bigint        default 0                 not null comment '存储ID',
    attachment_group_id int           default 0                 not null comment '组ID',
    name                varchar(128)                            not null comment '文件名称',
    size                int                                     not null comment '文件大小',
    url                 varchar(2080)                           not null comment '文件地址',
    file_name           varchar(200)                            null comment '上传文件名',
    thumb_url           varchar(2080) default ''                not null comment '缩略图地址',
    type                tinyint(2)                              not null comment '类型',
    create_by           varchar(32)                             null comment '创建人',
    update_by           varchar(32)                             null comment '更新人',
    create_time         datetime      default CURRENT_TIMESTAMP null comment '创建时间',
    update_time         datetime                                null on update CURRENT_TIMESTAMP comment '修改时间',
    is_deleted          char          default '0'               null comment '删除标识',
    is_recycle          tinyint(1)    default 0                 not null comment '是否加入回收站 0.否|1.是'
)
    comment '附件表' charset = utf8mb4;

create index type
    on sys_attachment (type);

create table sys_client
(
    id                      bigint(64) auto_increment comment '主键'
        primary key,
    client_id               varchar(48)                        not null comment '客户端id',
    client_secret           varchar(256)                       not null comment '客户端密钥',
    resource_ids            varchar(256)                       null comment '资源集合',
    scope                   varchar(256)                       not null comment '授权范围',
    status                  tinyint  default 0                 not null comment '状态',
    authorized_grant_types  varchar(256)                       not null comment '授权类型',
    web_server_redirect_uri varchar(256)                       null comment '回调地址',
    authorities             varchar(256)                       null comment '权限',
    access_token_validity   int                                not null comment '令牌过期秒数',
    refresh_token_validity  int                                not null comment '刷新令牌过期秒数',
    additional_information  varchar(4096)                      null comment '附加说明',
    autoapprove             varchar(256)                       null comment '自动授权',
    create_by               varchar(32)                        null comment '创建人',
    update_by               varchar(32)                        null comment '更新人',
    create_time             datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time             datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted              int(2)   default 0                 not null comment '是否已删除'
)
    comment '客户端表' charset = utf8mb4;

create table sys_config
(
    id          bigint(64) auto_increment comment '主键'
        primary key,
    parent_id   bigint(64) default 0                 null comment '父主键',
    code        varchar(255)                         null comment '码',
    c_key       varchar(255)                         null comment '值',
    value       varchar(255)                         null comment '名称',
    sort        int                                  null comment '排序',
    remark      varchar(255)                         null comment '备注',
    create_by   varchar(32)                          null comment '创建人',
    update_by   varchar(32)                          null comment '更新人',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                             null on update CURRENT_TIMESTAMP comment '修改时间',
    tenant_id   int        default 1                 null,
    is_deleted  int(2)     default 0                 null comment '是否已删除'
)
    comment '配置表' charset = utf8mb4;

create table sys_depart
(
    id          bigint auto_increment comment '部门ID'
        primary key,
    name        varchar(50)                        null comment '部门名称',
    sort        int      default 0                 not null comment '排序',
    create_by   varchar(32)                        null comment '创建人',
    update_by   varchar(32)                        null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '修改时间',
    is_deleted  char     default '0'               null comment '删除标识',
    parent_id   bigint   default 0                 null comment '上级ID'
)
    comment '组织机构表' charset = utf8mb4;

create table sys_dept
(
    id             bigint auto_increment comment '部门id'
        primary key,
    code           varchar(32) default ''                null comment '部门编码',
    name           varchar(64) default ''                not null comment '部门名称',
    parent_id      bigint      default 0                 not null comment '父部门id',
    sort           int         default 0                 not null comment '显示顺序',
    leader_user_id bigint                                null comment '负责人',
    phone          varchar(11)                           null comment '联系电话',
    email          varchar(50)                           null comment '邮箱',
    status         tinyint     default 0                 not null comment '部门状态（0正常 1停用）',
    create_by      varchar(64) default ''                null comment '创建者',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by      varchar(64) default ''                null comment '更新者',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted     bit         default b'0'              not null comment '是否删除'
)
    comment '部门表' collate = utf8mb4_unicode_ci;

create table sys_dict_data
(
    id          bigint auto_increment comment '字典编码'
        primary key,
    sort        int          default 0                 not null comment '字典排序',
    label       varchar(100) default ''                not null comment '字典标签',
    value       varchar(100) default ''                not null comment '字典键值',
    dict_type   varchar(100) default ''                not null comment '字典类型',
    status      tinyint      default 0                 not null comment '状态（0正常 1停用）',
    color_type  varchar(100) default ''                null comment '颜色类型',
    css_class   varchar(100) default ''                null comment 'css 样式',
    remark      varchar(500)                           null comment '备注',
    create_by   varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit          default b'0'              not null comment '是否删除'
)
    comment '字典数据表' collate = utf8mb4_unicode_ci;

create table sys_dict_type
(
    id           bigint auto_increment comment '字典主键'
        primary key,
    name         varchar(100) default ''                not null comment '字典名称',
    type         varchar(100) default ''                not null comment '字典类型',
    status       tinyint      default 0                 not null comment '状态（0正常 1停用）',
    remark       varchar(500)                           null comment '备注',
    create_by    varchar(64)  default ''                null comment '创建者',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by    varchar(64)  default ''                null comment '更新者',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted   bit          default b'0'              not null comment '是否删除',
    deleted_time datetime                               null comment '删除时间'
)
    comment '字典类型表' collate = utf8mb4_unicode_ci;

create table sys_log
(
    id           bigint auto_increment comment '主键id'
        primary key,
    type         char     default '1'               null comment '日志类型',
    trace_id     varchar(64)                        null comment '跟踪ID',
    title        varchar(64)                        null comment '日志标题',
    operation    text                               null comment '操作内容',
    method       varchar(64)                        null comment '执行方法',
    params       text                               null comment '参数',
    url          varchar(128)                       null comment '请求路径',
    ip           varchar(64)                        null comment 'ip地址',
    exception    text                               null,
    execute_time decimal(11)                        null comment '耗时',
    location     varchar(64)                        null comment '地区',
    create_by    varchar(32)                        null comment '创建人',
    update_by    varchar(32)                        null comment '更新人',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted   char     default '0'               null comment '删除标识',
    tenant_id    int                                null comment '租户ID'
)
    comment '系统日志表' charset = utf8mb4;

create table sys_menu
(
    id                bigint auto_increment comment '菜单ID'
        primary key,
    type              int      default 0                 null comment '菜单类型（sys_type）',
    name              varchar(64)                        null comment '菜单名称',
    parent_id         bigint   default 0                 null comment '父菜单ID',
    permission        varchar(128)                       null comment '菜单权限',
    button_permission text                               null comment '按钮权限',
    sort              int      default 1                 null comment '排序值',
    level             int      default 1                 null comment '菜单等级',
    path              varchar(128)                       not null comment '路由路径',
    icon              varchar(128)                       null comment '菜单图标',
    component         varchar(128)                       null comment '组件路径',
    component_name    varchar(128)                       null comment '组件名',
    status            tinyint  default 0                 null comment '状态（0:启用，1:禁用）',
    visible           bit      default b'1'              null comment '是否可见',
    keep_alive        bit      default b'1'              null comment '是否缓存该页面: 1:是  0:不是',
    always_show       bit      default b'1'              not null comment '是否总是显示',
    target            bit      default b'0'              not null comment '是否外链',
    remark            varchar(255)                       null comment '备注',
    create_by         varchar(32)                        null comment '创建人',
    create_time       datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_by         varchar(32)                        null comment '更新人',
    update_time       datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted        bit      default b'0'              null comment '是否删除'
)
    comment '菜单表' charset = utf8mb4;

create table sys_menu_old
(
    id                bigint auto_increment comment '菜单ID'
        primary key,
    type              int      default 0                 null comment '菜单类型（sys_type）',
    name              varchar(32)                        null comment '菜单标题',
    parent_id         bigint   default 0                 null comment '父菜单ID',
    permission        varchar(32)                        null comment '菜单权限',
    button_permission text                               null comment '按钮权限',
    level             int      default 1                 null comment '菜单等级',
    path              varchar(128)                       not null comment '路由路径',
    component         varchar(128)                       null comment '组件路径',
    icon              varchar(64)                        null comment '菜单图标',
    status            char     default '0'               null comment '状态（0:启用，1:禁用）',
    sort              int      default 1                 null comment '排序值',
    remark            varchar(255)                       null comment '备注',
    keep_alive        bit      default b'0'              null comment '是否缓存该页面: 1:是  0:不是',
    hidden            bit      default b'0'              not null comment '是否隐藏',
    target            bit      default b'0'              not null comment '是否外链',
    create_by         varchar(32)                        null comment '创建人',
    update_by         varchar(32)                        null comment '更新人',
    create_time       datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted        bit      default b'0'              null comment '删除标识'
)
    comment '菜单表' charset = utf8mb4;

create table sys_post
(
    id          bigint auto_increment comment '岗位ID'
        primary key,
    code        varchar(64)                        not null comment '岗位编码',
    name        varchar(50)                        not null comment '岗位名称',
    sort        int      default 0                 null comment '排序',
    status      tinyint                            not null comment '状态（0正常 1停用）',
    remark      varchar(500)                       null comment '备注',
    create_by   varchar(32)                        null comment '创建人',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(32)                        null comment '更新人',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit      default b'0'              null comment '删除标识'
)
    comment '岗位表' collate = utf8mb4_unicode_ci;

create table sys_role
(
    id          bigint auto_increment comment '主键id'
        primary key,
    type        int      default 0                 null comment '角色类型（sys_type）',
    name        varchar(64)                        null comment '角色名称',
    code        varchar(64)                        null comment '角色编码',
    remark      varchar(256)                       null comment '描述',
    sort        int      default 0                 null comment '排序',
    status      tinyint                            null comment '状态',
    create_by   varchar(32)                        null comment '创建人',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(32)                        null comment '更新人',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit      default b'0'              null comment '删除标识'
)
    comment '角色表' charset = utf8mb4;

create index idx_role_role_code
    on sys_role (code);

create table sys_role_menu
(
    id         bigint(64) auto_increment comment '主键'
        primary key,
    role_id    bigint(64)   null comment '角色id',
    menu_id    bigint(64)   null comment '菜单id',
    permission varchar(256) null comment '权限'
)
    comment '角色菜单权限表' charset = utf8mb4;

create index sys_role_menu_role_id_index
    on sys_role_menu (role_id);

create table sys_route
(
    id          bigint auto_increment comment '主键id'
        primary key,
    name        varchar(100)                       null comment '服务名称',
    path        varchar(255)                       null comment '服务前缀',
    url         varchar(255)                       null comment '地址',
    service_id  varchar(100)                       null comment '服务编码',
    status      tinyint  default 0                 null comment 'API状态:0:启用 1:禁用',
    create_by   varchar(32)                        null comment '创建人',
    update_by   varchar(32)                        null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  char     default '0'               null comment '删除标识',
    tenant_id   int                                null comment '租户ID'
)
    comment '系统路由表' charset = utf8mb4;

create table sys_type
(
    id          bigint auto_increment comment '主键id'
        primary key,
    type        int      default 0                 not null comment '系统类型（0:平台系统，1:商家系统，2:音乐平台系统，3:音乐人系统等）',
    name        varchar(128) charset utf8          null comment '系统类型名称',
    remark      varchar(256) charset utf8          null comment '系统类型描述',
    create_by   varchar(32)                        null comment '创建人',
    update_by   varchar(32)                        null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '系统类型表（划分为用户、菜单、角色等）' charset = utf8mb4;

create table sys_user
(
    id          bigint(64) auto_increment comment '主键'
        primary key,
    account     varchar(45)                        null comment '账号',
    password    varchar(200)                       null comment '密码',
    user_type   int      default 0                 null comment '用户类型（sys_type）',
    login_type  int      default 0                 null comment '登录类型（1：用户名密码登录　2：手机号登录　3：社交登录）',
    name        varchar(20)                        null comment '昵称',
    real_name   varchar(10)                        null comment '真名',
    avatar      varchar(200)                       null comment '头像',
    email       varchar(45)                        null comment '邮箱',
    telephone   varchar(45)                        null comment '手机',
    birthday    datetime                           null comment '生日',
    sex         smallint                           null comment '性别',
    dept_id     bigint   default 0                 null comment '部门id',
    status      tinyint  default 0                 null comment '状态',
    remark      varchar(255)                       null comment '备注',
    create_by   varchar(32)                        null comment '创建人',
    update_by   varchar(32)                        null comment '更新人',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit      default b'0'              not null comment '逻辑删除'
)
    comment '用户表' charset = utf8mb4;

create table sys_user_role
(
    id      bigint(64) auto_increment comment '主键'
        primary key,
    user_id bigint(64) null comment '用户id',
    role_id bigint(64) null comment '角色id'
)
    comment '用户角色表' charset = utf8mb4;

create index sys_user_role_user_id_index
    on sys_user_role (user_id);

create table t_oa_leave
(
    id          bigint auto_increment comment '主键'
        primary key,
    user_id     bigint                                not null comment '申请人的用户编号',
    type        tinyint                               not null comment '请假类型',
    reason      varchar(200)                          not null comment '请假原因',
    start_time  datetime                              not null comment '开始时间',
    end_time    datetime                              not null comment '结束时间',
    day         tinyint                               not null comment '请假天数',
    status      tinyint                               not null comment '审批结果',
    instance_id varchar(64)                           null comment '流程实例编号',
    create_by   varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit         default b'0'              not null comment '是否删除'
)
    comment 'OA 请假申请表' collate = utf8mb4_unicode_ci;

create table test
(
    id          int auto_increment
        primary key,
    user_id     varchar(255)                       not null,
    order_id    varchar(255)                       not null,
    create_time datetime default CURRENT_TIMESTAMP not null
);

create table undo_log
(
    branch_id     bigint       not null comment 'branch transaction id',
    xid           varchar(128) not null comment 'global transaction id',
    context       varchar(128) not null comment 'undo_log context,such as serialization',
    rollback_info longblob     not null comment 'rollback info',
    log_status    int          not null comment '0:normal status,1:defense status',
    log_created   datetime(6)  not null comment 'create datetime',
    log_modified  datetime(6)  not null comment 'modify datetime',
    constraint ux_undo_log
        unique (xid, branch_id)
)
    comment 'AT transaction mode undo table';


