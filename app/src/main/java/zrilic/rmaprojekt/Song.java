package zrilic.rmaprojekt;

import java.io.Serializable;

public class Song implements Serializable {

    private int id;
    private String strAlbum;
    private String strArtist;
    private String strTrack;
    private String strDescription;
    private String strGenre;
    private String strMusicVid;
    private String strTrackThumb;

    public Song(int id, String strAlbum, String strArtist,
                String strTrack, String strDescription,
                String strGenre, String strMusicVid, String strTrackThumb) {
        this.id = id;
        this.strAlbum = strAlbum;
        this.strArtist = strArtist;
        this.strTrack = strTrack;
        this.strDescription = strDescription;
        this.strGenre = strGenre;
        this.strMusicVid = strMusicVid;
        this.strTrackThumb = strTrackThumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrAlbum() {
        return strAlbum;
    }

    public void setStrAlbum(String strAlbum) {
        this.strAlbum = strAlbum;
    }

    public String getStrArtist() {
        return strArtist;
    }

    public void setStrArtist(String strArtist) {
        this.strArtist = strArtist;
    }

    public String getStrTrack() {
        return strTrack;
    }

    public void setStrTrack(String strTrack) {
        this.strTrack = strTrack;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrGenre() {
        return strGenre;
    }

    public void setStrGenre(String strGenre) {
        this.strGenre = strGenre;
    }

    public String getStrMusicVid() {
        return strMusicVid;
    }

    public void setStrMusicVid(String strMusicVid) {
        this.strMusicVid = strMusicVid;
    }

    public String getStrTrackThumb() {
        return strTrackThumb;
    }

    public void setStrTrackThumb(String strTrackThumb) {
        this.strTrackThumb = strTrackThumb;
    }
}
