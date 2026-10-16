package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbPersonResponse {
    @SerializedName("results")
    public List<TmdbPersonDto> results;
}
