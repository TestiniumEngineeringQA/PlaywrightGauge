package com.testinium.gaugeplaywright.steps;

import com.microsoft.playwright.Page;
import com.testinium.gaugeplaywright.support.PlaywrightRuntime;
import com.thoughtworks.gauge.Step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaywrightSteps {

    private Page page() {
        return PlaywrightRuntime.page();
    }

    @Step("I open the Google page")
    public void iOpenTheGooglePage() {
        PlaywrightRuntime.markStep("open-google-page");
        page().navigate("https://www.google.com");
    }

    @Step("I should see Google in the title")
    public void iShouldSeeGoogleInTheTitle() {
        PlaywrightRuntime.markStep("assert-google-title");
        assertTrue(page().title().contains("Google"));
    }

    @Step("I prepare the login page")
    public void iPrepareTheLoginPage() {
        PlaywrightRuntime.markStep("prepare-login-page");
        page().setContent(String.join("\n",
                "<html>",
                "  <head><title>Login</title></head>",
                "  <body>",
                "    <label>Username <input id=\"username\"></label>",
                "    <label>Password <input id=\"password\" type=\"password\"></label>",
                "    <button id=\"login\">Login</button>",
                "    <p id=\"result\"></p>",
                "    <script>",
                "      document.querySelector('#login').onclick = () => {",
                "        document.querySelector('#result').textContent = 'Login successful';",
                "      };",
                "    </script>",
                "  </body>",
                "</html>"
        ));
    }

    @Step("I enter login credentials")
    public void iEnterLoginCredentials() {
        PlaywrightRuntime.markStep("enter-login-credentials");
        page().locator("#username").fill("testinium-user");
        page().locator("#password").fill("secret-password");
    }

    @Step("I submit the login form")
    public void iSubmitTheLoginForm() {
        PlaywrightRuntime.markStep("submit-login-form");
        page().locator("#login").click();
    }

    @Step("I should see the login success message")
    public void iShouldSeeTheLoginSuccessMessage() {
        PlaywrightRuntime.markStep("assert-login-success");
        assertEquals("Login successful", page().locator("#result").textContent());
    }

    @Step("I prepare the todo page")
    public void iPrepareTheTodoPage() {
        PlaywrightRuntime.markStep("prepare-todo-page");
        page().setContent(String.join("\n",
                "<html>",
                "  <head><title>Todo List</title></head>",
                "  <body>",
                "    <input id=\"todo\" placeholder=\"New task\">",
                "    <button id=\"add\">Add</button>",
                "    <ul id=\"list\"></ul>",
                "    <script>",
                "      document.querySelector('#add').onclick = () => {",
                "        const input = document.querySelector('#todo');",
                "        const item = document.createElement('li');",
                "        item.textContent = input.value;",
                "        document.querySelector('#list').appendChild(item);",
                "        input.value = '';",
                "      };",
                "    </script>",
                "  </body>",
                "</html>"
        ));
    }

    @Step("I add a todo item")
    public void iAddATodoItem() {
        PlaywrightRuntime.markStep("add-todo-item");
        page().locator("#todo").fill("Check the Playwright trace report");
        page().locator("#add").click();
    }

    @Step("I should see the todo item in the list")
    public void iShouldSeeTheTodoItemInTheList() {
        PlaywrightRuntime.markStep("assert-todo-item");
        assertEquals(
                "Check the Playwright trace report",
                page().locator("#list li").textContent()
        );
    }

    @Step("I prepare the preferences page")
    public void iPrepareThePreferencesPage() {
        PlaywrightRuntime.markStep("prepare-preferences-page");
        page().setContent(String.join("\n",
                "<html>",
                "  <head><title>Test Preferences</title></head>",
                "  <body>",
                "    <label>Browser",
                "      <select id=\"browser\">",
                "        <option value=\"chromium\">Chromium</option>",
                "        <option value=\"firefox\">Firefox</option>",
                "        <option value=\"webkit\">WebKit</option>",
                "      </select>",
                "    </label>",
                "    <label><input id=\"headless\" type=\"checkbox\"> Run headless</label>",
                "  </body>",
                "</html>"
        ));
    }

    @Step("I choose Firefox and enable headless mode")
    public void iChooseFirefoxAndEnableHeadlessMode() {
        PlaywrightRuntime.markStep("set-preferences");
        page().locator("#browser").selectOption("firefox");
        page().locator("#headless").check();
    }

    @Step("I should see the selected preferences")
    public void iShouldSeeTheSelectedPreferences() {
        PlaywrightRuntime.markStep("assert-preferences");
        assertEquals("firefox", page().locator("#browser").inputValue());
        assertTrue(page().locator("#headless").isChecked());
    }

    @Step("It should throw runtime exception")
    public void RuntimeExcTest() {
        throw new RuntimeException();
    }

    // ---- Live site scenarios (the-internet.herokuapp.com) ----

    private Page newTabPage;

    @Step("I open the JavaScript alerts page")
    public void iOpenTheJavaScriptAlertsPage() {
        PlaywrightRuntime.markStep("open-js-alerts-page");
        page().navigate("https://the-internet.herokuapp.com/javascript_alerts");
    }

    @Step("I trigger the confirm dialog and accept it")
    public void iTriggerTheConfirmDialogAndAcceptIt() {
        PlaywrightRuntime.markStep("trigger-confirm-dialog");
        page().onDialog(dialog -> dialog.accept());
        page().locator("text=Click for JS Confirm").click();
    }

    @Step("I should see the confirm result message")
    public void iShouldSeeTheConfirmResultMessage() {
        PlaywrightRuntime.markStep("assert-confirm-result");
        assertEquals("You clicked: Ok", page().locator("#result").textContent());
    }

    @Step("I open the multiple windows page")
    public void iOpenTheMultipleWindowsPage() {
        PlaywrightRuntime.markStep("open-multiple-windows-page");
        page().navigate("https://the-internet.herokuapp.com/windows");
    }

    @Step("I click the link that opens a new tab")
    public void iClickTheLinkThatOpensANewTab() {
        PlaywrightRuntime.markStep("open-new-tab");
        newTabPage = page().context().waitForPage(() ->
                page().locator("text=Click Here").click()
        );
        newTabPage.waitForLoadState();
    }

    @Step("I should see the new tab content")
    public void iShouldSeeTheNewTabContent() {
        PlaywrightRuntime.markStep("assert-new-tab-content");
        assertEquals("New Window", newTabPage.locator("h3").textContent());
    }

    @Step("I open the rich text editor page")
    public void iOpenTheRichTextEditorPage() {
        PlaywrightRuntime.markStep("open-iframe-editor-page");
        page().navigate("https://the-internet.herokuapp.com/iframe");
    }

    @Step("I type text inside the editor iframe")
    public void iTypeTextInsideTheEditorIframe() {
        PlaywrightRuntime.markStep("type-inside-iframe");
        page().frameLocator("#mce_0_ifr").locator("#tinymce")
                .fill("Testinium Playwright suite");
    }

    @Step("I should see my text inside the editor iframe")
    public void iShouldSeeMyTextInsideTheEditorIframe() {
        PlaywrightRuntime.markStep("assert-iframe-text");
        assertEquals(
                "Testinium Playwright suite",
                page().frameLocator("#mce_0_ifr").locator("#tinymce").textContent()
        );
    }

    @Step("I open the dynamic loading page")
    public void iOpenTheDynamicLoadingPage() {
        PlaywrightRuntime.markStep("open-dynamic-loading-page");
        page().navigate("https://the-internet.herokuapp.com/dynamic_loading/2");
    }

    @Step("I start the dynamic loading process")
    public void iStartTheDynamicLoadingProcess() {
        PlaywrightRuntime.markStep("start-dynamic-loading");
        page().locator("#start button").click();
    }

    @Step("I should see the dynamically loaded text")
    public void iShouldSeeTheDynamicallyLoadedText() {
        PlaywrightRuntime.markStep("assert-dynamic-loading-text");
        page().locator("#finish h4").waitFor();
        assertEquals("Hello World!", page().locator("#finish h4").textContent());
    }

    @Step("I open the hovers page")
    public void iOpenTheHoversPage() {
        PlaywrightRuntime.markStep("open-hovers-page");
        page().navigate("https://the-internet.herokuapp.com/hovers");
    }

    @Step("I hover over the first user avatar")
    public void iHoverOverTheFirstUserAvatar() {
        PlaywrightRuntime.markStep("hover-first-avatar");
        page().locator(".figure").first().hover();
    }

    @Step("I hover over the first user avatar error")
    public void iHoverOverTheFirstUserAvatarError() {
        PlaywrightRuntime.markStep("hover-first-avatar");
        page().locator(".figure-ERROR").first().hover();
    }

    @Step("I should see that user's profile link")
    public void iShouldSeeThatUsersProfileLink() {
        PlaywrightRuntime.markStep("assert-profile-link");
        assertEquals(
                "View profile",
                page().locator(".figure").first().locator("a").textContent()
        );
    }

    @Step("I hover over the first user avatar wait")
    public void iHoverOverTheFirstUserAvatarWait() {
        page().waitForTimeout(20 * 60 * 1000);
    }

    // ---- Multi-step user journey (the-internet.herokuapp.com) ----

    @Step("I open the login page")
    public void iOpenTheLoginPage() {
        PlaywrightRuntime.markStep("open-real-login-page");
        page().navigate("https://the-internet.herokuapp.com/login");
    }

    @Step("I should see <fragment> in the page title")
    public void iShouldSeeInThePageTitle(String fragment) {
        PlaywrightRuntime.markStep("assert-page-title-contains");
        assertTrue(page().title().contains(fragment));
    }

    @Step("I enter the username <username>")
    public void iEnterTheUsername(String username) {
        PlaywrightRuntime.markStep("enter-username");
        page().locator("#username").fill(username);
    }

    @Step("I enter the password <password>")
    public void iEnterThePassword(String password) {
        PlaywrightRuntime.markStep("enter-password");
        page().locator("#password").fill(password);
    }

    @Step("I submit the site login form")
    public void iSubmitTheSiteLoginForm() {
        PlaywrightRuntime.markStep("submit-site-login-form");
        page().locator("button[type='submit']").click();
    }

    @Step("I should see the login success message <fragment>")
    public void iShouldSeeTheLoginSuccessMessage(String fragment) {
        PlaywrightRuntime.markStep("assert-site-login-success");
        assertTrue(page().locator("#flash").textContent().contains(fragment));
    }

    @Step("I open the checkboxes page")
    public void iOpenTheCheckboxesPage() {
        PlaywrightRuntime.markStep("open-checkboxes-page");
        page().navigate("https://the-internet.herokuapp.com/checkboxes");
    }

    @Step("the first checkbox should be unchecked")
    public void theFirstCheckboxShouldBeUnchecked() {
        PlaywrightRuntime.markStep("assert-first-checkbox-unchecked");
        assertFalse(page().locator("#checkboxes input[type='checkbox']").nth(0).isChecked());
    }

    @Step("I check the first checkbox")
    public void iCheckTheFirstCheckbox() {
        PlaywrightRuntime.markStep("check-first-checkbox");
        page().locator("#checkboxes input[type='checkbox']").nth(0).check();
    }

    @Step("the first checkbox should be checked")
    public void theFirstCheckboxShouldBeChecked() {
        PlaywrightRuntime.markStep("assert-first-checkbox-checked");
        assertTrue(page().locator("#checkboxes input[type='checkbox']").nth(0).isChecked());
    }

    @Step("the second checkbox should be checked")
    public void theSecondCheckboxShouldBeChecked() {
        PlaywrightRuntime.markStep("assert-second-checkbox-checked");
        assertTrue(page().locator("#checkboxes input[type='checkbox']").nth(1).isChecked());
    }

    @Step("I uncheck the second checkbox")
    public void iUncheckTheSecondCheckbox() {
        PlaywrightRuntime.markStep("uncheck-second-checkbox");
        page().locator("#checkboxes input[type='checkbox']").nth(1).uncheck();
    }

    @Step("the second checkbox should be unchecked")
    public void theSecondCheckboxShouldBeUnchecked() {
        PlaywrightRuntime.markStep("assert-second-checkbox-unchecked");
        assertFalse(page().locator("#checkboxes input[type='checkbox']").nth(1).isChecked());
    }

    @Step("I open the dropdown page")
    public void iOpenTheDropdownPage() {
        PlaywrightRuntime.markStep("open-dropdown-page");
        page().navigate("https://the-internet.herokuapp.com/dropdown");
    }

    @Step("I select dropdown option <value>")
    public void iSelectDropdownOption(String value) {
        PlaywrightRuntime.markStep("select-dropdown-option");
        page().locator("#dropdown").selectOption(value);
    }

    @Step("the selected dropdown option should be <value>")
    public void theSelectedDropdownOptionShouldBe(String value) {
        PlaywrightRuntime.markStep("assert-dropdown-value");
        assertEquals(value, page().locator("#dropdown").inputValue());
    }

    @Step("I open the add remove elements page")
    public void iOpenTheAddRemoveElementsPage() {
        PlaywrightRuntime.markStep("open-add-remove-elements-page");
        page().navigate("https://the-internet.herokuapp.com/add_remove_elements/");
    }

    @Step("I add a new element")
    public void iAddANewElement() {
        PlaywrightRuntime.markStep("add-new-element");
        page().locator("text=Add Element").click();
    }

    @Step("I should see <count> as the added elements count")
    public void iShouldSeeNAddedElements(String count) {
        PlaywrightRuntime.markStep("assert-added-elements-count");
        assertEquals(Integer.parseInt(count), page().locator(".added-manually").count());
    }

    @Step("I delete the added element")
    public void iDeleteTheAddedElement() {
        PlaywrightRuntime.markStep("delete-added-element");
        page().locator(".added-manually").click();
    }
}