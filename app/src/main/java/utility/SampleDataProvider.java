package utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import database.NoteEntity;

public class SampleDataProvider {
    public static final String SAMPLE_TEXT_1="A material metaphor is the unifying theory of a rationalized space and a system of motion.The fundamentals of light, surface, and movement are key to conveying how objects move,interact, and exist in space and in relation to each other. Realistic lighting showsseams, divides space, and indicates moving parts.";
    public static final String SAMPLE_TEXT_2="Surfaces and edges of the material provide visual cues that are grounded in reality.";
    public static final String SAMPLE_TEXT_3="Yet the flexibility of the material creates new affordances that supercede those in the physical world, without breaking the rules of physics.";
    public static Date getDate(int diffAmount){
        GregorianCalendar gregorianCalendar=new GregorianCalendar();
        gregorianCalendar.add(Calendar.MILLISECOND,diffAmount);
        return gregorianCalendar.getTime();
    }
    public static List<NoteEntity> getSampleData(){
        List<NoteEntity> noteEntities=new ArrayList<>();
        noteEntities.add(new NoteEntity(getDate(0),SAMPLE_TEXT_1));
        noteEntities.add(new NoteEntity(getDate(-1),SAMPLE_TEXT_2));
        noteEntities.add(new NoteEntity(getDate(-2),SAMPLE_TEXT_3));
        return noteEntities;
    }
}
