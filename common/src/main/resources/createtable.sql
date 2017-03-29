CREATE TABLE IF NOT EXISTS t_customer (
  cid         BIGINT      NOT NULL,
  certtype   SMALLINT    NOT NULL,
  certno     VARCHAR(32) NOT NULL,
  name       VARCHAR(64) NOT NULL,
  createtime BIGINT      NOT NULL DEFAULT 0,
  updatetime BIGINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (cid)
);

CREATE TABLE IF NOT EXISTS t_account (
  aid         BIGINT   NOT NULL,
  type       SMALLINT NOT NULL,
  custid     BIGINT   NOT NULL,
  parentid   BIGINT   NOT NULL,
  statusid   TINYINT  NOT NULL,
  createtime BIGINT   NOT NULL DEFAULT 0,
  updatetime BIGINT   NOT NULL DEFAULT 0,
  PRIMARY KEY (aid)
);

CREATE TABLE IF NOT EXISTS t_csdcacct (
  exchid     SMALLINT    NOT NULL,
  type       SMALLINT    NOT NULL,
  no         VARCHAR(10) NOT NULL,
  custid     BIGINT      NOT NULL,
  statusid   TINYINT     NOT NULL,
  createtime BIGINT      NOT NULL DEFAULT 0,
  updatetime BIGINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (exchid, type, no)
);

CREATE TABLE IF NOT EXISTS t_csdcacctlink (
  exchid       SMALLINT    NOT NULL,
  exchaccttype SMALLINT    NOT NULL,
  exchacctno   VARCHAR(10) NOT NULL,
  acctid       BIGINT      NOT NULL,
  createtime   BIGINT      NOT NULL DEFAULT 0,
  updatetime   BIGINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (exchid, exchaccttype, exchacctno, acctid)
);

CREATE TABLE IF NOT EXISTS t_position (
  acctid     BIGINT         NOT NULL,
  prodid     BIGINT         NOT NULL,
  type       SMALLINT       NOT NULL,
  settleflag TINYINT        NOT NULL,
  bizdate    INT            NOT NULL,
  settledate INT            NOT NULL,
  nos        DECIMAL(16, 3) NOT NULL,
  frozennos  DECIMAL(16, 3) NOT NULL,
  cytype     SMALLINT       NOT NULL,
  createtime BIGINT         NOT NULL DEFAULT 0,
  updatetime BIGINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (acctid, prodid, type, settleflag, bizdate, settledate)
);