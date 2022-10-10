package org.mindtickle.helpers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.mindtickle.constants.EndPoints;
import org.mindtickle.constants.model.SerializationPojo.PetCategoryPOJO;
import org.mindtickle.constants.model.SerializationPojo.PetModel;
import org.mindtickle.constants.model.SerializationPojo.TagPojo;
import org.mindtickle.utils.ConfigManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;

public class PetServiceHelper {
  private static final String BASE_URL;
   static {
        try {
            BASE_URL = ConfigManager.getInstance().getString("baseURL");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*No Arg Constructor is a part of E2E tests */
    public PetServiceHelper() {
        RestAssured.baseURI = BASE_URL;
    }

    public PetServiceHelper(String UnitTest) { }


    public void createMultiplePets(String statusToUpdate,int id,String user) {
        PetCategoryPOJO petCategoryPOJO = new PetCategoryPOJO(id,user);
        List<String> PhotoURLsList= new ArrayList<>(Arrays.asList("PhotourlSample"));
        TagPojo tagPojo1 = new TagPojo(987,"tagpojo1");
        TagPojo tagPojo2 = new TagPojo(987,"tagpojo2");
        List<TagPojo>TagPojoList =new ArrayList<>(Arrays.asList(tagPojo1,tagPojo2));
        PetModel petModel = new PetModel(123L,petCategoryPOJO,user,PhotoURLsList,TagPojoList,"pending");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(petModel).post(EndPoints.CREATE_PET)
                .then().assertThat().statusCode(200);
}

    public PetModel createMultiplePets_UsingPOJO(int cid,String cname, int tid,String tName,Long id,String name,String status) {
        PetCategoryPOJO petCategoryPOJO = new PetCategoryPOJO(cid,cname);
        List<String> PhotoURLsList= new ArrayList<>(Arrays.asList("PhotourlSample"));
        TagPojo tagPojo1 = new TagPojo(tid,tName);
        TagPojo tagPojo2 = new TagPojo(987,"tagpojo2");
        List<TagPojo>TagPojoList =new ArrayList<>(Arrays.asList(tagPojo1,tagPojo2));
        PetModel petModel = new PetModel(id,petCategoryPOJO,name,PhotoURLsList,TagPojoList,status);
        PetModel petmodel=RestAssured.given().contentType(ContentType.JSON)
                        .when().body(petModel).post(EndPoints.CREATE_PET).as(PetModel.class);
        return petmodel;


    }
    public Response UpdatePetStatus(String statusToUpdate,int id,String user) {
         String payload ="{\n" +
                "        \"id\": "+Math.abs(id)+",\n" +
                "            \"category\": {\n" +
                "        \"id\": 0,\n" +
                "                \"name\": \""+user+"\"\n" +
                "    },\n" +
                "        \"name\": \"Automated tests\",\n" +
                "            \"photoUrls\": [\n" +
                "        \"string\"\n" +
                "],\n" +
                "        \"tags\": [\n" +
                "        {\n" +
                "            \"id\": 0,\n" +
                "                \"name\": \""+user+"\"\n" +
                "        }\n" +
                "],\n" +
                "        \"status\": \""+statusToUpdate+"\"\n" +
                "    }";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON).log().uri().log().method()
                .when().body(payload)
                .put(EndPoints.CREATE_PET);
        assertThat("Assert check with post", response.statusCode()==200);
        return response;
    }

    public Response getPetsByStatus(String parameter) throws IOException {

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get(EndPoints.GET_PET_BY_STATUS+"?"+parameter);
        assertThat("Checking for response code ", response.statusCode()==200);

        return response;
    }


}
