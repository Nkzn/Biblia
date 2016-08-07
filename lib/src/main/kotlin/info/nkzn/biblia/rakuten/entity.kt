package info.nkzn.biblia.rakuten

data class SearchResult(
        val count: Int,
        val page: Int,
        val first: Int,
        val last: Int,
        val hits: Int,
        val carrier: Int,
        val pageCount: Int,
        val Items: List<Book>, // formatVersion=2
        val GenreInformation: List<Any>
)

data class Book(
        val title: String,
        val titleKana: String,
        val subTitle: String,
        val subTitleKana: String,
        val seriesName: String,
        val seriesNameKana: String,
        val contents: String,
        val author: String,
        val authorKana: String,
        val publisherName: String,
        val size: String,
        val isbn: String,
        val itemCaption: String,
        val salesDate: String,
        val itemPrice: Int,
        val listPrice: Int,
        val discountRate: Int,
        val discountPrice: Int,
        val itemUrl: String,
        val affiliateUrl: String,
        val smallImageUrl: String,
        val mediumImageUrl: String,
        val largeImageUrl: String,
        val chirayomiUrl: String,
        val availability: String,
        val postageFlag: Int,
        val limitedFlag: Int,
        val reviewCount: Int,
        val reviewAverage: String,
        val booksGenreId: String
)
