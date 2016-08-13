package info.nkzn.biblia.rakuten

import info.nkzn.biblia.ApiClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable

class RakutenApiClient(): ApiClient {

    companion object Static {
        lateinit var applicationId: String
    }

    override fun search(title: String): Observable<List<Book>> = search(SearchParams(title = title))

    override fun search(params: SearchParams): Observable<List<Book>> {
        params.applicationId = applicationId

        val retrofit = Retrofit.Builder()
                .baseUrl("https://app.rakuten.co.jp/services/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(RakutenBooks::class.java)
                .search(params.toParams())
                .map { it.Items }
    }
}