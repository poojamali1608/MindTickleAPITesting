package org.mindtickle.E2ETests;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mindtickle.helpers.PetServiceHelper;
import java.io.IOException;
import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;

public class testPetAPI {

    private PetServiceHelper PetServiceHelper;
    @BeforeEach
    public void init(){
        PetServiceHelper = new PetServiceHelper();}

    @ParameterizedTest
    @CsvSource({"pending,1234,user1","available,1,user2","sold,12345,user3"})
    @DisplayName("Test user with POJO")
    public void testPet(String statusToUpdate,int id,String user) throws IOException {
        PetServiceHelper.createMultiplePets(statusToUpdate,id,user);
        JSONArray jsonArrayPreUpdate = new JSONArray(PetServiceHelper.getPetsByStatus(statusToUpdate).asString());
        PetServiceHelper.UpdatePetStatus(statusToUpdate,id,user);
        JSONArray jsonArrayAfterUpdate = new JSONArray(PetServiceHelper.getPetsByStatus(statusToUpdate).asString());
        assertThat("The count for "+statusToUpdate+" is not increased by "+1,jsonArrayAfterUpdate.length()-jsonArrayPreUpdate.length()==1);

    }

    @ParameterizedTest
    @CsvSource({"pending,1","available,1","sold,1"})
    @DisplayName("Update the pet status  by 1 and verify if count increase by 1 by performing get for values given in @CsvSource")
    public void VerifyCountForAvailableStatusUpdatesByOne(String statusToUpdate,String val_Increased ) throws IOException, InterruptedException {
            JSONArray jsonArrayPreUpdate = new JSONArray(PetServiceHelper.getPetsByStatus(statusToUpdate).asString());
            String user = RandomStringUtils.randomAlphabetic(10);
            Random random = new Random(); int id = random.nextInt();
            PetServiceHelper.UpdatePetStatus(statusToUpdate,id,user);
            Thread.sleep(1000*5);
            JSONArray jsonArrayAfterUpdate = new JSONArray(PetServiceHelper.getPetsByStatus(statusToUpdate).asString());
            assertThat("The count for "+statusToUpdate+" is not increased by "+val_Increased,jsonArrayAfterUpdate.length()-jsonArrayPreUpdate.length()==Integer.parseInt(val_Increased));
 }



}
