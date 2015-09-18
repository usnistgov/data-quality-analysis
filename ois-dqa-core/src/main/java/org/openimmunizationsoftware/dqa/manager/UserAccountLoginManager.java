package org.openimmunizationsoftware.dqa.manager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.UserAccount;

public class UserAccountLoginManager
{
  public static boolean login(UserAccount userAccount, String password, Session session)
  {

    String passwordHashed = userAccount.getPassword();
    if (!passwordHashed.startsWith("1000:"))
    {
      if (passwordHashed.equals(password))
      {
        // update hashed password
        try
        {
          passwordHashed = HashManager.generateStrongPasswordHash(password);
        } catch (Exception e)
        {
          e.printStackTrace();
        }
        Transaction tx = session.beginTransaction();
        userAccount.setPassword(passwordHashed);
        session.update(userAccount);
        tx.commit();
        return true;
      }
      return false;
    }
    boolean valid = false;
    try
    {
      valid = HashManager.validatePassword(password, userAccount.getPassword());
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return valid;
  }
}
