package pe.glinares.proyectonotas;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListFragment.ListFragmentInterface{

    private FragmentManager fragmentManager;

    private static final String LIST_FRAGMENT_TAG = "list_fragment";
    private static final String CONTENT_FRAGMENT_TAG = "content_fragment";
    private static final String EDIT_FRAGMENT_TAG = "edit_fragment";

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if(isTablet){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (savedInstanceState == null) {
            final ListFragment listFragment = new ListFragment();
            fragmentManager
                    .beginTransaction()
                    .addToBackStack(LIST_FRAGMENT_TAG)
                    .replace(R.id.container, listFragment, LIST_FRAGMENT_TAG)
                    .commit();
            listFragment.setListFragmentInterface(this);
        } else {
            final Fragment fragment = fragmentManager.findFragmentByTag(LIST_FRAGMENT_TAG);
            if (fragment != null) {
                ((ListFragment) fragment).setListFragmentInterface(this);
            }
        }
    }

    @Override
    public void onNoteSelected(Note note) {
        final ContentFragment contentFragment = ContentFragment.newInstance(note);
        if (isTablet) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container2, contentFragment, CONTENT_FRAGMENT_TAG)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, contentFragment, CONTENT_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onNoteSelectedtoEdit(Note note) {
        final EditFragment editFragment = EditFragment.newInstance(note);
        if (isTablet) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container2, editFragment, EDIT_FRAGMENT_TAG)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, editFragment, EDIT_FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void onNoteNew() {
        final EditFragment editFragment = EditFragment.newInstance();
        if (isTablet) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container2, editFragment, EDIT_FRAGMENT_TAG)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, editFragment, EDIT_FRAGMENT_TAG)
                    .commit();
        }
    }
}
