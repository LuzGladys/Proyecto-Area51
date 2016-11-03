package pe.glinares.proyectonotas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ESTUDIO on 2/11/2016.
 */
public class EditFragment extends Fragment {

    private SQLiteManager sqLiteManager;
//    private EditFragmentInterface editFragmentInterface;

    private EditText titleEditText;
    private EditText contentEditText;
    private Button saveButton;
    private String typeEdit;

    private final static String ARG_NOTE_ID = "id";
    private final static String ARG_NOTE_TITLE = "title";
    private final static String ARG_NOTE_CONTENT = "content";
    private final static String ARG_EDIT = "edit";

    public static EditFragment newInstance(final Note note) {
        final EditFragment editFragment = new EditFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(ARG_NOTE_ID,note.getId());
        arguments.putString(ARG_NOTE_TITLE, note.getTitle());
        arguments.putString(ARG_NOTE_CONTENT, note.getContent());
        arguments.putString(ARG_EDIT, "1");
        editFragment.setArguments(arguments);
        return editFragment;
    }

    public static EditFragment newInstance() {
        final EditFragment editFragment = new EditFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(ARG_EDIT, "0");
        editFragment.setArguments(arguments);
        return editFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit, container, false);
        titleEditText = (EditText) view.findViewById(R.id.edittext_title);
        contentEditText = (EditText) view.findViewById(R.id.edittext_content);
        saveButton = (Button) view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeEdit = getArguments().getString(ARG_EDIT);
                if(typeEdit.equals("1")){
                    long id = getArguments().getLong(ARG_NOTE_ID);
                    String title = titleEditText.getText().toString();
                    String content = contentEditText.getText().toString();
                    Note note = new Note(id,title,content,System.currentTimeMillis(),System.currentTimeMillis());
                    sqLiteManager.updateNote(note);
                }else{
                    String title = titleEditText.getText().toString();
                    String content = contentEditText.getText().toString();
                    long id = sqLiteManager.insertNote(new Note(title, content, System.currentTimeMillis(),System.currentTimeMillis()));
                    System.out.println("id:"+id);
                }
                ////
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sqLiteManager = SQLiteManager.getInstance(getActivity());
        showNote();
    }

    private void showNote(){
        typeEdit = getArguments().getString(ARG_EDIT);

        if(typeEdit.equals("1")){
            final String noteTitle = getArguments().getString(ARG_NOTE_TITLE);
            final String noteContent = getArguments().getString(ARG_NOTE_CONTENT);
            titleEditText.setText(noteTitle);
            contentEditText.setText(noteContent);
        }
    }

/*    public void setEditFragmentInterface(EditFragmentInterface editFragmentInterface) {
        this.editFragmentInterface = editFragmentInterface;
    }

    public interface EditFragmentInterface {
        void onNoteSelectedtoEdit(final Note note);

    }*/
}
