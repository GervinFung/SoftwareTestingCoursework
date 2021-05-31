package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.module.customer.TestAreaValidation;
import test.module.customer.TestDistrictValidation;
import test.module.customer.TestNumericValidation;
import test.module.customer.TestStringValidation;
import test.module.filehandler.TestFileExists;
import test.module.filehandler.TestIOFile;
import test.module.item.TestItemList;
import test.module.member.TestContactValidation;
import test.module.member.TestMemberLogin;
import test.module.order.*;
import test.module.payment.TestPaymentTypeFromString;
import test.module.payment.TestPayment;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestAreaValidation.class,
        TestDistrictValidation.class,
        TestNumericValidation.class,
        TestStringValidation.class,
        TestFileExists.class,
        TestIOFile.class,
        TestItemList.class,
        TestContactValidation.class,
        TestMemberLogin.class,
        TestAreaDelivery.class,
        TestItemRetrievalByID.class,
        TestMakeOrderFromFile.class,
        TestOrderException.class,
        TestOrderSameItem.class,
        TestOrderStatusFromString.class,
        TestPaymentTypeFromString.class,
        TestPayment.class
})

public final class ProgramTestSuite {
    private ProgramTestSuite() { throw new RuntimeException("Should not instantiate ProgramTestSuite"); }
}