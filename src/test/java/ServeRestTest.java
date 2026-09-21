import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import com.lesson.homework.models.Usuario;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServeRestTest {

  private static String userId;
  private static String token;
  private static String userEmail;

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "https://serverest.dev";
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Order(1)
  @Test
  void shouldGetAllUsers() {
    given().
    when()
        .get("/usuarios")
    .then()
        .statusCode(200).contentType("application/json")
        .body("quantidade", greaterThan(0))
        .body("usuarios", not(empty()));
  }

  @Order(2)
  @Test
  void shouldFindUserByEmail() {
    String email = given().when().get("/usuarios")
        .then().extract().path("usuarios[0].email");

    given().queryParam("email", email)
        .when().get("/usuarios")
    .then().statusCode(200)
        .body("quantidade", equalTo(1))
        .body("usuarios[0].email", equalTo(email));
  }

  @Order(3)
  @Test
  void shouldCreateNewUser() {
    String uniqueEmail = "spy_" + System.currentTimeMillis() + "@qa.com";
    userEmail = uniqueEmail;

    String requestBody = """
        {
            "nome": "Тайный Покупатель",
            "email": "%s",
            "password": "secret123",
            "administrador": "true"
        }
        """.formatted(uniqueEmail); // %s заменяется на uniqueEmail

    var response = given()
        .contentType("application/json")
        .body(requestBody)
        .when()
        .post("/usuarios");

    response.then()
        .statusCode(201)
        .body("message", equalTo("Cadastro realizado com sucesso"))
        .body("_id", not(emptyString()));

    userId = response.path("_id");
    System.out.println("Создан пользователь с ID: " + userId);
  }

  @Test
  @Order(4)
  void shouldUpdateUser() {
      if (userId == null) {
        throw new IllegalStateException("Сначала выполните shouldCreateNewUser()");
    }

      String newRequestBody = """
              {
                  "nome": "Обновлённый Покупатель",
                  "email": "%s",
                  "password": "secret123",
                  "administrador": "false"
              }
              """.formatted(userEmail);

      given()
          .contentType("application/json")
          .body(newRequestBody)
          .pathParam("id", userId)
      .when()
          .put("/usuarios/{id}")
          .then()
          .statusCode(200)
          .body("message", equalTo("Registro alterado com sucesso"));

      System.out.println("Обновлен пользователь с ID: " + userId);
  }


  @Test
  @Order(5)
  void shouldLogin() {
    String loginBody = """
            {
                "email": "%s",
                "password": "secret123"
            }
            """.formatted(userEmail);

    var response = given()
        .contentType("application/json")
        .body(loginBody)
    .when()
        .post("/login");

    response.then()
        .statusCode(200)
        .body("message", equalTo("Login realizado com sucesso"))
        .body("authorization", not(emptyString()));

    token = response.path("authorization");
    System.out.println("Получен токен: " + token);
  }


  @Test
  @Order(6)
  void shouldDeleteUser() {
    given()
        .header("Authorization", token)
        .pathParam("id", userId)
    .when()
        .delete("/usuarios/{id}")
    .then()
        .statusCode(200)
        .body("message", equalTo("Registro excluído com sucesso"));

    // Проверяем, что пользователь действительно удален
    given()
        .pathParam("id", userId)
        .when()
        .get("/usuarios/{id}")
        .then()
        .statusCode(400)
        .body("message", equalTo("Usuário não encontrado"));
  }

  @Test
  @Order(7)
  void shouldGetAllProducts() {
    var response = given()
        .when()
        .get("/produtos");

    response.then()
        .statusCode(200)
        .body("quantidade", greaterThan(0))
        .body("produtos.preco", everyItem(not(0)))
        .body("produtos.nome", everyItem(not(emptyString())));

    String firstProductName = response.path("produtos[0].nome");

    response.then()
        .body("produtos.nome", hasItem(firstProductName));

    System.out.println("Первый товар в каталоге: " + firstProductName);
  }

  @Test
  @Order(8)
  @DisplayName("★ Создание пользователя через DTO (сериализация)")
  void shouldCreateUserFromDto() {
    String uniqueEmail = "dto_" + System.currentTimeMillis() + "@qa.com";

    Usuario usuario = new Usuario(
        "Тайный Покупатель DTO",
        uniqueEmail,
        "secret123",
        "true"
    );

    var response = given()
        .contentType("application/json")
        .body(usuario)             // передаём ОБЪЕКТ, а не строку
        .when()
        .post("/usuarios");

    // 4. Проверяем ответ
    response.then()
        .statusCode(201)
        .body("message", equalTo("Cadastro realizado com sucesso"))
        .body("_id", not(emptyString()));

    // 5. Сохраняем данные для возможного использования дальше
    userEmail = uniqueEmail;
    userId = response.path("_id");

    System.out.println("Создан пользователь через DTO, ID: " + userId);
  }
}
