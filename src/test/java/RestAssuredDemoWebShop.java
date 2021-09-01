import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class RestAssuredDemoWebShop {

	String me = "Nop.customer=fdb7e878-0a75-407d-9792-a1b13c8a71ba;";

	@Test
	void addToBasketTest() {
		Response response =
				given()
						.contentType("application/x-www-form-urlencoded; charset=UTF-8")
						.cookie(me)
						.body("product_attribute_16_5_4=13&product_attribute_16_6_5=15&" +
								"product_attribute_16_3_6=19&product_attribute_16_4_7=44&" +
								"product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
						.when()
						.post("http://demowebshop.tricentis.com/addproducttocart/details/16/1")
						.then()
						.statusCode(200)
						.body("success", is(true))
						.body("message", is("The product has been added to your <a href=\"/cart\">shopping " +
								"cart</a>"))
						.extract().response();

		String basket = response.path("updatetopcartsectionhtml");
		String cookieBaskBack = response.cookie("NOP.CUSTOMER");

		open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
		getWebDriver().manage().addCookie(new Cookie("NOP.CUSTOMER", cookieBaskBack));
		open("http://demowebshop.tricentis.com");
		assertThat($(".cart-qty").getText()).isEqualTo(basket);
	}
}
