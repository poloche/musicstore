package org.plc.cetification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface IMusicopedia {

    default boolean createAuthor(Connection connection) {
        String CREATE_AUTHOR = "CREATE TABLE IF NOT EXISTS author(id INTEGER PRIMARY KEY, name TEXT NOT NULL);";
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_AUTHOR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    default boolean createAlbum(Connection connection) {
        String CREATE_AUTHOR = "CREATE TABLE IF NOT EXISTS album(id INTEGER PRIMARY KEY, author int NOT NULL, name text NOT NULL, year INT NOT NULL);";
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_AUTHOR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    default boolean createCancion(Connection connection) {
        String CREATE_AUTHOR = "CREATE TABLE IF NOT EXISTS cancion(id INTEGER PRIMARY KEY, album int NOT NULL, name TEXT NOT NULL, number INT NOT NULL );";
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_AUTHOR);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    Author agregarAutor(String authorName);

    Album agregarAlbum(Author author, String albumName, int year);

    void agregarCancion(Album album, int position, String songName);

    List<Album> getAlbumsByAuthor(String authorName);

    List<Cancion> getSongsByAutor(String songName);

    List<Album> getAlbumsByYear(int year);

    List<Cancion> getAllSongsThatContain(String word);

    List<Cancion> getSongsByAlbum(String... albumNames);

    List<Author> getAutorsBySongName(String songName);

    List<Cancion> getSongsThatStartWith(String startWord);

    boolean isEmptyDB();
}
