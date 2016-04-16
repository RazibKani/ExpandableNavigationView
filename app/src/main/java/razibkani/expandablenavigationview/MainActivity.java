package razibkani.expandablenavigationview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by razibkani on 4/3/16.
 */
public class MainActivity extends AppCompatActivity implements ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener {

    private ExpandableListView sidebarList;
    private SidebarAdapter mAdapter;
    private DrawerLayout drawer;
    private List<String> listParentSidebar;
    private List<String> programChild;
    private HashMap<String, List<String>> listChildSidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setListData(); // load data
        mAdapter = new SidebarAdapter(this, listParentSidebar, listChildSidebar); // init adapter

        // initialize expandable list
        sidebarList = (ExpandableListView) findViewById(R.id.sidebar_list);
        sidebarList.setAdapter(mAdapter);
        sidebarList.setOnGroupClickListener(this);
        sidebarList.setOnChildClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListData() {
        listParentSidebar = new ArrayList<String>();
        listChildSidebar = new HashMap<String, List<String>>();

        // Adding parent data
        listParentSidebar.add(Constant.S_POS_HOME, "Home");
        listParentSidebar.add(Constant.S_POS_PROFILE, "Profile");
        listParentSidebar.add(Constant.S_POS_PROGRAM, "Program");
        listParentSidebar.add(Constant.S_POS_ABOUT, "About Us");

        // Adding child data
        programChild = new ArrayList<String>();
        programChild.add("Program 1");
        programChild.add("Program 2");
        programChild.add("Program 3");

        // Set child to particular parent
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_HOME), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_PROFILE), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_PROGRAM), programChild);
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_ABOUT), new ArrayList<String>()); // adding empty child
    }

    // Handling click on child item
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (groupPosition == 2) { // index program
            showToast(programChild.get(childPosition));
        }

        drawer.closeDrawer(GravityCompat.START); // close drawer
        return true;
    }

    // Handling click on parent item
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        // Flag for group that have child, then parent will expand or collapse
        boolean isHaveChild = false;

        switch (groupPosition) {
            case Constant.S_POS_HOME:
                showToast("Home Clicked!");
                break;

            case Constant.S_POS_PROFILE:
                showToast("Profile Clicked!");
                break;

            case Constant.S_POS_PROGRAM:
                isHaveChild = true; // have child

                if (parent.isGroupExpanded(groupPosition)) // if parent expanded
                    parent.collapseGroup(groupPosition); // collapse parent
                else
                    parent.expandGroup(groupPosition); // expand parent
                break;

            case Constant.S_POS_ABOUT:
                showToast("About Clicked!");
                break;

            default:
                break;
        }

        if (!isHaveChild) { // if don't have child, close drawer
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
