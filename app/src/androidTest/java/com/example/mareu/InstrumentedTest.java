package com.example.mareu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import com.example.mareu.main.MainActivity;

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
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion A - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Réunion A - 19H0 - Mario")));

        onView(withId(R.id.list_reunion_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));

        Thread.sleep(1000); //wait for delete animation

        textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion A - 19H0 - Mario"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(doesNotExist());
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


    // Suppression d'une réunion dans un filtre date vérification si elle est bien supprimé de la liste du filtre et de la liste générale
//
    // Suppression d'une réunion dans un filtre lieu vérification si elle est bien supprimé de la liste du filtre et de la liste générale
//
    // Création d'une réunion vérification si la réunion est créée dans toute les liste
//
    // Pendant la création d'une réunion vérification si la liste des salle est bien filtré
//
    // Création puis suppression du réunion
//
    // Création d'une réunion vérification de l'affichage dans la liste (nom , salle , date , liste d'email, heure de départ )
//
    //         Vérification du retour dans liste des réunions
//
    //         Dans création affichage du time picker
//
    //         Dans création affichage du date picker
//
    //         Dans le filtre affichage du date picker
//
    //         Affichage de la page création
//
    //         selection menu
}