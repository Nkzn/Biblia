package info.nkzn.biblia

import info.nkzn.biblia.rakuten.Book
import info.nkzn.biblia.rakuten.SearchParams
import rx.Observable

interface ApiClient {
    fun search(title: String): Observable<List<Book>>
    fun search(searchParams: SearchParams): Observable<List<Book>>
}