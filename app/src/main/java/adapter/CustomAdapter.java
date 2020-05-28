package adapter;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.harry.example.notetakingapp.EditorActivity;
import com.harry.example.notetakingapp.MainActivity;
import com.harry.example.notetakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import database.NoteEntity;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private List<NoteEntity> noteEntities;
    private Context context;
    private EditNote editNote;

    public CustomAdapter(Context context, List<NoteEntity> noteEntities) {
        this.context = context;
        this.noteEntities = noteEntities;
        editNote=(MainActivity)context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.note_card, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            NoteEntity noteEntity=noteEntities.get(position);
            holder.textView.setText(noteEntity.getContent());
            holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   editNote.editNoteOfId(noteEntity.getId());
                }
            });
    }

    @Override
    public int getItemCount() {
        return noteEntities.size();
    }

    public NoteEntity getNoteAt(int position){
        return noteEntities.get(position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        public TextView textView;
        @BindView(R.id.edit_float_button)
        public FloatingActionButton floatingActionButton;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            textView.setMovementMethod(new ScrollingMovementMethod());
        }
    }
    public interface EditNote{
        void editNoteOfId(int noteId);
    }
}
