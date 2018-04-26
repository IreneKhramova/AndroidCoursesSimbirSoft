package com.example.irene.androidcourses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private Menu mMenu;
    private NavigationView navigationView;
    private FirebaseUser user;
    private TextView nameTextView;
    private TextView emailTextView;
    private FriendsAdapter friendsAdapter;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback simpleCallback;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.nav_manage: {
                                Context context = MainActivity.this;
                                Intent intent = new Intent(context, ProfileActivity.class);
                                context.startActivity(intent);
                                break;
                            }

                            case R.id.nav_exit: {
                                FirebaseAuth.getInstance().signOut();
                                mMenu.findItem(R.id.nav_exit).setVisible(false);
                                mMenu.findItem(R.id.login).setVisible(true);
                                mMenu.findItem(R.id.nav_manage).setVisible(false);
                                mMenu.findItem(R.id.nav_map).setVisible(false);
                                recyclerView.setAdapter(null);
                                emailTextView.setText("");
                                nameTextView.setText("");
                                break;
                            }

                            case R.id.login: {
                                Context context = MainActivity.this;
                                Intent intent = new Intent(context, AuthActivity.class);
                                context.startActivity(intent);
                                break;
                            }

                            case R.id.nav_map: {
                                Context context = MainActivity.this;
                                Intent intent = new Intent(context, MapsActivity.class);
                                context.startActivity(intent);
                                break;
                            }
                        }

                        return true;
                    }
                });
        mMenu = navigationView.getMenu();
        nameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameView);
        emailTextView = navigationView.getHeaderView(0).findViewById(R.id.userEmailView);
        recyclerView = findViewById(R.id.friends_list);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    nameTextView.setText(u.getName());
                    emailTextView.setText(u.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            loadFriends();
        }

        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Удалить диалог?");

                    builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            friendsAdapter.notifyItemRemoved(position);
                            List<Friend> friends = friendsAdapter.getFriends();
                            friends.remove(position);
                            friendsAdapter.setFriends(friends);
                            return;
                        }
                    }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            friendsAdapter.notifyItemRemoved(position + 1);
                            friendsAdapter.notifyItemRangeChanged(position, friendsAdapter.getItemCount());
                            return;
                        }
                    }).show();
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mAuth.getCurrentUser() != null) {
            mMenu.findItem(R.id.login).setVisible(false);
            mMenu.findItem(R.id.nav_exit).setVisible(true);
            mMenu.findItem(R.id.nav_manage).setVisible(true);
            mMenu.findItem(R.id.nav_map).setVisible(true);
        }
        else {
            mMenu.findItem(R.id.nav_exit).setVisible(false);
            mMenu.findItem(R.id.login).setVisible(true);
            mMenu.findItem(R.id.nav_manage).setVisible(false);
            mMenu.findItem(R.id.nav_map).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void loadFriends() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsAdapter = new FriendsAdapter();
        recyclerView.setAdapter(friendsAdapter);

        final FriendsRepository friendsRepository = new FriendsRepository();
        friendsRepository.loadFriends(new FriendsRepository.FriendsLoadListener() {
            @Override
            public void onFriendsLoaded(List<Friend> friends) {
                FriendsDiffCallback friendsDiffCallback = new FriendsDiffCallback(friendsAdapter.getFriends(), friends);
                DiffUtil.DiffResult friendsDiffResult = DiffUtil.calculateDiff(friendsDiffCallback);

                friendsAdapter.setFriends(friends);

                friendsDiffResult.dispatchUpdatesTo(friendsAdapter);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
