import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardFormTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String millisSinceDate(int days) {
        long millis = LocalDate.now().plusDays(days).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return String.valueOf(millis);
    }

    @Test
    public void shouldShowSuccessNotification() {

        String planningDate = generateDate(4);

        Configuration.browserSize = "1280x720";
        Configuration.browser = "chrome";
        open("http://localhost:9999/");
        $x("//*[@data-test-id=\"city\"]//input").setValue("Ярославль");
        $x("//*[@data-test-id=\"date\"]//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//*[@data-test-id=\"date\"]//input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Ильхом Додов");
        $x("//*[@name=\"phone\"]").setValue("+79998884433");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__content\"]/parent::button").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test
    public void sendFormUsingDropdownAndCalendarWidget() {

        String planningDate = generateDate(7);

        Configuration.browserSize = "1280x720";
        Configuration.browser = "chrome";
        open("http://localhost:9999/");
        $x("//*[@data-test-id=\"city\"]//input").setValue("Яр");
        ElementsCollection dropDownMenu = $$x("//*[contains(@class, \"menu-item\")]/*");
        dropDownMenu.find(Condition.exactText("Ярославль")).click();

        $x("//*[@name=\"phone\"]").setValue("+79998884433");

        $x("//*[@data-test-id=\"date\"]//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String weekFromNow = millisSinceDate(7);
        $x("//*[contains(@class, \"icon_name_calendar\")]/../../..").click();
        ElementsCollection calendarDays = $$x("//*[@class=\"calendar__row\"]//td");

        calendarDays.find(Condition.attribute("data-day", weekFromNow)).click();

//        if (calendarDays.isEmpty()) {
//            $x("//*[@data-step=\"1\"]").click();
//            calendarDays = $$x("//*[@class=\"calendar__row\"]//td");
//        } else {
//            calendarDays.first().click();
//        }

        $x("//*[@name=\"name\"]").setValue("Ильхом Додов");
        $x("//*[@name=\"phone\"]").setValue("+79998884433");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__content\"]/parent::button").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
