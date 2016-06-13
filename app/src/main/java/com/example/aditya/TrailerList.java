package com.example.aditya.nanodegreeproj1;





        import java.util.ArrayList;
        import java.util.List;
        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class TrailerList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers = new ArrayList<Trailer>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Trailer> getTrailers() {
        return trailers;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setTrailers(List<Trailer> trailers) {
        this.trailers= trailers;
    }

}