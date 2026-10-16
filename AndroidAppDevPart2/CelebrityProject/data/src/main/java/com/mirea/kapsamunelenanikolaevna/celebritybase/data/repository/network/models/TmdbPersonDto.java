package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models;


import com.google.gson.annotations.SerializedName;

public class TmdbPersonDto {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_path")
    public String profilePath;

    @SerializedName("known_for_department")
    public String department;
}
