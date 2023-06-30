package com.t8webs.tvtrackerapi.enterprise.dao;


import com.t8webs.tvtrackerapi.enterprise.dto.UserAccount;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Profile("dev")
public class UserAccountDAO implements IUserAccountDAO {


    /**
     * Method for creating a new UserAccount record in the database
     *
     * @param userAccount UserAccount object to be saved as a record in the database
     * @return boolean indicating a successful save
     */
    @Override
    public boolean save(UserAccount userAccount) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.setColumnValue("username", userAccount.getUsername());
        query.setColumnValue("password", userAccount.getPassword());
        query.setColumnValue("email", userAccount.getEmail());
        query.setColumnValue("birthDate", userAccount.getBirthDate());
        query.setColumnValue("token", userAccount.getToken());
        query.setColumnValue("lastLogin", userAccount.getLastLogin());
        return query.insert();
    }

    /**
     * Method for fetching a distinct UserAccount record from the database
     *
     * @param username String uniquely identifying a UserAccount record
     * @return UserAccount representation of the corresponding UserAccount database record
     */
    @Override
    public UserAccount fetch(String username) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.addWhere("username", username);
        List<UserAccount> users = parse(query.select());

        if(users.isEmpty())
            return null;

        return users.get(0);
    }

    /**
     * Method for checking whether a record exists for the given username
     *
     * @param username String uniquely identifying a UserAccount record
     * @return boolean indicating whether a record exists for this username
     */
    @Override
    public boolean existsBy(String username) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.addWhere("username", username);
        return !query.select().isEmpty();
    }

    /**
     * Method for deleting a distinct UserAccount record from the database
     *
     * @param username String uniquely identifying a UserAccount record
     * @return boolean indicating a successful delete
     */
    @Override
    public boolean delete(String username) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.addWhere("username", username);
        return query.delete();
    }

    /**
     * Method for updating an existing UserAccount record in the database
     *
     * @param userAccount UserAccount object to be used for updating a database record
     * @return boolean indicating a successful update
     */
    @Override
    public boolean update(UserAccount userAccount) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.setColumnValue("token", userAccount.getToken());
        query.setColumnValue("lastLogin", userAccount.getLastLogin());
        query.addWhere("username", userAccount.getUsername());
        return query.update();
    }

    private DbQuery newQuery() {
        DbQuery dbQuery = new DbQuery();
        dbQuery.setTableName("UserAccount");
        return dbQuery;
    }

    /**
     * Method for parsing SQL results into List of UserAccount objects
     *
     * @param results data structure representation of sql results
     * @return List of UserAccount objects
     */
    private List<UserAccount> parse(ArrayList<HashMap<String, Object>> results) {
        ArrayList<UserAccount> userAccounts = new ArrayList<>();
        for (HashMap valuesMap: results) {
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername((String) valuesMap.get("username"));
            userAccount.setPassword((String) valuesMap.get("password"));
            userAccount.setEmail((String) valuesMap.get("email"));
            userAccount.setBirthDate((Timestamp) valuesMap.get("birthdate"));
            userAccount.setLastLogin((Timestamp) valuesMap.get("lastLogin"));
            userAccount.setToken((String) valuesMap.get("token"));
            userAccounts.add(userAccount);
        }
        return userAccounts;
    }
}
