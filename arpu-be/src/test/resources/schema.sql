DROP TABLE IF EXISTS tbl_address;
DROP TABLE IF EXISTS tbl_code_code;
DROP TABLE IF EXISTS tbl_customer;
DROP TABLE IF EXISTS tbl_customer_ref;
DROP TABLE IF EXISTS tbl_interest;
DROP TABLE IF EXISTS tbl_job_config;
DROP TABLE IF EXISTS tbl_loan;
DROP TABLE IF EXISTS tbl_log_api;
DROP TABLE IF EXISTS tbl_reference;
DROP TABLE IF EXISTS tbl_sync_audit_log;
DROP TABLE IF EXISTS tbl_version;

CREATE TABLE tbl_interest (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  created_date datetime DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  interest_rate varchar(255) DEFAULT NULL,
  loan_term varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  status varchar(255) DEFAULT NULL
);

CREATE TABLE tbl_reference (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  full_name varchar(255) DEFAULT NULL,
  msisdn varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE tbl_customer_ref (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  created_date datetime DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  reference_id bigint(20) DEFAULT NULL,
  relationship_id varchar(255) DEFAULT NULL
);

CREATE TABLE tbl_code_code (
  id varchar(255) PRIMARY KEY,
  code_name varchar(255) DEFAULT NULL,
  code_type varchar(255) DEFAULT NULL,
  code_type_name varchar(255) DEFAULT NULL,
);

CREATE TABLE tbl_address (
  id INT PRIMARY KEY AUTO_INCREMENT,
  created_date datetime DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  address_detail varchar(255) DEFAULT NULL,
  district varchar(255) DEFAULT NULL,
  province varchar(255) DEFAULT NULL,
  village varchar(255) DEFAULT NULL
);

CREATE TABLE tbl_customer (
   id INT PRIMARY KEY AUTO_INCREMENT,
   created_date datetime  DEFAULT NULL,
   last_modified_date datetime DEFAULT NULL,
   arpu bigint(20) DEFAULT NULL,
   arpu_latest_three_months decimal(19,2) DEFAULT NULL,
   batch_status varchar(255) DEFAULT NULL,
   customer_account varchar(255) DEFAULT NULL,
   date_of_birth date DEFAULT NULL,
   date_of_issue date DEFAULT NULL,
   email varchar(255) DEFAULT NULL,
   full_name varchar(255) DEFAULT NULL,
   gender varchar(255) DEFAULT NULL,
   identity_number varchar(255) DEFAULT NULL,
   identity_type varchar(255) DEFAULT NULL,
   loan_maximum decimal(19,2) DEFAULT NULL,
   loan_minimum decimal(19,2) DEFAULT NULL,
   lock_status varchar(255) DEFAULT NULL,
   msisdn varchar(255) DEFAULT NULL,
   nationality varchar(255) DEFAULT NULL,
   place_of_issue varchar(255) DEFAULT NULL,
   score_max int(11) DEFAULT NULL,
   score_min int(11) DEFAULT NULL,
   version_id bigint(20) DEFAULT NULL,
   viettelpay_wallet varchar(255) DEFAULT NULL,
   address_id bigint(20) DEFAULT NULL
);



CREATE TABLE tbl_loan (
  id INT PRIMARY KEY AUTO_INCREMENT,
  created_date datetime DEFAULT NULL,
  last_modified_date datetime DEFAULT NULL,
  amount_pay decimal(19,2) DEFAULT NULL,
  amount_spent decimal(19,2) DEFAULT NULL,
  arpu_latest_three_months decimal(19,2) DEFAULT NULL,
  contract_link varchar(255) DEFAULT NULL,
  expiration_date date DEFAULT NULL,
  fee decimal(19,2) DEFAULT NULL,
  is_automatic_payment tinyint(4) NOT NULL,
  limit_remaining decimal(19,2) DEFAULT NULL,
  loan_account varchar(255) DEFAULT NULL,
  loan_amount decimal(19,2) DEFAULT NULL,
  maximum_limit decimal(19,2) DEFAULT NULL,
  minimum_limit decimal(19,2) DEFAULT NULL,
  profit_amount decimal(19,2) DEFAULT NULL,
  reason_rejection varchar(255) DEFAULT NULL,
  repayment_form varchar(255) DEFAULT NULL,
  approval_status_id varchar(255) DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  customer_ref_id bigint(20) DEFAULT NULL,
  interest_id bigint(20) DEFAULT NULL,
  loan_status_id varchar(255) DEFAULT NULL
);

-- auto-generated definition
create table tbl_version
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    batch_id     bigint       null,
    batch_name   varchar(255) null,
    created_date datetime     null,
    reason       varchar(255) null,
    run_status   varchar(255) null,
    version      bigint       null
);
-- auto-generated definition
create table tbl_sync_audit_log
(
    id                 bigint auto_increment
        primary key,
    created_date       datetime      null,
    last_modified_date datetime      null,
    reason             varchar(5000) not null,
    record_content     varchar(500)  not null,
    record_number      bigint        not null,
    version_id         bigint        not null
);

-- auto-generated definition
create table tbl_job_config
(
    batch_id    bigint       not null
        primary key,
    cron        varchar(255) not null,
    description varchar(255) null
);

CREATE TABLE tbl_log_api (
request_id varchar(255) PRIMARY KEY,
created_date datetime DEFAULT NULL,
last_modified_date datetime DEFAULT NULL,
request_body_info text DEFAULT NULL,
request_header_info text DEFAULT NULL,
response_body_info text DEFAULT NULL
)
