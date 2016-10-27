package pe.glinares.proyectonotas;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESTUDIO on 16/10/2016.
 */
public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment";

    private ListView listViewItems;

    private ListFragmentInterface listFragmentInterface;

    private NoteArrayAdapter noteArrayAdapter;

    private SQLiteManager sqLiteManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        listViewItems = (ListView) view.findViewById(R.id.listview_items);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        sqLiteManager = SQLiteManager.getInstance(getActivity());
        //noteArrayAdapter = new NoteArrayAdapter(getActivity(), createTestNotes(100));
        noteArrayAdapter = new NoteArrayAdapter(getContext(), createTestNotes(100));//sqLiteManager.getNotes()
        listViewItems.setAdapter(noteArrayAdapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listFragmentInterface != null) {
                    //final Note note = ((NoteArrayAdapter) listViewItems.getAdapter()).getItem(position);
                    final Note note = noteArrayAdapter.getItem(position);
                    listFragmentInterface.onNoteSelected(note);
                }
            }
        });

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if (listFragmentInterface != null) {
                    AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                    adb.setTitle(noteArrayAdapter.getItem(position).getTitle());
                    adb.setMessage("Selecciona la acción a realizar.");
                    adb.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface adb, int id) {
                            editar();
                        }
                    });
                    adb.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface adb, int id) {
                            eliminar(position);
                        }
                    });
                    adb.show();
                }
                return true;
            }
        });
    }

    private void editar(){

    }

    private void eliminar(int position){
        final Note note = noteArrayAdapter.getItem(position);
        boolean isDeleted = sqLiteManager.deleteNote(note.getId());
        if(isDeleted){
            noteArrayAdapter.remove(note);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_note:
                final String title = "Title";
                final String content = "Content";
                final long creationTimestamp = System.currentTimeMillis();
                final long modificationTimestamp = System.currentTimeMillis();
                final long id = sqLiteManager.insertNote(new Note(-1, title, content, creationTimestamp, modificationTimestamp));
                System.out.println("id:"+id);
                final Note note = new Note(id, title + " "+ String.valueOf(id), content, creationTimestamp, modificationTimestamp);
                noteArrayAdapter.add(note);
                return true;
            case R.id.action_edit_note:
                return true;
//            case R.id.action_delete_note:
//                return true;
            default:
                return false;
        }
    }

    public void setListFragmentInterface(ListFragmentInterface listFragmentInterface) {
        this.listFragmentInterface = listFragmentInterface;
    }

    private List<Note> createTestNotes(final int size) {
        final List<Note> notes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final Note note = new Note(
                    i + 1,
                    "Title " + (i + 1),
                    "This is the content of the note " + (i + 1) + ".",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );
            notes.add(note);
        }
        return notes;
    }

    public static class NoteArrayAdapter extends ArrayAdapter<Note> {

        public static final String TAG = "NoteArrayAdapter";

        public NoteArrayAdapter(final Context context, final List<Note> notes) {
            super(context, 0, notes);
        }

        private static class NoteViewHolder {
            private TextView textViewDate;
            private TextView textViewTitle;
            //private TextView textViewContent;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView() - position: " + position + "; convertView " + (convertView != null ? "!= null" : "== null"));
            final View viewListElement;
            final NoteViewHolder noteViewHolder;
            if (convertView != null) {
                viewListElement = convertView;
                noteViewHolder = (NoteViewHolder) viewListElement.getTag();
            } else {
                final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                viewListElement = layoutInflater.inflate(R.layout.element_note, parent, false);
                noteViewHolder = new NoteViewHolder();
                noteViewHolder.textViewDate = (TextView) viewListElement.findViewById(R.id.textview_date);
                noteViewHolder.textViewTitle = (TextView) viewListElement.findViewById(R.id.textview_title);
                //noteViewHolder.textViewContent = (TextView) viewListElement.findViewById(R.id.textview_content);
                viewListElement.setTag(noteViewHolder);
            }
            final Note note = getItem(position);
            noteViewHolder.textViewDate.setText(note.getCreationTimestamp());
            noteViewHolder.textViewTitle.setText(note.getTitle());
            //noteViewHolder.textViewContent.setText(note.getContent());
            return viewListElement;
        }

    }

    public interface ListFragmentInterface {

        void onNoteSelected(final Note note);

    }
}
