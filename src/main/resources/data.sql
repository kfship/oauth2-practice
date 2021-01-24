insert into user_info (username, password, userrole) values ('user', 'pass', 'USER');

insert into oauth_client_details (client_id, client_secret, scope, web_server_redirect_uri, authorized_grant_types)
values ('testClientId', 'testSecret', 'read,write', 'http://localhost:8080/oauth2/callback', 'authorization_code,password,refresh_token');

commit;