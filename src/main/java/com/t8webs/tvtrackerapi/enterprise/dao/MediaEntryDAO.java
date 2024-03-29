package com.t8webs.tvtrackerapi.enterprise.dao;


import com.t8webs.tvtrackerapi.enterprise.dto.MediaEntry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Profile("dev")
public class MediaEntryDAO implements IMediaEntryDAO {

    /**
     * Method for creating a new MediaEntry record in the database
     *
     * @param mediaEntry MediaEntry object to be saved as a record in the database
     * @return MediaEntry object representation of new database record
     */
    @Override
    public boolean save(MediaEntry mediaEntry) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQueryUsing(mediaEntry);
        return query.insert();
    }

    /**
     * Method for fetching all MediaEntry records for a given user
     *
     * @param username String uniquely identify a UserAccount record
     * @return List of MediaEntry objects belonging to the given user
     */
    @Override
    public List<MediaEntry> fetchByUsername(String username) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.addWhere("username", username);
        return parse(query.select());
    }

    /**
     * Method for fetching a distinct record in the database
     *
     * @param id integer uniquely identifying a MediaEntry record
     * @return MediaEntry object representation of corresponding database record
     */
    @Override
    public MediaEntry fetch(int id) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.addWhere("id", id);
        List<MediaEntry> entries = parse(query.select());

        if(entries.isEmpty())
            return null;

        return entries.get(0);
    }

    /**
     * Method for deleting a single MediaEntry record in the database
     *
     * @param id integer uniquely identifying a MediaEntry record
     * @return boolean indicating a successfully delete
     */
    @Override
    public boolean delete(int id) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQuery();
        query.addWhere("id", id);
        return query.delete();
    }

    /**
     * Method for updating a MediaEntry record in the database
     *
     * @param mediaEntry MediaEntry object to be used for updating a database record
     * @return boolean indicating a successful update
     */
    @Override
    public boolean update(MediaEntry mediaEntry) throws SQLException, IOException, ClassNotFoundException {
        DbQuery query = newQueryUsing(mediaEntry);
        query.addWhere("id", mediaEntry.getId());
        return query.update();
    }

    /**
     * This method sets all media entry column values for SQL statement
     *
     * @param mediaEntry source of column values
     */
    private DbQuery newQueryUsing(MediaEntry mediaEntry) {
        DbQuery query = newQuery();

        query.setColumnValue("title", mediaEntry.getTitle());
        query.setColumnValue("type", mediaEntry.getType());
        query.setColumnValue("platform", mediaEntry.getPlatform());
        query.setColumnValue("description", mediaEntry.getDescription());
        query.setColumnValue("imageUrl", mediaEntry.getImageUrl());
        query.setColumnValue("watched", mediaEntry.isWatched() ? 1 : 0);
        query.setColumnValue("username", mediaEntry.getUsername());

        return query;
    }

    private DbQuery newQuery() {
        DbQuery dbQuery = new DbQuery();
        dbQuery.setTableName("MediaEntry");
        return dbQuery;
    }

    /**
     * Method for parsing SQL results into List of MediaEntry objects
     *
     * @param results data structure representation of sql results
     * @return List of MediaEntry objects
     */
    private List<MediaEntry> parse(ArrayList<HashMap<String, Object>> results) {
        ArrayList<MediaEntry> mediaEntries = new ArrayList<>();
        for (HashMap valuesMap: results) {
            MediaEntry mediaEntry = new MediaEntry();
            mediaEntry.setId((Integer) valuesMap.get("id"));
            mediaEntry.setTitle((String) valuesMap.get("title"));
            mediaEntry.setType((String) valuesMap.get("type"));
            mediaEntry.setPlatform((String) valuesMap.get("platform"));
            mediaEntry.setDescription((String) valuesMap.get("description"));
            mediaEntry.setImageUrl((String) valuesMap.get("imageUrl"));
            mediaEntry.setWatched((Boolean) valuesMap.get("watched"));
            mediaEntry.setUsername((String) valuesMap.get("username"));
            mediaEntries.add(mediaEntry);
        }
        return mediaEntries;
    }
}
