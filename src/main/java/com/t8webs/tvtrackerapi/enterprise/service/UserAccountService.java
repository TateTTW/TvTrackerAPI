package com.t8webs.tvtrackerapi.enterprise.service;

import com.t8webs.tvtrackerapi.enterprise.dao.IUserAccountDAO;
import com.t8webs.tvtrackerapi.enterprise.dto.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class UserAccountService implements IUserAccountService {

    @Autowired
    IUserAccountDAO userAccountDAO;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    /**
     * Creates a new UserAccount database record from a UserAccount object.
     *
     * Returns null if an error occurs or if an account already exists with the given username.
     *
     * @param userAccount UserAccount object representing a user to be created
     * @return newly created UserAccount object
     */
    @Override
    @CachePut(value="userAccount", key="#userAccount.username")
    public UserAccount createUserAccount(UserAccount userAccount) throws SQLException, IOException, ClassNotFoundException {
        String token = generateNewToken();
        userAccount.setToken(token);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userAccount.setLastLogin(timestamp);

        boolean success = userAccountDAO.save(userAccount);

        if(success) {
            return userAccount;
        }

        return null;
    }

    /**
     * Indicates whether a user with the given username already exists.
     *
     * @param userAccount UserAccount to compare against database records
     * @return boolean indicating whether a user with this username exists
     */
    public boolean userAccountExists(UserAccount userAccount) throws SQLException, IOException, ClassNotFoundException {
        return userAccountDAO.existsBy(userAccount.getUsername());
    }

    /**
     * Retrieves a UserAccount object with the given username.
     *
     * Returns null if a user account with the given username could not be found.
     *
     * @param username String uniquely identifying a user
     * @return UserAccount object for the given username
     */
    @Override
//    @Cacheable(value="userAccount", key="#username")
    public UserAccount fetchUserAccount(String username) throws SQLException, IOException, ClassNotFoundException {
        if(username == null)
            return null;

        return userAccountDAO.fetch(username);
    }

    /**
     * Indicates whether a token is valid for a given UserAccount
     *
     * @param userAccount UserAccount object to compare token against
     * @param token String to validate for the given user
     * @return boolean indicating whether the token is valid for the given user
     */
    @Override
    public boolean isTokenValid(UserAccount userAccount, String token) throws SQLException, IOException, ClassNotFoundException {
        if(userAccount == null || userAccount.getToken() == null || userAccount.getLastLogin() == null)
            return false;

        if(!userAccount.getToken().equals(token))
            return false;

        // Verify user's token was created within the last hour
        Instant lastLogin = userAccount.getLastLogin().toInstant();
        Duration hour = Duration.ofHours( 1 );
        Instant timeCutOff = lastLogin.plus(hour);

        return Instant.now().isBefore(timeCutOff);
    }

    /**
     * Updates the token and lastLogin for a UserAccount
     *
     * @param userAccount UserAccount object to create a new valid token for
     * @return UserAccount object containing a valid token
     */
    @Override
    @CachePut(value="userAccount", key="#userAccount.username")
    public UserAccount updateUserToken(UserAccount userAccount) throws SQLException, IOException, ClassNotFoundException {
        if(userAccount == null)
            return null;

        String token = generateNewToken();
        userAccount.setToken(token);
        userAccount.setLastLogin(new Timestamp(System.currentTimeMillis()));

        boolean success = userAccountDAO.update(userAccount);

        if(success) {
            return userAccount;
        }

        return null;
    }

    /**
     * @return randomly generated character string for authentication
     */
    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
