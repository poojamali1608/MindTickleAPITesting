package org.mindtickle.helpers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mindtickle.constants.EndPoints;
import org.mindtickle.constants.model.SerializationPojo.PetCategoryPOJO;
import org.mindtickle.constants.model.SerializationPojo.PetModel;
import org.mindtickle.constants.model.SerializationPojo.TagPojo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;

class PetServiceHelperTest {
    PetServiceHelper petServiceHelper;
    WireMockServer wireMockServer;
    @BeforeEach
    public void setup () {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        petServiceHelper = new PetServiceHelper("");
    }
    @AfterEach
    public void cleanup()
    {wireMockServer.stop();}

    @ParameterizedTest
    @CsvSource({"1,categoryName,0,tagName,12345,myPet2,available","2,category2Name,0,tag2Name,123455456,my2Pet,pending"})
    @DisplayName("Unit test for Create method")
    public void unitTestForCreateMultiplePetsMethod(int cid,String cname, int tid,String tName,Long id,String name,String status) throws IOException {
        PetCategoryPOJO petCategoryPOJO = new PetCategoryPOJO(cid, cname);
        TagPojo tagPojo = new TagPojo(tid, tName);
        List<String> photoUrls = new ArrayList<>(Arrays.asList("PhotoURL"));
        List<TagPojo> ListOFtags = new ArrayList<>();
        ListOFtags.add(tagPojo);
        PetModel petModel = new PetModel(id, petCategoryPOJO, name, photoUrls, ListOFtags, status);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(petModel.toString());

        /* Build stub for Create pet with Array*/
        wireMockServer.stubFor(post(urlEqualTo(EndPoints.CREATE_PET))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(petModel))));

        /* placing the create request */
        petServiceHelper.createMultiplePets_UsingPOJO(cid, cname, tid, tName, id, name, status);
        wireMockServer.verify(1, postRequestedFor(urlEqualTo(EndPoints.CREATE_PET)));
    }

    @ParameterizedTest
    @CsvSource({"1,categoryName,0,tagName,12345,myPet,available"})
    @DisplayName("Unit test for Create method")
    public void unitTestForPUTPetMethod(int cid,String cname, int tid,String tName,Long id,String name,String status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PetCategoryPOJO petCategoryPOJO = new PetCategoryPOJO(cid, cname);
        TagPojo tagPojo = new TagPojo(tid, tName);
        List<String> photoUrls = new ArrayList<>(Arrays.asList("PhotoURL"));
        List<TagPojo> ListOFtags = new ArrayList<>();
        ListOFtags.add(tagPojo);
        PetModel petModelforUpdate = new PetModel(id, petCategoryPOJO, name, photoUrls, ListOFtags, status);

        /* Build stub for Get pet with Array*/
        wireMockServer.stubFor(put(urlEqualTo(EndPoints.UPDATE_PET))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(petModelforUpdate))));
        /*Placing the get request */
        Response response=petServiceHelper.UpdatePetStatus(status, Math.toIntExact(id),name);
        assertThat("Status code obtained is not 200 for get",response.getStatusCode()==200);

        /* Verifying if the call were hit to WIREMOCK SERVER AND NOT THE ACTUAL SERVER*/
        wireMockServer.verify(1,putRequestedFor(urlEqualTo(EndPoints.UPDATE_PET)));

    }

    @ParameterizedTest
    @CsvSource({"1,categoryName,0,tagName,12345,myPet,available"})
    @DisplayName("Unit test for Create method")
    public void unitTestFoGetPetMethod(int cid,String cname, int tid,String tName,Long id,String name,String status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PetCategoryPOJO petCategoryPOJO = new PetCategoryPOJO(cid, cname);
        TagPojo tagPojo = new TagPojo(tid, tName);
        List<String> photoUrls = new ArrayList<>(Arrays.asList("PhotoURL"));
        List<TagPojo> ListOFtags = new ArrayList<>();
        ListOFtags.add(tagPojo);
        PetModel petModel = new PetModel(id, petCategoryPOJO, name, photoUrls, ListOFtags, status);

        /* Build stub for Get pet with Array*/
        wireMockServer.stubFor(get(urlEqualTo(EndPoints.GET_PET_BY_STATUS+"?"+status))
                        .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(petModel))));
        /*Placing the get request */
        Response response=petServiceHelper.getPetsByStatus(status);
        assertThat("Status code obtained is not 200 for get",response.getStatusCode()==200);

        /* Verifying if the call were hit to WIREMOCK SERVER AND NOT THE ACTUAL SERVER*/
        wireMockServer.verify(1,getRequestedFor(urlEqualTo(EndPoints.GET_PET_BY_STATUS+"?"+status)));

    }



}