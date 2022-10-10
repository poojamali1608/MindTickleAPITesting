package org.mindtickle.E2ETests;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mindtickle.constants.EndPoints;
import org.mindtickle.constants.model.DeSerializationPojo.CreateUserPojo;
import org.mindtickle.constants.model.DeSerializationPojo.GetUserPojo;
import org.mindtickle.constants.model.DeSerializationPojo.UpdateUserPojo;
import org.mindtickle.constants.model.SerializationPojo.UserPOJO;
import org.mindtickle.helpers.UserServiceHelper;
import static org.hamcrest.MatcherAssert.assertThat;

public class testUserAPI {
private UserServiceHelper UserServiceHelper;
    @BeforeEach
    public void init(){
    UserServiceHelper = new UserServiceHelper();}

    @ParameterizedTest
    @CsvSource({"123,user1,fn1,ln1,e1,pwd1,phn1,0","456,user2,fn2,ln2,e2,pwd2,phn2,1"})
    @DisplayName("Test to get the Updated User")
    public void CreateMultipleUser(int id,String Un,String fn,String ln, String email,String pwd,String phn, int status) throws JsonProcessingException, InterruptedException {
        CreateUserPojo createUserPojo = UserServiceHelper.createUser(id,Un,fn,ln,email,pwd,phn,status);
        assertThat("Code obtained in response is not 200",createUserPojo.getCode()==200);
        assertThat("Message obtained in response is not ok",createUserPojo.getMessage().equals("ok"));

        UserPOJO userPOJOUpdate = new UserPOJO(id,Un,"firstName_Updated",ln,email,pwd,phn,status);
        UpdateUserPojo updateUserPojo = UserServiceHelper.updateUser(userPOJOUpdate,Un);
        GetUserPojo getUserPojo = RestAssured.given().log().uri().log().method()
                .when().get(EndPoints.GET_SINGLE_USER+Un).as(GetUserPojo.class);
        assertThat("ID obtained is null",getUserPojo.getId()!=null);
        assertThat("Data for first name does not match",getUserPojo.getFirstName().equals("firstName_Updated"));
        assertThat("Data for last name does not match",getUserPojo.getLastName().equals(ln));

    }

}
