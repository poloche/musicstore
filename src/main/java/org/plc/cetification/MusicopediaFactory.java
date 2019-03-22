package org.plc.cetification;

import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MusicopediaFactory {
    private MusicopediaFactory() {

    }

    public static IMusicopedia getInstance(SQLiteConnectionPoolDataSource ds) throws SQLException {
        return new Musicopedia(ds);
    }

    private static class Musicopedia implements IMusicopedia {
        private int authorNextId;
        private int albumNextId;
        private int cancionNextId;

        private final Connection connection;

        Musicopedia(SQLiteConnectionPoolDataSource ds) throws SQLException {
            this.connection = ds.getConnection();
            createAuthor(connection);
            createAlbum(connection);
            createCancion(connection);
            authorNextId = 0;
            albumNextId = 0;
            cancionNextId = 0;
        }

        public Author agregarAutor(String authorName) {
            authorNextId = authorNextId + 1;
            return Author.insert(authorNextId, authorName, connection);
        }

        @Override
        public List<Author> getAutorsBySongName(String songName) {
            return Author.getBySongName(songName, connection);
        }


        public Album agregarAlbum(Author author, String albumName, int year) {
            albumNextId++;
            return Album.getNewAlbum(albumNextId, albumName, year, author, connection);
        }

        @Override
        public List<Album> getAlbumsByAuthor(String authorName) {
            List<Author> authors = Author.getByName(authorName, connection);
            return Album.getByAuthors(authors, connection);
        }

        @Override
        public List<Album> getAlbumsByYear(int year) {
            return Album.getByYear(year, connection);
        }

        public void agregarCancion(Album album, int position, String songName) {
            cancionNextId++;
            Cancion.insert(cancionNextId, position, songName, album, connection);
        }

        @Override
        public List<Cancion> getSongsByAutor(String authorName) {
            System.out.println("*****************************");
            System.out.println("Listing Songs By Author Name");
            List<Album> albums = getAlbumsByAuthor(authorName);
            return Cancion.getByAlbumns(albums, connection);
        }


        @Override
        public List<Cancion> getAllSongsThatContain(String word) {
            return Cancion.getByName(word, LikeType.Contains, connection);
        }

        @Override
        public List<Cancion> getSongsByAlbum(String... albumNames) {
            return Cancion.getByAlbumName(connection, albumNames);
        }


        @Override
        public List<Cancion> getSongsThatStartWith(String startWord) {
            return Cancion.getByName(startWord, LikeType.Start, connection);
        }

        @Override
        public boolean isEmptyDB() {
            return Author.getAll(connection).isEmpty();
        }
    }
}
