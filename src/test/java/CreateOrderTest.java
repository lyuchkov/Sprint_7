import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final Gson gson = new Gson();
    private Order order;

    public CreateOrderTest(Order order) {
        this.order = order;
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{new Order("ТестовоеИмя", "ТестоваяФамилия", "Тестовый адрес д.1", "Сокольники", "79111512256", 1, "2024-06-21T21:00:00.000Z","",new String[]{"GREY"})},
                new Object[]{new Order("ТестовоеИмя", "ТестоваяФамилия", "Тестовый адрес д.1", "Сокольники", "79111512256", 1, "2024-06-21T21:00:00.000Z","",new String[]{"GREY", "BLACK"})},
                new Object[]{new Order("ТестовоеИмя", "ТестоваяФамилия", "Тестовый адрес д.1", "Сокольники", "79111512256", 1, "2024-06-21T21:00:00.000Z","",new String[]{})}
        );
    }
    @Test
    @DisplayName("Проверка создания заказа")
    public void orderCreationTest(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(order))
                        .when()
                        .post("/api/v1/orders");
        assertNotNull(response.path("track"));
        assertThat(response.path("track"), instanceOf(Integer.class));
        assertEquals("Тело ответа: " + response.asString(), 201, response.statusCode());

    }
}
