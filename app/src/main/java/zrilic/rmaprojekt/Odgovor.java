package zrilic.rmaprojekt;

import java.util.List;

public class Odgovor {

    private List<Song> data;

    public List<Song> getSongs() {
        return data;
    }

    public void setSongs(List<Song> songs) {
        this.data = songs;
    }
}
