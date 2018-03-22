package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RemarkTest {
    @Test
    public void isEqualRemark() {
        Remark remark = new Remark("Test");

        // same object -> returns true
        assertTrue(remark.equals(remark));

        //same values -> returns true
        Remark sameRemark = new Remark(remark.value);
        assertTrue(remark.equals(sameRemark));

        //different types -> returns false
        assertFalse(remark.equals(1));

        //null -> returns false
        assertFalse(remark.equals(null));

        //different remarks -> returns false
        Remark differentRemark = new Remark("Different");
        assertFalse(remark.equals(differentRemark));

    }
}
