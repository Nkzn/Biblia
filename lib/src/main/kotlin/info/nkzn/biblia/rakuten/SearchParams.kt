package info.nkzn.biblia.rakuten

/**
 * 楽天ブックス書籍検索API 入力パラメーター version:2013-05-22
 */
data class SearchParams(
        /* 区分:共通パラメーター */
        /** アプリID */
        var applicationId: String? = null,
        /** アフィリエイトID */
        var affiliateId: String? = null,
        /** レスポンス形式 */
        var format: String = "json", // XML来られても困る
        /** 出力パラメータ指定 */
        var elements: List<Elements>? = null,
        /** 出力フォーマットバージョン */
        var formatVersion: Int = 2, // v1来られても困る

        /* 区分:サービス固有パラメーター */
        /** 書籍タイトル */
        var title: String? = null,
        /** 著者名 */
        var author: String? = null,
        /** 出版社名 */
        var publisherName: String? = null,
        /** 書籍のサイズ */
        var size: Size = SearchParams.Size.ALL,
        /** ISBNコード */
        var isbn: String? = null,
        /** 楽天ブックスジャンルID */
        var booksGenreId: String? = null,
        /** 1ページあたりの取得件数	 */
        var hits: Int = 30,
        /** 取得ページ */
        var page: Int = 1,
        /** 在庫状況 */
        var availability: Availability = SearchParams.Availability.ALL,
        /** 品切れ等購入不可商品表示フラグ */
        var outOfStockFlag: OutOfStockFlag = SearchParams.OutOfStockFlag.Invisible,
        /** チラよみフラグ */
        var chirayomiFlag: ChirayomiFlag = SearchParams.ChirayomiFlag.ALL,
        /** ソート */
        var sort: Sort = SearchParams.Sort.Standard,
        /** 限定フラグ */
        var limitedFlag: LimitedFlag = SearchParams.LimitedFlag.ALL,
        /** キャリア */
        var carrier: Carrier = SearchParams.Carrier.PC,
        /** ジャンルごとの商品数取得フラグ */
        var genreInformationFlag: GenreInformationFlag = SearchParams.GenreInformationFlag.NotRequired
) {
    init {
        // 「タイトル、著者名、出版社名、書籍のサイズ、ISBNコード、楽天ブックスジャンルIDのいずれかが指定されていることが必須です」
        if (title == null
                && author == null
                && publisherName == null
                && isbn == null
                && booksGenreId == null) {
            throw IllegalArgumentException("title or author or publisherName or isbn or booksGenreId should be filled.")
        }
    }

    fun toParams(): Map<String, String> {
        if (applicationId == null) {
            throw IllegalStateException("applicationId must not be empty.")
        }

        val map = mutableMapOf<String, String>()

        applicationId?.let {
            map.put("applicationId", it)
        }
        affiliateId?.let {
            map.put("affiliateId", it )
        }
        format.let {
            map.put("format", format)
        }
        elements?.let {
            it.map { it.name }.joinToString(separator = ",") { it -> it }
        }?.let { joinedElements ->
            if (joinedElements.isNotEmpty()) {
                map.put("elements", joinedElements)
            }
        }
        formatVersion.let {
            map.put("formatVersion", it.toString())
        }
        title?.let {
            map.put("title", it)
        }
        author?.let {
            map.put("author", it)
        }
        publisherName?.let {
            map.put("publisherName", it)
        }
        size.let {
            map.put("size", size.code.toString())
        }
        isbn?.let {
            map.put("isbn", it)
        }
        booksGenreId?.let {
            map.put("booksGenreId", it)
        }
        hits.let {
            map.put("hits", hits.toString())
        }
        page.let {
            map.put("page", page.toString())
        }
        availability.let {
            map.put("availability", availability.code.toString())
        }
        outOfStockFlag.let {
            map.put("outOfStockFlag", outOfStockFlag.code.toString())
        }
        chirayomiFlag.let {
            map.put("chirayomiFlag", chirayomiFlag.code.toString())
        }
        sort.let {
            map.put("sort", it.code)
        }
        limitedFlag.let {
            map.put("limitedFlag", limitedFlag.code.toString())
        }
        carrier.let {
            map.put("carrier", carrier.code.toString())
        }
        genreInformationFlag.let {
            map.put("genreInformationFlag", genreInformationFlag.code.toString())
        }

        return map
    }

    /**
     * 出力パラメーター指定
     */
    enum class Elements {
        /** 書籍タイトル */
        title,
        /** 著者名 */
        author,
        /** 出版社名 */
        publisherName,
        /** 書籍のサイズ */
        size,
        /** ISBNコード */
        isbn,
        /** 楽天ブックスジャンルID */
        booksGenreId,
        /** 1ページあたりの取得件数	 */
        hits,
        /** 取得ページ */
        page,
        /** 在庫状況 */
        availability,
        /** 品切れ等購入不可商品表示フラグ */
        outOfStockFlag,
        /** チラよみフラグ */
        chirayomiFlag,
        /** ソート */
        sort,
        /** 限定フラグ */
        limitedFlag,
        /** キャリア */
        carrier,
        /** ジャンルごとの商品数取得フラグ */
        genreInformationFlag,
    }

    /**
     * 書籍のサイズ
     */
    enum class Size(val code: Int) {
        /** 全て */
        ALL(0),
        /** 単行本 */
        Tankobon(1),
        /** 文庫 */
        Bunko(2),
        /** 新書 */
        Shinsho(3),
        /** 全集・双書 */
        Zenshu(4),
        /** 事・辞典 */
        Jiten(5),
        /** 図鑑 */
        Zukan(6),
        /** 絵本 */
        Ehon(7),
        /** カセット, CDなど */
        CassetteCD(8),
        /** コミック */
        Comic(9),
        /** ムックその他 */
        Others(10)
    }

    /**
     * 在庫状況
     */
    enum class Availability(val code: Int) {
        /** すべての商品 */
        ALL(0),
        /** 在庫あり */
        Available(1),
        /** 通常3～7日程度で発送 */
        ThreeToSeven(2),
        /** 通常3～9日程度で発送 */
        ThreeToNine(3),
        /** メーカー取り寄せ */
        MakerObtaining(4),
        /** 予約受付中 */
        OrderAccepting(5),
        /** メーカーに在庫確認 */
        MakerCheck(6)
    }

    /**
     * 品切れ等購入不可商品表示フラグ
     */
    enum class OutOfStockFlag(val code: Int) {
        /** 品切れや販売終了など購入不可の商品は結果に表示させない */
        Invisible(0),
        /** 品切れや販売終了など購入不可の商品を結果に表示させる */
        Visible(1)
    }

    /**
     * チラよみフラグ
     */
    enum class ChirayomiFlag(val code: Int) {
        /** すべての商品 */
        ALL(0),
        /** チラよみ対象商品で絞り込む */
        ChirayomiOnly(1)
    }

    /**
     * ソート
     */
    enum class Sort(val code: String) {
        /** 標準 */
        Standard("standard"),
        /** 売れている */
        Sales("sales"),
        /** 発売日(古い) */
        OldReleaseDate("+releaseDate"),
        /** 発売日(新しい) */
        NewReleaseDate("-releaseDate"),
        /** 価格が安い */
        LowItemPrice("+itemPrice"),
        /** 価格が高い */
        HighItemPrice("-itemPrice"),
        /** レビューの件数が多い */
        ReviewCount("reviewCount"),
        /** レビューの評価(平均)が高い */
        ReviewAverage("reviewAverage")
    }

    /**
     * 限定フラグ
     */
    enum class LimitedFlag(val code: Int) {
        /** すべての商品 */
        ALL(0),
        /** 限定版商品のみ */
        LimitedOnly(1)
    }

    /**
     * キャリア
     */
    enum class Carrier(val code: Int) {
        /** PC用の情報を返す */
        PC(0),
        /** モバイル用の情報を返す */
        Mobile(1)
    }

    /**
     * ジャンルごとの商品数取得フラグ
     */
    enum class GenreInformationFlag(val code: Int) {
        /** ジャンルごとの商品数の情報を取得しない */
        NotRequired(0),
        /** ジャンルごとの商品数の情報を取得する */
        Required(1)
    }

    /**
     * 検索パラメータを組み立てるBuilder
     * （Kotlinから利用する場合はSearchParamsの名前付きコンストラクタを使うべきです）
     */
    class Builder() {
        private val searchParams: SearchParams = SearchParams()

        /** アフィリエイトID */
        fun affiliateId(affiliateId: String): Builder {
            searchParams.affiliateId = affiliateId
            return this
        }
        /** レスポンス形式 */
        fun format(format: String): Builder {
            searchParams.format = format
            return this
        }
        /** 出力パラメータ指定 */
        fun elements(elements: List<Elements>): Builder {
            searchParams.elements = elements
            return this
        }
        /** 出力フォーマットバージョン */
        fun formatVersion(formatVersion: Int): Builder {
            searchParams.formatVersion = formatVersion
            return this
        }
        /** 書籍タイトル */
        fun title(title: String): Builder {
            searchParams.title = title
            return this
        }
        /** 著者名 */
        fun author(author: String): Builder {
            searchParams.author = author
            return this
        }
        /** 出版社名 */
        fun publisherName(publisherName: String): Builder {
            searchParams.publisherName = publisherName
            return this
        }
        /** 書籍のサイズ */
        fun size(size: Size): Builder {
            searchParams.size = size
            return this
        }
        /** ISBNコード */
        fun isbn(isbn: String): Builder {
            searchParams.isbn = isbn
            return this
        }
        /** 楽天ブックスジャンルID */
        fun booksGenreId(booksGenreId: String): Builder {
            searchParams.booksGenreId = booksGenreId
            return this
        }
        /** 1ページあたりの取得件数	 */
        fun hits(hits: Int): Builder {
            searchParams.hits = hits
            return this
        }
        /** 取得ページ */
        fun page(page: Int): Builder {
            searchParams.page = page
            return this
        }
        /** 在庫状況 */
        fun availability(availability: Availability): Builder {
            searchParams.availability = availability
            return this
        }
        /** 品切れ等購入不可商品表示フラグ */
        fun outOfStockFlag(outOfStockFlag: OutOfStockFlag): Builder {
            searchParams.outOfStockFlag = outOfStockFlag
            return this
        }
        /** チラよみフラグ */
        fun chirayomiFlag(chirayomiFlag: ChirayomiFlag): Builder {
            searchParams.chirayomiFlag = chirayomiFlag
            return this
        }
        /** ソート */
        fun sort(sort: Sort): Builder {
            searchParams.sort = sort
            return this
        }
        /** 限定フラグ */
        fun limitedFlag(limitedFlag: LimitedFlag): Builder {
            searchParams.limitedFlag = limitedFlag
            return this
        }
        /** キャリア */
        fun carrier(carrier: Carrier): Builder {
            searchParams.carrier = carrier
            return this
        }
        /** ジャンルごとの商品数取得フラグ */
        fun genreInformationFlag(genreInformationFlag: GenreInformationFlag): Builder {
            searchParams.genreInformationFlag = genreInformationFlag
            return this
        }
        fun build(): SearchParams? {
            return searchParams
        }
    }
}
