package shows.kristijanmitrov.model

data class ShowsResultData(
    val isSuccessful: Boolean,
    val shows: ArrayList<Show> = arrayListOf()
)