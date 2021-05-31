package test.module.member;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.member.Member;
import main.backend.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnitParamsRunner.class)
public final class TestMemberLogin {

    private final static Controller CONTROLLER = new Controller();

    private Object[] getIDAndPassword() {
        return new Object[] {
                new Object[] {180, "abc"},     //Test Case 001 To verify Rule 1, wrong id and password
                new Object[] {180000, "abc"},  //Test Case 002 To verify Rule 2, wrong password
                new Object[] {180, "123456"}   //Test Case 003 To verify Rule 3, wrong id
        };
    }

    @Test
    @Parameters(method = "getIDAndPassword")
    public void testInvalidLogin(final int ID, final String password) {
        final Member member = CONTROLLER.searchMember(ID, password);
        assertNull(member);
    }

    //Test Case 004 To verify Rule 4, valid ID and password
    @Test
    public void testMemberLoginFound(){
        //Get actual result
        final Member actualMember = CONTROLLER.searchMember(180000, "123456");
        final Member expectedMember = new Controller().getMemberRecord().getDataList().get(0);
        assertEquals(expectedMember, actualMember);
    }
}