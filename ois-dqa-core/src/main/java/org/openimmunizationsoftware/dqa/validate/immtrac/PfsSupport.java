/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.validate.immtrac;

public class PfsSupport
{
  /**
   * 
   * @param pfsNumber
   * @return
   */
  public static boolean verifyCorrect(String pfsNumber)
  {
    boolean good = false;
    if (pfsNumber != null && pfsNumber.length() == 10)
    {
      good = true;
      for (char c : pfsNumber.toCharArray())
      {
        if (c < '0' || c > '9')
        {
          good = false;
          break;
        }
      }
      if (good)
      {
        good = createCheckDigit(pfsNumber.substring(0, 9)) == pfsNumber.charAt(9);
      }
    }
    return good;
  }
  
  /**
   * This was originally written in C++ for ImmTrac. It has been replicated
   * here in Java. NAB 09/28/2011
   * @param pfs_number
   * @return
   */
  public static char createCheckDigit(String pfs_number)
  {
    // Original C code: 
    // -- int PFS::PFSCheckDigit( const CString& pfs_number)
    // -- {
    // --   // validation routine specified by TDH interoffice memo:
    // --   // - multiply each digit by 1 or 2 alternately
    // --   // - add EACH digit of the products (emphasis in original)
    // --   // - subtract the total from the next highest multiple of 10.
    // --   // One wrinkle is that the facility number may start with an
    // --   // alpha character.
    // --   // I think that the following implements this, but someone should
    // --   // validate it.
    // --   
    // --   int i; // index into string
    // --   int lim = pfs_number.GetLength();
    // --   int one_or_two = 1;
    // --   int product;
    // --   int digit_sum = 0;
    // --   char buf[2] = { '\0', '\0' };
    int i = 0;
    int lim = pfs_number.length();
    int one_or_two = 1;
    int product;
    int digit_sum = 0;
    char buf[] = {'\0', '\0'};
    // --   
    // --   // skip first char if alpha 
    // --   i = isalpha(pfs_number[0]) ? 1 : 0;
    // NAB 09/28/2011 not skipping alpha because this not expected any more
    // --   
    // --   while (i < lim)
    // --   {
    // --     buf[0] = pfs_number[i++];
    // --     product = atoi(buf) * one_or_two;
    // --     one_or_two ^= 3; // if 2, sets to 1; if 1, sets to 2
    // --  
    // --     // JBF 09/24/1995
    // --     // No product will be greater than 18 (2*9) so for
    // --     // products greater than 9 we add 1 (for the Tens position)
    // --     // plus the difference between 10 and the product
    // --     // (the Ones position). This simplifies to either
    // --     // digit_sum + product or digit_sum + product - 9.
    // --  
    // --     digit_sum += (product < 10) ? product : product - 9;
    // --   }
    // --  
    while (i < lim) {
      buf[0] = pfs_number.charAt(i++);
      product = (((int) buf[0]) - ((int) '0')) * one_or_two; 
      one_or_two = one_or_two == 2 ? 1 : 2;
      digit_sum += (product < 10) ? product : product - 9;
    }
    // --   // If the sum is an even multiple of 10 return 0,
    // --   // otherwise return the difference between the sum
    // --   // of the digits and the next higher multiple of 10.
    // --     
    // --   return (10 - (digit_sum % 10)) % 10;
    // -- }
    // NAB 09/28/2011 same return, but converted to a char instead of an int
    return (char) (((int) '0') + ((10 - (digit_sum % 10)) % 10)); 
  }
}
