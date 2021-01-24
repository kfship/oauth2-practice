package com.home.oauthpractice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.oauthpractice.entity.UserInfo;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class OauthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Access Token으로 접근하기
     */
    @Test
    public void accessTokenTest() throws Exception {
        // access_token 획득
        String clientId = "testClientId";
        String secret = "testSecret";
        String credentials = clientId+":"+secret;
        String encodedCredentials = new String(Base64.encode(credentials.getBytes()));

        String username = "user";
        String password = "pass";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);
        params.add("grant_type", "password");

        MvcResult mvcResult = mockMvc.perform(post("/oauth/token")
                .params(params)
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Basic " + encodedCredentials)
        ).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(response).contains("access_token");
        Assertions.assertThat(response).contains("read write");

        Map map = objectMapper.readValue(response, Map.class);
        System.out.println("map = " + map);


        // token 으로 접근

        String accessToken = (String) map.get("access_token");
        String url = "/api/me";

        MvcResult mvcResultApi = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer " + accessToken)
        ).andReturn();

        System.out.println("mvcResultApi = " + mvcResultApi);
        System.out.println("mvcResultApi.getResponse().getContentAsString() = " + mvcResultApi.getResponse().getContentAsString());
        String responseString = mvcResultApi.getResponse().getContentAsString();

        Assertions.assertThat(mvcResultApi.getResponse().getStatus()).isEqualTo(200);
        Assertions.assertThat(responseString).isEqualTo(username);
    }




    /**
     * Access Token 없이 접근하기
     */
    @Test
    public void noAccessTokenTest() throws Exception {
        String url = "/api/me";

        MvcResult mvcResult = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assertions.assertThat(mvcResult.getResponse().getStatus())
                .isEqualTo(401);
        Assertions.assertThat(mvcResult.getResponse().getContentAsString())
                .contains("unauthorized");
    }




    @Test
    public void oauthTokenTest() throws Exception {
        // given
        String clientId = "testClientId";
        String secret = "testSecret";
        String username = "user";
        String password = "pass";
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);


        // when
        MvcResult result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, secret))
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        System.out.println("result = " + result);
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("body = " + contentAsString);

        // then
        Assertions.assertThat(contentAsString).contains("access_token").contains("refresh_token");

        Map map = objectMapper.readValue(contentAsString, Map.class);
        System.out.println("map = " + map);


        // token 검토
        String accessToken = (String) map.get("access_token");
        String url = "/oauth/check_token?token=" + accessToken;

        MvcResult mvcResultApi = mockMvc.perform(get(url)).andReturn();

        System.out.println("mvcResultApi = " + mvcResultApi);
        System.out.println("mvcResultApi.getResponse().getContentAsString() = " + mvcResultApi.getResponse().getContentAsString());
        String checkTokenStr = mvcResultApi.getResponse().getContentAsString();

        Map checkTokenMap = objectMapper.readValue(checkTokenStr, Map.class);
        //boolean active = (boolean) checkTokenMap.get("active");
        String user_name = (String) checkTokenMap.get("user_name");
        String client_id = (String) checkTokenMap.get("client_id");

        //Assertions.assertThat(active).isEqualTo(true);
        Assertions.assertThat(user_name).isEqualTo(username);
        Assertions.assertThat(client_id).isEqualTo(clientId);
    }



    @Test
    public void passwordGrantTypeTest() throws Exception {
        // given
        String clientId = "testClientId";
        String secret = "testSecret";
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("grant_type", "password");
        params.add("username", "user");
        params.add("password", "pass");


        // when
        MvcResult result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, secret))
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        System.out.println("result = " + result);
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("body = " + contentAsString);

        // then
        Assertions.assertThat(contentAsString).contains("access_token").contains("refresh_token");
    }
}