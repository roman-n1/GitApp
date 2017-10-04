package com.yarullin.roman.gitapp.network.responce;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pageable<Data> {
    @SerializedName("total_count") private int totalCount;
    @SerializedName("incomplete_results") private boolean incompleteResults;
    @SerializedName("items") private List<Data> items;

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<Data> getItems() {
        return items;
    }
}
