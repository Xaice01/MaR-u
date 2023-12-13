package com.example.mareu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // Suppression d'une réunion dans la liste générale
    @Test
    public void deleteReunionTest() throws InterruptedException {
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion A - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Réunion A - 19H0 - Mario")));

        onView(withId(R.id.list_reunion_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));

        Thread.sleep(1000); //wait to delete Reunion

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion A - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());
    }

    // Suppression d'une réunion dans un filtre date vérification si elle est bien supprimé de la liste du filtre et de la liste générale
    @Test
    public void deleteReunionWithFiltreDateTest() throws InterruptedException {
        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(androidx.transition.R.id.title), withText("Filtrer par date"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 1, 25));

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());


        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion C - 19H0 - Luigi"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Réunion C - 19H0 - Luigi")));
        //Delete
        onView(withId(R.id.list_reunion_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));

        Thread.sleep(1000); //wait to delete Reunion

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion C - 19H0 - Luigi"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());

        overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        appCompatTextView = onView(
                allOf(withId(androidx.transition.R.id.title), withText("Reset du filtre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion C - 19H0 - Luigi"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());

    }

    // Suppression d'une réunion dans un filtre lieu vérification si elle est bien supprimé de la liste du filtre et de la liste générale
    @Test
    public void deleteReunionWithFiltreSalleTest() throws InterruptedException {
        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(androidx.core.R.id.title), withText("Filtrer par lieu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Mario"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion B - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Réunion B - 19H0 - Mario")));
        //Delete
        onView(withId(R.id.list_reunion_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));

        Thread.sleep(1000); //wait to delete Reunion

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion B - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());

        overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        appCompatTextView = onView(
                allOf(withId(androidx.transition.R.id.title), withText("Reset du filtre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion B - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());
    }

    // Création d'une réunion vérification si la réunion est créée dans les liste
    @Test
    public void createReunionTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton_AddReunion), withContentDescription("Ajoute une Réunion"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.textInput_Nom_Reunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Nom_Reunion),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("ReunionTest"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.textInput_Date),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Date),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 12, 25));


        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.textInput_Duree_start),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Duree_start),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15, 00));

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.textInput_Duree_end),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Duree_end),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15, 30));

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.autoComplete_Salle_Reunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Salle_Reunion),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(click());


        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //navigate on autocompletetext
        device.pressDPadDown();
        device.pressDPadDown();
        device.pressDPadUp();
        device.pressEnter();


        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInput_Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Email),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("test@gmail.com"));
        textInputEditText5.perform(click());
        textInputEditText5.perform(click());

        textInputEditText5.perform(replaceText("test2@gmail.com"));
        textInputEditText5.perform(click());
        textInputEditText5.perform(click(), closeSoftKeyboard());


        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button_CreateReunion), withText("Créer la Reunion"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout_CreateReunion),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton4.perform(click());

        //test
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("ReunionTest - 15H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("ReunionTest - 15H0 - Mario")));
    }

    // Création puis suppression de la réunion
    @Test
    public void createReunionAndDeleteThisReunionTest() throws InterruptedException {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton_AddReunion), withContentDescription("Ajoute une Réunion"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.textInput_Nom_Reunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Nom_Reunion),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("ReunionTest2"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.textInput_Date),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Date),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 11, 25));


        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.textInput_Duree_start),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Duree_start),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15, 00));

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.textInput_Duree_end),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Duree_end),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15, 30));

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.autoComplete_Salle_Reunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Salle_Reunion),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(click());


        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //navigate on autocompletetext
        device.pressDPadDown();
        device.pressDPadDown();
        device.pressDPadDown();
        device.pressDPadUp();
        device.pressEnter();


        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInput_Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Email),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("test@gmail.com"));
        textInputEditText5.perform(click());
        textInputEditText5.perform(click());

        textInputEditText5.perform(replaceText("test2@gmail.com"));
        textInputEditText5.perform(click());
        textInputEditText5.perform(click(), closeSoftKeyboard());


        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button_CreateReunion), withText("Créer la Reunion"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout_CreateReunion),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(androidx.transition.R.id.title), withText("Filtrer par date"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 11, 25));

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        //test
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("ReunionTest2 - 15H0 - Luigi"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("ReunionTest2 - 15H0 - Luigi")));
        //Delete
        onView(withId(R.id.list_reunion_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));

        Thread.sleep(1000); //wait to delete Reunion

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("ReunionTest2 - 15H0 - Luigi"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());

    }

    // Création d'une réunion vérification de l'affichage dans la liste (nom , salle , liste d'email, heure de départ )
    @Test
    public void createReunionAndCheckNameSalleEmailHourInList() throws InterruptedException {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton_AddReunion), withContentDescription("Ajoute une Réunion"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.textInput_Nom_Reunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Nom_Reunion),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("ReunionTest3"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.textInput_Date),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Date),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 11, 20));


        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.textInput_Duree_start),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Duree_start),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16, 00));

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.textInput_Duree_end),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Duree_end),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(17, 30));

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.autoComplete_Salle_Reunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Salle_Reunion),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(click());


        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //navigate on autocompletetext
        device.pressDPadDown();
        device.pressDPadDown();
        device.pressDPadDown();
        device.pressDPadUp();
        device.pressEnter();


        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInput_Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.outlinedTextField_Email),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("test@gmail.com"));
        textInputEditText5.perform(click());
        textInputEditText5.perform(click());

        textInputEditText5.perform(replaceText("test2@gmail.com"));
        textInputEditText5.perform(click());
        textInputEditText5.perform(click(), closeSoftKeyboard());


        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button_CreateReunion), withText("Créer la Reunion"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout_CreateReunion),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(androidx.transition.R.id.title), withText("Filtrer par date"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 11, 20));

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        //test
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("ReunionTest3 - 16H0 - Luigi"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("ReunionTest3 - 16H0 - Luigi")));

        ViewInteraction textView1 = onView(
                allOf(withId(R.id.item_list_email),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView1.check(matches(withText("test@gmail.com, test2@gmail.com")));

    }

    //Vérification du retour dans liste des réunions
    @Test
    public void checkBackInCreateView() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton_AddReunion), withContentDescription("Ajoute une Réunion"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.action_bar),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //test
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list_reunion_recyclerview),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }

    //Vérification l'affichage de la page création
    @Test
    public void checkIfCreateViewIsExist() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton_AddReunion), withContentDescription("Ajoute une Réunion"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.constraintLayout_CreateReunion),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

    }


    //Vérification l'affichage du menu des filtres
    @Test
    public void checkAllNameOfMenuFilter() {
        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(androidx.core.R.id.title), withText("Filtrer par date"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Filtrer par date")));

        ViewInteraction textView2 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Reset du filtre"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("Reset du filtre")));

        ViewInteraction textView3 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Filtrer par lieu"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("Filtrer par lieu")));

        ViewInteraction appCompatTextView = onView(
                allOf(withId(androidx.core.R.id.title), withText("Filtrer par lieu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Mario"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("Mario")));

        ViewInteraction textView5 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Luigi"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("Luigi")));

        ViewInteraction textView6 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Peach"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView6.check(matches(withText("Peach")));

        ViewInteraction textView7 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Toad"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView7.check(matches(withText("Toad")));

        ViewInteraction textView8 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Daisy"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView8.check(matches(withText("Daisy")));

        ViewInteraction textView9 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Harmonie"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView9.check(matches(withText("Harmonie")));

        ViewInteraction textView10 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Wario"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView10.check(matches(withText("Wario")));

        ViewInteraction textView11 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Waluigi"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView11.check(matches(withText("Waluigi")));

        ViewInteraction textView12 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Yoshi"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView12.check(matches(withText("Yoshi")));

        ViewInteraction textView13 = onView(
                allOf(withId(androidx.core.R.id.title), withText("Bowser"),
                        withParent(withParent(withId(androidx.appcompat.R.id.content))),
                        isDisplayed()));
        textView13.check(matches(withText("Bowser")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}