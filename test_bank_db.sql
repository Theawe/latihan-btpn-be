/*
 Navicat Premium Data Transfer

 Source Server         : localhost_5432
 Source Server Type    : PostgreSQL
 Source Server Version : 130012 (130012)
 Source Host           : localhost:5432
 Source Catalog        : test_bank_db
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130012 (130012)
 File Encoding         : 65001

 Date: 23/08/2023 16:21:12
*/


-- ----------------------------
-- Sequence structure for account_account_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."account_account_id_seq";
CREATE SEQUENCE "public"."account_account_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for transaction_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."transaction_id_seq";
CREATE SEQUENCE "public"."transaction_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS "public"."account";
CREATE TABLE "public"."account" (
  "account_id" int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
  "balance" numeric(19,2) NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for transaction
-- ----------------------------
DROP TABLE IF EXISTS "public"."transaction";
CREATE TABLE "public"."transaction" (
  "id" int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
  "amount" numeric(19,2),
  "debit_credit_status" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "transaction_date" timestamp(6),
  "account_id" int4 NOT NULL,
  "balance" numeric(19,2)
)
;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."account_account_id_seq"
OWNED BY "public"."account"."account_id";
SELECT setval('"public"."account_account_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."transaction_id_seq"
OWNED BY "public"."transaction"."id";
SELECT setval('"public"."transaction_id_seq"', 6, true);

-- ----------------------------
-- Primary Key structure for table account
-- ----------------------------
ALTER TABLE "public"."account" ADD CONSTRAINT "account_pkey" PRIMARY KEY ("account_id");

-- ----------------------------
-- Primary Key structure for table transaction
-- ----------------------------
ALTER TABLE "public"."transaction" ADD CONSTRAINT "transaction_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table transaction
-- ----------------------------
ALTER TABLE "public"."transaction" ADD CONSTRAINT "fk6g20fcr3bhr6bihgy24rq1r1b" FOREIGN KEY ("account_id") REFERENCES "public"."account" ("account_id") ON DELETE NO ACTION ON UPDATE NO ACTION;
