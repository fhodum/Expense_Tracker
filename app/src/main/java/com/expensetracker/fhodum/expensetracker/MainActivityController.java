package com.expensetracker.fhodum.expensetracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.expensetracker.fhodum.expensetracker.database.ExpenseTrackerDatabase;
import com.expensetracker.fhodum.expensetracker.database.Person;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * Created by fhodum on 5/29/16.
 */
public class MainActivityController extends ControllerBase implements ViewStackController, NavigationView.OnNavigationItemSelectedListener {


    private ExpenseTrackerDatabase database = null;
    List<Person> people;

    Queue<String> views = new LinkedList<>();

    public MainActivityController(Activity activity){
        super(activity);

        database = ExpenseTrackerDatabase.instance(ctxt);

        LocalBroadcastManager.getInstance(ctxt).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.UPDATED_PEOPLE));

        people  = database.getPeople();
        if(people.size()==0){
            new EditSavePersonController(ctxt).showSaveScreen();
        }else{
            showSummary();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            loadSubMenus();
            Log.d("receiver", "Got message: updated-people");
        }
    };
    public void loadSubMenus(){
        NavigationView navigationView = (NavigationView) ctxt.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();


        Menu subMenu = menu.findItem(R.id.people_group_item).getSubMenu();
        subMenu.clear();

        if(people.size() > 0) {
            for (Person person : people) {
                MenuItem myMenuItem = subMenu.add(R.id.people_menu_group, (int) person.getId(), Menu.NONE, person.getName()).setIcon(R.drawable.ic_monetization_on_black_24dp);
                myMenuItem.getItemId();
            }
        }
        else {
            menu.setGroupVisible(R.id.people_menu_group,false);

        }
    }

    private void showSummary(){
        ctxt.setTitle("Summary");
        ViewGroup vg = (ViewGroup)ctxt.findViewById(R.id.mainView);
        vg.removeAllViews();
        new SummaryViewController(ctxt).showPerson(vg);
    }

    private void showPerson(Person person){
        ctxt.setTitle(person.getName());
        ViewGroup vg = (ViewGroup)ctxt.findViewById(R.id.mainView);
        vg.removeAllViews();
        new PersonViewController(ctxt,person).showPerson(vg);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Person person = findPersonById(id);
        if(person != null) {
            showPerson(person);
        }
        else if(id == R.id.add_new_person){
            //Change to new person view
//            ((ViewGroup)parentActivity.findViewById(R.id.mainView)).removeAllViews();
            new EditSavePersonController(ctxt).showSaveScreen();
        } else if (id == R.id.show_summary){
            showSummary();
        }

        DrawerLayout drawer = (DrawerLayout) ctxt.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Person findPersonById(int id){
        Person retVal = null;
        for (Person person:people){
            if(person.getId() == id){
                retVal = person;
                break;
            }
        }
        return retVal;
    }

    @Override
    public boolean handleBackButton() {
        String view =views.remove();
        if (view.equals("Summary")) {
            showSummary();
        }else if(view.equals("Person")){
            //showPerson();
        }
        return false;
    }

    @Override
    public void pushShowSummaryOnStack() {
        views.add("Summary");

    }

    @Override
    public void pushShowPersonOnStack() {
        views.add("Summary");
    }
}
