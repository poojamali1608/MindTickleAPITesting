package org.mindtickle.helpers;
import com.github.tomakehurst.wiremock.WireMockServer;
import static org.hamcrest.MatcherAssert.assertThat;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mindtickle.constants.EndPoints;
import org.mindtickle.constants.model.DeSerializationPojo.CreateUserPojo;
import org.mindtickle.constants.model.DeSerializationPojo.GetUserPojo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mindtickle.constants.model.DeSerializationPojo.UpdateUserPojo;
import org.mindtickle.constants.model.SerializationPojo.UserPOJO;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

class UserServiceHelperTest {
    UserServiceHelper userServiceHelper;
    WireMockServer wireMockServer;

    @BeforeEach
    public void setup () {

        userServiceHelper= new UserServiceHelper("");
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        }
    @AfterEach
    public void cleanup()
    {wireMockServer.stop();}

    @ParameterizedTest
    @CsvSource({"123,user1,fn1,ln1,e1,pwd1,phn1,0"})
    @DisplayName("Unit test with mock server for testing createUser method in userServiceHelper")
    public void StubbingPOSTMethod(int id,String Un,String fn,String ln, String email,String pwd,String phn, int status) throws ParseException {
        wireMockServer.stubFor(post(urlEqualTo(EndPoints.CREATE_MULTIPLE_USERS_WITH_ARRAY))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\n" +
                                "    \"code\": 200,\n" +
                                "    \"type\": \"unit test type unknown\",\n" +
                                "    \"message\": \"unit test message ok\"\n" +
                                "}")));
        CreateUserPojo createUserPojo = userServiceHelper.createUser(id, Un, fn, ln, email, pwd, phn, status);
        assertThat("message contains different value", createUserPojo.getMessage().equals("unit test message ok"));
        assertThat("type contains different value", createUserPojo.getType().equals("unit test type unknown"));
        assertThat("code contains value other than 200", createUserPojo.getCode() == 200);
        wireMockServer.verify(1,postRequestedFor(urlEqualTo(EndPoints.CREATE_MULTIPLE_USERS_WITH_ARRAY)));

    }


    @ParameterizedTest
    @CsvSource({"123,user1,fn1,ln1,e1,pwd1,phn1,0"})
    @DisplayName("Unit test with mock server for testing getUser method in userServiceHelper")
    public void StubbingGETMethod(int id,String Un,String fn,String ln, String email,String pwd,String phn, int status) throws ParseException {
        wireMockServer.stubFor(get(urlEqualTo(EndPoints.GET_SINGLE_USER+Un))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200).withBody("{\n" +
                                "    \"id\": 9222968140497195254,\n" +
                                "    \"username\": \"mockusername\",\n" +
                                "    \"firstName\": \"mock firstname\",\n" +
                                "    \"lastName\": \"mock lastName\",\n" +
                                "    \"email\": \"mock email\",\n" +
                                "    \"password\": \"mock password\",\n" +
                                "    \"phone\": \"mock phone\",\n" +
                                "    \"userStatus\": 0\n" +
                                "}")));

        GetUserPojo getUserPojo=userServiceHelper.getUser(Un);
        assertThat("Username contains different value",getUserPojo.getUsername().equals("mockusername"));
        assertThat("id returned is null",getUserPojo.getId()!=null);
        wireMockServer.verify(1,getRequestedFor(urlPathMatching(EndPoints.GET_SINGLE_USER+Un)));
    }



    @ParameterizedTest
    @CsvSource({"123,user1,mock_firstName_Updated,ln1,e1,pwd1,phn1,0"})
    @DisplayName("Unit test with mock server for testing  updateUser method in userServiceHelper")
    public void StubbingPUTMethod(int id,String Un,String fn,String ln, String email,String pwd,String phn, int status) throws ParseException {
        wireMockServer.stubFor(put(urlEqualTo(EndPoints.UPDATE_SINGLE_USER+Un))
                        .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\n" +
                                "    \"code\": 200,\n" +
                                "    \"type\": \"unknown\",\n" +
                                "    \"message\": \"12\"\n" +
                                "}")));
        UserPOJO userPOJOUpdate = new UserPOJO(id,Un,fn,ln,email,pwd,phn,status);
        UpdateUserPojo updateUserPojo=userServiceHelper.updateUser(userPOJOUpdate,Un);
        wireMockServer.verify(1,putRequestedFor(urlPathMatching(EndPoints.GET_SINGLE_USER+Un)));
 }



}