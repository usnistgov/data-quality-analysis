package org.openimmunizationsoftware.dqa.validate;

import org.openimmunizationsoftware.dqa.db.model.received.types.PhoneNumber;

import junit.framework.TestCase;

public class ValidatorTest extends TestCase
{
  public void testValidatePhone()
  {
    PhoneNumber phone;
    phone = new PhoneNumber("(801)285-7998");
    assertTrue(Validator.isValidPhone(phone));
    phone = new PhoneNumber("801-285-7998");
    assertTrue(Validator.isValidPhone(phone));
    phone = new PhoneNumber("8012857998");
    assertTrue(Validator.isValidPhone(phone));
    phone = new PhoneNumber("801.285.7998");
    assertTrue(Validator.isValidPhone(phone));
    phone = new PhoneNumber("801", "285-7998");
    assertTrue(Validator.isValidPhone(phone));
    phone = new PhoneNumber("123", "285-7998");
    assertFalse("123 is an invalid area code", Validator.isValidPhone(phone));
    phone = new PhoneNumber("801", "123-7998");
    assertFalse("123 is an invalid local exchange", Validator.isValidPhone(phone));
    phone = new PhoneNumber("801", "811-7998");
    assertFalse("811 is an invalid local exchange", Validator.isValidPhone(phone));
    phone = new PhoneNumber("801", "285-1234");
    assertTrue(Validator.isValidPhone(phone));
  }
  
  public void testHasTooManyConsecutiveChars()
  {
  	assertFalse(Validator.hasTooManyConsecutiveChars("", 1));
  	
  	assertFalse(Validator.hasTooManyConsecutiveChars("1", 1));
  	assertTrue(Validator.hasTooManyConsecutiveChars("11", 1));
  	assertFalse(Validator.hasTooManyConsecutiveChars("11", 2));
  	
  	assertFalse(Validator.hasTooManyConsecutiveChars("11111", 6)); // 5
  	assertFalse(Validator.hasTooManyConsecutiveChars("111111", 6)); // 6
  	assertTrue(Validator.hasTooManyConsecutiveChars("1111111", 6)); // 7
  	
  	assertFalse(Validator.hasTooManyConsecutiveChars("111116", 6)); // 5 pre
  	assertFalse(Validator.hasTooManyConsecutiveChars("611111", 6)); // 5 post
  	assertFalse(Validator.hasTooManyConsecutiveChars("1111117", 6)); // 6 pre
  	assertFalse(Validator.hasTooManyConsecutiveChars("7111111", 6)); // 6 post
  	assertTrue(Validator.hasTooManyConsecutiveChars("11111118", 6)); // 7 pre
  	assertTrue(Validator.hasTooManyConsecutiveChars("81111111", 6)); // 7 post
  }
}
