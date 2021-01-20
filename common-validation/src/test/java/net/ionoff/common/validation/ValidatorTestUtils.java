package net.ionoff.common.validation;


import net.ionoff.common.validation.message.ValidationMessage;
import org.junit.Assert;

import java.util.List;

public class ValidatorTestUtils {

    static void assertContains(List<ValidationMessage> messages, ValidationMessage message) {
        for (ValidationMessage msg : messages) {
            if (msg.equals(message)) {
                return;
            }
        }
        Assert.fail("Not found message in list. " + message.toString());
    }

    static void assertNotContain(List<ValidationMessage> messages, ValidationMessage message) {
        for (ValidationMessage msg : messages) {
            if (msg.equals(message)) {
                Assert.fail("Not expected message in list. " + message.toString());
            }
        }
    }
}
