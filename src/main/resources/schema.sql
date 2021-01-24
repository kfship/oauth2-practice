-- 클라이언트 등록과 관련된 데이터 테이블
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(2000),
  autoapprove VARCHAR(256)
);

-- 발급된 액세스 토큰을 저장하기 위한 테이블
-- 예제는 JWT 토큰을 사용하므로 사용하지 않는다. 하지만 JWT토큰을 사용하지 않으면 해당 스키마를 사용한다.
create table oauth_access_token (
  token_id VARCHAR(256),
  token clob,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication clob,
  refresh_token VARCHAR(256)
);

-- 리프레시 토큰 발급을 위한 테이블
-- 이 테이블도 역시 JWT 토큰을 사용하므로 사용하지 않는다.
create table oauth_refresh_token (
  token_id VARCHAR(256),
  token varchar(1000),
  authentication clob
);

-- 사용자(Resource Owner)의 승인을 저장하기 위한 테이블
create table oauth_approvals (
  userId VARCHAR(256),
  clientId VARCHAR(256),
  scope VARCHAR(256),
  status VARCHAR(10),
  expiresAt TIMESTAMP,
  lastModifiedAt TIMESTAMP
);

-- authorization code table
create table oauth_code (
  code VARCHAR(256), authentication blob
);
