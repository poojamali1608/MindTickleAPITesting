package org.mindtickle.helpers;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.mindtickle.constants.EndPoints;
import org.mindtickle.constants.model.DeSerializationPojo.CreateUserPojo;
import org.mindtickle.constants.model.DeSerializationPojo.GetUserPojo;
import org.mindtickle.constants.model.DeSerializationPojo.UpdateUserPojo;
import org.mindtickle.constants.model.SerializationPojo.UserPOJO;
import org.mindtickle.utils.ConfigManager;
import java.io.IOException;

public class UserServiceHelper {
    private static final String BASE_URL;

    static {
        try {
            BASE_URL = ConfigManager.getInstance().getString("baseURL");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserServiceHelper() {
        RestAssured.baseURI = BASE_URL;
    }
    public UserServiceHelper(String unittest) {

    }


    public CreateUserPojo createUser(int id,String Un,String fn,String ln, String email,String pwd,String phn, int status) {
        UserPOJO userPOJO = new UserPOJO(id,Un,fn,ln,email,pwd,phn,status);
        Gson gson = new Gson();
        String payLoad = "["+gson.toJson(userPOJO)+"]";
        CreateUserPojo createUserPojo=RestAssured.given().contentType(ContentType.JSON).log().uri().log().method()
                .when().log().body().body(payLoad).post(EndPoints.CREATE_MULTIPLE_USERS_WITH_ARRAY).as(CreateUserPojo.class);
        return createUserPojo;
    }


    public UpdateUserPojo updateUser(UserPOJO userPOJOUpdate,String Un)
    {
        UpdateUserPojo updateUserPojo = RestAssured.given().contentType(ContentType.JSON).log().uri().log().method()
                .when().body(userPOJOUpdate).put(EndPoints.UPDATE_SINGLE_USER+Un).as(UpdateUserPojo.class);
        return  updateUserPojo;
    }

    public GetUserPojo getUser(String Un)
    {
        GetUserPojo getUserPojo = RestAssured.given().contentType(ContentType.JSON)
                .when().get(EndPoints.GET_SINGLE_USER+Un).as(GetUserPojo.class);
        return getUserPojo;

    }
}