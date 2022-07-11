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

    public int monthOfPlanningDate(int days) {
        return LocalDate.now().plusDays(days).getMonthValue();
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

        int daysUntilMeeting = 30;
        String planningDate;

        Configuration.browserSize = "1280x720";
        Configuration.browser = "chrome";
        open("http://localhost:9999/");
        $x("//*[@data-test-id=\"city\"]//input").setValue("Яр");
        ElementsCollection dropDownMenu = $$x("//*[contains(@class, \"menu-item\")]/*");
        dropDownMenu.find(Condition.exactText("Ярославль")).click();

        $x("//*[@name=\"phone\"]").setValue("+79998884433");

        $x("//*[@data-test-id=\"date\"]//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//*[contains(@class, \"icon_name_calendar\")]/../../..").click();
        if (monthOfPlanningDate(daysUntilMeeting) > LocalDate.now().plusDays(3).getMonthValue()) {
            int day = LocalDate.now().plusDays(daysUntilMeeting).getDayOfMonth();
            $x("//*[@data-step=\"1\"]").click();
            ElementsCollection calendarDays = $$x("//*[@class=\"calendar__row\"]//td");
            calendarDays.find(Condition.text(String.valueOf(day))).click();
        } else {
            ElementsCollection dates = $$x("//*[@class=\"calendar__row\"]//td");
            int day = LocalDate.now().plusDays(daysUntilMeeting).getDayOfMonth();
            dates.find(Condition.text(String.valueOf(day))).click();
        }
        planningDate = generateDate(daysUntilMeeting);

        $x("//*[@name=\"name\"]").setValue("Ильхом Додов");
        $x("//*[@name=\"phone\"]").setValue("+79998884433");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__content\"]/parent::button").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
