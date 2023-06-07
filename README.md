# PlayListMaker
Подключение изображения через Glide

private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image = findViewById<ImageView>(R.id.image)

        Glide.with(applicationContext).load(imageUrl).into(image)
    }