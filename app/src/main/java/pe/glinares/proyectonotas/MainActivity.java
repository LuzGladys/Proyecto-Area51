package pe.glinares.proyectonotas;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListFragment.ListFragmentInterface {

    private FragmentManager fragmentManager;

    private static final String LIST_FRAGMENT_TAG = "list_fragment";
    private static final String CONTENT_FRAGMENT_TAG = "content_fragment";

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
            //Toast.makeText(MainActivity.this, "savedInstanceState == null", Toast.LENGTH_SHORT).show();
            final ListFragment listFragment = new ListFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, listFragment, LIST_FRAGMENT_TAG)
                    .commit();
            listFragment.setListFragmentInterface(this);
        } else {
            //Toast.makeText(MainActivity.this, "savedInstanceState != null", Toast.LENGTH_SHORT).show();
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
}
