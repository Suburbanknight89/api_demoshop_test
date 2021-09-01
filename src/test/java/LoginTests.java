

import io.qameta.allure.Story;

import org.junit.jupiter.api.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@Story("Login tests")
public class LoginTests {

	@Test
	@Tag("demowebshop")
	@Disabled("Example test code for further test development")
	@DisplayName("Successful authorization to some demowebshop (UI)")
	void loginTest() {
		step("Open login page", () ->
			open("http://demowebshop.tricentis.com/login"));

		step("Fill login form", () -> {
			$("#Email").setValue("lumenteam@yandex.ru");
			$("#Password").setValue("reload")
					.pressEnter();
		});

		step("Verify successful authorization", () -> {
			$(".account").shouldHave(text("lumenteam@yandex.ru"));
		});

		step("Get cookie by api and set it to browser", () -> {
			Response responce =
					given()
								.contentType("application/x-www-form-urlencoded; charset=UTF-8")
								.body("product_attribute_74_5_26=81&product_attribute_74_6_27=83" +
										"&product_attribute_74_3_28=86" +
										"&addtocart_74.EnteredQuantity=1")
								.when()
								.post("http://demowebshop.tricentis.com/addproducttocart/details/74/1")
								.then()
								.statusCode(200)
								.body("success", is(true))
								.body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
								.extract().response();
			});
		}
	}
