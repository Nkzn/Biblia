package info.nkzn.biblia.rakuten

import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

interface RakutenBooks {
    @GET("BooksBook/Search/20130522")
    fun search(@QueryMap params: Map<String, String>): Observable<SearchResult>
}
