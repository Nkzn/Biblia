package info.nkzn.biblia.rakuten

/**
 * 楽天ブックス書籍検索API 入力パラメーター version:2013-05-22
 */
data class SearchParams(
        /* 区分:共通パラメーター */
        /** アプリID */
        var applicationId: String? = null,
        /** アフィリエイトID */
        val affiliateId: String? = null,
        /** レスポンス形式 */
        val format: String = "json", // XML来られても困る
        /** 出力パラメータ指定 */
        val elements: List<Elements>? = null,
        /** 出力フォーマットバージョン */
        val formatVersion: Int = 2, // v1来られても困る

        /* 区分:サービス固有パラメーター */
        /** 書籍タイトル */
        val title: String? = null,
        /** 著者名 */
        val author: String? = null,
        /** 出版社名 */
        val publisherName: String? = null,
        /** 書籍のサイズ */
        val size: Size = SearchParams.Size.ALL,
        /** ISBNコード */
        val isbn: String? = null,
        /** 楽天ブックスジャンルID */
        val booksGenreId: String? = null,
        /** 1ページあたりの取得件数	 */
        val hits: Int = 30,
        /** 取得ページ */
        val page: Int = 1,
        /** 在庫状況 */
        val availability: Availability = SearchParams.Availability.ALL,
        /** 品切れ等購入不可商品表示フラグ */
        val outOfStockFlag: OutOfStockFlag = SearchParams.OutOfStockFlag.Invisible,
        /** チラよみフラグ */
        val chirayomiFlag: ChirayomiFlag = SearchParams.ChirayomiFlag.ALL,
        /** ソート */
        val sort: Sort = SearchParams.Sort.Standard,
        /** 限定フラグ */
        val limitedFlag: LimitedFlag = SearchParams.LimitedFlag.ALL,
        /** キャリア */
        val carrier: Carrier = SearchParams.Carrier.PC,
        /** ジャンルごとの商品数取得フラグ */
        val genreInformationFlag: GenreInformationFlag = SearchParams.GenreInformationFlag.NotRequired
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

        map.put("applicationId", applicationId!!)

        if (affiliateId != null) {
            map.put("affiliateId", affiliateId)
        }

        map.put("format", format)

        if (elements != null && !elements.isEmpty()) {
            val joinedElements = elements.map { it.name }.joinToString(separator = ",") { it -> it }
            if (joinedElements.isNotEmpty()) {
                map.put("elements", joinedElements)
            }
        }

        map.put("formatVersion", formatVersion.toString())

        if (title != null) {
            map.put("title", title)
        }

        if (author != null) {
            map.put("author", author)
        }

        if (publisherName != null) {
            map.put("publisherName", publisherName)
        }

        map.put("size", size.code.toString())

        if (isbn != null) {
            map.put("isbn", isbn)
        }

        if (booksGenreId != null) {
            map.put("booksGenreId", booksGenreId)
        }

        map.put("hits", hits.toString())
        map.put("page", page.toString())
        map.put("availability", availability.code.toString())
        map.put("outOfStockFlag", outOfStockFlag.code.toString())
        map.put("chirayomiFlag", chirayomiFlag.code.toString())
        map.put("sort", sort.code)
        map.put("limitedFlag", limitedFlag.code.toString())
        map.put("carrier", carrier.code.toString())
        map.put("genreInformationFlag", genreInformationFlag.code.toString())

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
}


