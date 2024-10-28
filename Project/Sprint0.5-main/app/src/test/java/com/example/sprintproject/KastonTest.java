package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import com.example.sprintproject.model.DatabaseSingleton;
import com.example.sprintproject.model.Destination;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

public class KastonTest {
    @Test
    public void testDbSingleton() {
        DatabaseSingleton db = DatabaseSingleton.getDatabase();
        assertEquals(DatabaseSingleton.class, db.getClass());

        assertEquals(FirebaseAuth.class, db.getFirebaseAuthorization().getClass());
        assertEquals(DatabaseReference.class, db.userDb().getClass());
        assertEquals(DatabaseReference.class, db.destinationDb().getClass());
    }

    @Test
    public void calculateVacationTime() {
        int dur;
        dur = Destination.calculateDurationInDays("10/31/24", "11/5/24");
        assertEquals(dur, 6);

        String end;
        end = Destination.calculateEndDate("10/31/2024", 6);
        assertEquals("11/5/2024", end);

        String start;
        start = Destination.calculateStartDate("11/5/2024", 6);
        assertEquals("10/31/2024", start);
    }
}
