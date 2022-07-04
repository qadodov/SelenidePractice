import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Selenide.*;

public class CardFormTest {

    @Test
    public void shouldShowSuccessNotification() {

        LocalDate currentDayPlusThreeDays = LocalDate.now().getChronology().dateNow().plusDays(3);
        String threeDaysFromNow = currentDayPlusThreeDays.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        Configuration.browserSize = "1280x720";
        Configuration.browser = "chrome";
        open("http://localhost:9999/");
        $x("//*[@data-test-id=\"city\"]//input").setValue("Ярославль");
        $x("//*[@data-test-id=\"date\"]//input").setValue(threeDaysFromNow);
        $x("//*[@name=\"name\"]").setValue("Ильхом Додов");
        $x("//*[@name=\"phone\"]").setValue("+79998884433");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__content\"]/parent::button").click();
        $x("//*[@data-test-id=\"notification\"]").shouldNotBe(hidden, Duration.ofSeconds(15));
    }

    @Test
    public void sendFormUsingDropdownAndCalendarWidget() {

        Configuration.browserSize = "1280x720";
        Configuration.browser = "chrome";
        open("http://localhost:9999/");
        $x("//*[@data-test-id=\"city\"]//input").setValue("Яр");
        ElementsCollection dropDownMenu = $$x("//*[contains(@class, \"menu-item\")]/*");
        dropDownMenu.last().click();
        $x("//*[@name=\"phone\"]").setValue("+79998884433");
        $x("//*[contains(@class, \"icon_name_calendar\")]/../../..").click();
        ElementsCollection calendarDays = $$x("//*[@role=\"gridcell\"]");
        int weekFromNow = LocalDate.now().plusDays(7).getDayOfMonth();
        String currentDayPlus7 = Integer.toString(weekFromNow);
        calendarDays.find(Condition.attributeMatching("innerHTML", currentDayPlus7)).click();
        $x("//*[@name=\"name\"]").setValue("Ильхом Додов");
        $x("//*[@name=\"phone\"]").setValue("+79998884433");
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@class=\"button__content\"]/parent::button").click();
        $x("//*[@data-test-id=\"notification\"]").shouldBe(appear, Duration.ofSeconds(15));
    }
}
