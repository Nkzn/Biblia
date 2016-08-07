package info.nkzn.biblia

import info.nkzn.biblia.rakuten.Book
import rx.Observable

interface ApiClient {
    fun search(title: String): Observable<List<Book>>
}